package com.hotel.management.aiassistant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.management.booking.Booking;
import com.hotel.management.booking.BookingRepository;
import com.hotel.management.booking.BookingStatus;
import com.hotel.management.config.AppProperties;
import com.hotel.management.food.FoodItem;
import com.hotel.management.food.FoodItemRepository;
import com.hotel.management.room.Room;
import com.hotel.management.room.RoomRepository;
import com.hotel.management.room.RoomType;
import com.hotel.management.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);

    private final AppProperties appProperties;
    private final RoomRepository roomRepository;
    private final FoodItemRepository foodItemRepository;
    private final BookingRepository bookingRepository;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public GeminiService(AppProperties appProperties,
                         RoomRepository roomRepository,
                         FoodItemRepository foodItemRepository,
                         BookingRepository bookingRepository) {
        this.appProperties = appProperties;
        this.roomRepository = roomRepository;
        this.foodItemRepository = foodItemRepository;
        this.bookingRepository = bookingRepository;
        this.restClient = RestClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }

    public String generateResponse(String userMessage, User user) {
        String apiKey = appProperties.getGemini().getApiKey();
        if (!StringUtils.hasText(apiKey)) {
            return "Hello! I am your Royal Stay Hotel concierge. The GEMINI_API_KEY is currently not configured in the application environment, but our full room catalog and dining menus are active and ready for your booking!";
        }

        try {
            // Build filtered hotel contexts
            String roomContext = buildRoomContext(userMessage);
            String foodContext = buildFoodContext(userMessage);
            String bookingContext = buildBookingContext(user);

            String prompt = String.format("""
                    You are the Royal Stay Luxury Hotel AI Concierge.
                    Your responsibilities:
                    - Recommend available hotel rooms, suites, amenities, and room pricing
                    - Recommend dishes from our Indian & Continental dining menu
                    - Help guests navigate reservations and dining orders
                    - Answer booking inquiries accurately using ONLY the live hotel data below
                    - Maintain strict confidentiality: NEVER discuss or disclose any guest information outside of the current authenticated user's own reservations.
                    
                    =========================
                    HOTEL ROOMS DATA:
                    =========================
                    %s
                    
                    =========================
                    DINING MENU DATA:
                    =========================
                    %s
                    
                    =========================
                    GUEST'S PERSONAL RESERVATION:
                    =========================
                    %s
                    
                    =========================
                    GUEST INQUIRY:
                    =========================
                    %s
                    
                    Give a warm, courteous, professional, and helpful response.
                    """, roomContext, foodContext, bookingContext, userMessage);

            // Construct Gemini REST API payload
            Map<String, Object> payload = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", prompt)
                            ))
                    )
            );

            String model = appProperties.getGemini().getModel();
            String uri = String.format(
                    "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                    model, apiKey
            );

            String responseJson = restClient.post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .body(String.class);

            if (responseJson != null) {
                JsonNode root = objectMapper.readTree(responseJson);
                JsonNode candidates = root.path("candidates");
                if (candidates.isArray() && !candidates.isEmpty()) {
                    JsonNode textNode = candidates.get(0).path("content").path("parts").get(0).path("text");
                    if (!textNode.isMissingNode()) {
                        return textNode.asText();
                    }
                }
            }

            return "Welcome to Royal Stay! We are happy to assist with any room reservations or room dining orders.";
        } catch (Exception e) {
            logger.error("Error calling Google Gemini REST API", e);
            return "I apologize, but I am having temporary trouble reaching our AI concierge service right now. Please browse our Rooms and Dining menus directly or contact the front desk.";
        }
    }

    private String buildRoomContext(String userMessage) {
        String msg = userMessage.toLowerCase();
        List<Room> rooms = roomRepository.findByIsAvailableTrue();

        if (msg.contains("cheap") || msg.contains("budget") || msg.contains("low price") || msg.contains("affordable")) {
            rooms = rooms.stream()
                    .sorted((r1, r2) -> r1.getPricePerNight().compareTo(r2.getPricePerNight()))
                    .limit(3)
                    .collect(Collectors.toList());
        } else if (msg.contains("luxury") || msg.contains("suite") || msg.contains("presidential") || msg.contains("premium")) {
            rooms = rooms.stream()
                    .filter(r -> r.getRoomType() == RoomType.SUITE || r.getRoomType() == RoomType.PRESIDENTIAL || r.getRoomType() == RoomType.DELUXE)
                    .collect(Collectors.toList());
        } else if (msg.contains("family") || msg.contains("4 people") || msg.contains("four")) {
            rooms = rooms.stream()
                    .filter(r -> r.getCapacity() >= 4 || r.getRoomType() == RoomType.FAMILY)
                    .collect(Collectors.toList());
        }

        if (rooms.isEmpty()) {
            return "No matching rooms currently available.";
        }

        StringBuilder sb = new StringBuilder();
        for (Room r : rooms) {
            sb.append(String.format("Room %s | Type: %s | Price: $%s/night | Capacity: %d guests | %s\n",
                    r.getRoomNumber(), r.getRoomType(), r.getPricePerNight(), r.getCapacity(), r.getDescription()));
        }
        return sb.toString();
    }

    private String buildFoodContext(String userMessage) {
        String msg = userMessage.toLowerCase();
        List<FoodItem> items = foodItemRepository.findByAvailableTrue();

        if (msg.contains("veg") && !msg.contains("non-veg")) {
            items = items.stream().filter(FoodItem::getIsVegetarian).collect(Collectors.toList());
        } else if (msg.contains("biryani")) {
            items = items.stream().filter(i -> i.getName().toLowerCase().contains("biryani")).collect(Collectors.toList());
        } else if (msg.contains("dessert") || msg.contains("sweet")) {
            items = items.stream().filter(i -> i.getCategory().name().equalsIgnoreCase("DESSERT")).collect(Collectors.toList());
        }

        if (items.isEmpty()) {
            return "No matching dishes found on current menu.";
        }

        StringBuilder sb = new StringBuilder();
        for (FoodItem item : items) {
            sb.append(String.format("- %s ($%s, %s): %s\n",
                    item.getName(), item.getPrice(), item.getCategory(), item.getDescription()));
        }
        return sb.toString();
    }

    private String buildBookingContext(User user) {
        if (user == null) {
            return "Guest is browsing as a visitor.";
        }

        List<Booking> bookings = bookingRepository.findByUserAndStatusNotOrderByCreatedAtDesc(user, BookingStatus.CANCELLED);
        if (bookings.isEmpty()) {
            return "Guest has no active hotel bookings currently.";
        }

        StringBuilder sb = new StringBuilder();
        for (Booking b : bookings) {
            sb.append(String.format("Reservation #%d: Room %s (%s), Dates: %s to %s, Status: %s\n",
                    b.getId(), b.getRoom().getRoomNumber(), b.getRoom().getRoomType(),
                    b.getCheckInDate(), b.getCheckOutDate(), b.getStatus()));
        }
        return sb.toString();
    }
}

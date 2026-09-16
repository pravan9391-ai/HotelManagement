package com.hotel.management.config;

import com.hotel.management.food.FoodCategory;
import com.hotel.management.food.FoodItem;
import com.hotel.management.food.FoodItemRepository;
import com.hotel.management.room.Room;
import com.hotel.management.room.RoomRepository;
import com.hotel.management.room.RoomType;
import com.hotel.management.user.Role;
import com.hotel.management.user.User;
import com.hotel.management.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final FoodItemRepository foodItemRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           RoomRepository roomRepository,
                           FoodItemRepository foodItemRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.foodItemRepository = foodItemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        initializeUsers();
        initializeRooms();
        initializeFoodItems();
    }

    private void initializeUsers() {
        // Admin User
        userRepository.findByEmail("admin@royalstay.com").ifPresentOrElse(
                admin -> {
                    admin.setPassword(passwordEncoder.encode("Admin@123"));
                    userRepository.save(admin);
                },
                () -> {
                    User admin = new User(
                            "admin",
                            "admin@royalstay.com",
                            passwordEncoder.encode("Admin@123"),
                            "+1-555-0100",
                            Role.ROLE_ADMIN
                    );
                    userRepository.save(admin);
                    logger.info("Initialized default admin user: admin@royalstay.com");
                }
        );

        // Customer User
        userRepository.findByEmail("john.doe@example.com").ifPresentOrElse(
                customer -> {
                    customer.setPassword(passwordEncoder.encode("Customer@123"));
                    userRepository.save(customer);
                },
                () -> {
                    User customer = new User(
                            "johndoe",
                            "john.doe@example.com",
                            passwordEncoder.encode("Customer@123"),
                            "+1-555-0101",
                            Role.ROLE_CUSTOMER
                    );
                    userRepository.save(customer);
                    logger.info("Initialized default customer user: john.doe@example.com");
                }
        );
    }

    private void initializeRooms() {
        if (roomRepository.count() == 0) {
            roomRepository.save(new Room("101", RoomType.SINGLE, new BigDecimal("99.00"), 1, "Cozy single room with a queen bed, high-speed WiFi, smart workspace, and premium bath amenities.", true, "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=1200&h=800&fit=crop&q=80"));
            roomRepository.save(new Room("102", RoomType.SINGLE, new BigDecimal("110.00"), 1, "Deluxe single room featuring a garden view, rainfall shower, and artisan espresso machine.", true, "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=1200&h=800&fit=crop&q=80"));
            roomRepository.save(new Room("201", RoomType.DOUBLE, new BigDecimal("160.00"), 2, "Spacious double room with king-size bedding, plush lounge chairs, and city skyline view.", true, "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=1200&h=800&fit=crop&q=80"));
            roomRepository.save(new Room("202", RoomType.DOUBLE, new BigDecimal("175.00"), 2, "Executive double room with panoramic balcony, marble bathroom, and evening turndown service.", true, "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=1200&h=800&fit=crop&q=80"));
            roomRepository.save(new Room("301", RoomType.SUITE, new BigDecimal("280.00"), 3, "Opulent master suite with separate living parlor, walk-in dressing room, and soaking tub.", true, "https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=1200&h=800&fit=crop&q=80"));
            roomRepository.save(new Room("302", RoomType.DELUXE, new BigDecimal("220.00"), 2, "Deluxe corner suite with floor-to-ceiling windows, soundproof design, and private bar.", true, "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=1200&h=800&fit=crop&q=80"));
            roomRepository.save(new Room("401", RoomType.FAMILY, new BigDecimal("260.00"), 4, "Family suite featuring interconnecting bedrooms, entertainment center, and children amenities.", true, "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=1200&h=800&fit=crop&q=80"));
            roomRepository.save(new Room("501", RoomType.PRESIDENTIAL, new BigDecimal("550.00"), 4, "Presidential Penthouse offering 360-degree views, private butler service, jacuzzi, and dining salon.", true, "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=1200&h=800&fit=crop&q=80"));
            logger.info("Initialized default rooms in catalog");
        }
    }

    private void initializeFoodItems() {
        if (foodItemRepository.count() == 0) {
            foodItemRepository.save(new FoodItem("Crispy Paneer Tikka", "Tandoor-roasted cottage cheese marinated in aromatic spices and hung curd, served with mint chutney.", FoodCategory.STARTER, new BigDecimal("14.50"), "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=800&fit=crop&q=80", true, 20, true, false));
            foodItemRepository.save(new FoodItem("Tandoori Chicken Skewers", "Tender chicken marinated in spiced yoghurt and chargrilled to perfection.", FoodCategory.STARTER, new BigDecimal("16.00"), "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=800&fit=crop&q=80", true, 20, false, false));
            foodItemRepository.save(new FoodItem("Paneer Butter Masala", "Fresh cottage cheese cubes simmered in a velvety, rich tomato and cashew butter gravy.", FoodCategory.VEG_MAIN, new BigDecimal("18.00"), "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=800&fit=crop&q=80", true, 25, true, false));
            foodItemRepository.save(new FoodItem("Dal Makhani", "Slow-cooked black lentils simmered overnight with cream, butter, and mild spices.", FoodCategory.VEG_MAIN, new BigDecimal("15.00"), "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800&fit=crop&q=80", true, 20, true, false));
            foodItemRepository.save(new FoodItem("Royal Butter Chicken", "Classic chicken tikka cooked in a mild, silken tomato, butter, and cream reduction.", FoodCategory.NON_VEG_MAIN, new BigDecimal("21.00"), "https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?w=800&fit=crop&q=80", true, 25, false, false));
            foodItemRepository.save(new FoodItem("Mutton Rogan Josh", "Slow-braised lamb in Kashmiri aromatic gravy with notes of fennel and dry ginger.", FoodCategory.NON_VEG_MAIN, new BigDecimal("24.50"), "https://images.unsplash.com/photo-1545247181-516773cae754?w=800&fit=crop&q=80", true, 30, false, false));
            foodItemRepository.save(new FoodItem("Butter Garlic Naan", "Clay-oven baked leavened flatbread brushed with crushed garlic and melted herb butter.", FoodCategory.BREAD, new BigDecimal("4.50"), "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=800&fit=crop&q=80", true, 10, true, false));
            foodItemRepository.save(new FoodItem("Royal Hyderabadi Dum Biryani", "Aged Basmati rice layered with spiced tender chicken, saffron, and fried shallots.", FoodCategory.RICE, new BigDecimal("22.00"), "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=800&fit=crop&q=80", true, 30, false, false));
            foodItemRepository.save(new FoodItem("Warm Gulab Jamun", "Golden milk dumplings infused with cardamom syrup, served with crushed pistachios.", FoodCategory.DESSERT, new BigDecimal("8.50"), "https://images.unsplash.com/photo-1589119908995-c6837fa14d48?w=800&fit=crop&q=80", true, 10, true, false));
            foodItemRepository.save(new FoodItem("Mango Kulfi", "Traditional slow-churned Indian ice cream infused with Alphonso mango pulp and saffron.", FoodCategory.DESSERT, new BigDecimal("9.00"), "https://images.unsplash.com/photo-1579954115545-a95591f28bfc?w=800&fit=crop&q=80", true, 10, true, false));
            foodItemRepository.save(new FoodItem("Signature Masala Chai", "Freshly brewed black tea simmered with whole spices, ginger, and organic milk.", FoodCategory.BEVERAGE, new BigDecimal("4.00"), "https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=800&fit=crop&q=80", true, 10, true, false));
            foodItemRepository.save(new FoodItem("Fresh Sweet Lime & Mint Cooler", "Chilled citrus cooler with crushed garden mint leaves and black rock salt.", FoodCategory.BEVERAGE, new BigDecimal("6.00"), "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=800&fit=crop&q=80", true, 10, true, true));
            logger.info("Initialized default dining menu in catalog");
        }
    }
}

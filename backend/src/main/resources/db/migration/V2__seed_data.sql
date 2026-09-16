-- V2__seed_data.sql
-- Seed default admin, customer, rooms, and food items

-- Users (BCrypt hash for 'Admin@123' and 'Customer@123')
INSERT INTO users (id, username, email, password, phone_number, role, created_at, updated_at) VALUES
(1, 'admin', 'admin@royalstay.com', '$2a$10$640g8uXh3y6y.o0n4V0fMe3aZ6/kYVd/R2M.wH1p7y5W3p5C9hW2S', '+1-555-0100', 'ROLE_ADMIN', NOW(), NOW()),
(2, 'johndoe', 'john.doe@example.com', '$2a$10$640g8uXh3y6y.o0n4V0fMe3aZ6/kYVd/R2M.wH1p7y5W3p5C9hW2S', '+1-555-0101', 'ROLE_CUSTOMER', NOW(), NOW());

-- Rooms
INSERT INTO rooms (id, room_number, room_type, price_per_night, capacity, description, is_available, image_url, created_at) VALUES
(1, '101', 'SINGLE', 99.00, 1, 'Cozy single room with a queen bed, high-speed WiFi, smart workspace, and premium bath amenities.', TRUE, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=1200&h=800&fit=crop&q=80', NOW()),
(2, '102', 'SINGLE', 110.00, 1, 'Deluxe single room featuring a garden view, rainfall shower, and artisan espresso machine.', TRUE, 'https://images.unsplash.com/photo-1582719508461-905c673771fd?w=1200&h=800&fit=crop&q=80', NOW()),
(3, '201', 'DOUBLE', 160.00, 2, 'Spacious double room with king-size bedding, plush lounge chairs, and city skyline view.', TRUE, 'https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=1200&h=800&fit=crop&q=80', NOW()),
(4, '202', 'DOUBLE', 175.00, 2, 'Executive double room with panoramic balcony, marble bathroom, and evening turndown service.', TRUE, 'https://images.unsplash.com/photo-1590490360182-c33d57733427?w=1200&h=800&fit=crop&q=80', NOW()),
(5, '301', 'SUITE', 280.00, 3, 'Opulent master suite with separate living parlor, walk-in dressing room, and soaking tub.', TRUE, 'https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=1200&h=800&fit=crop&q=80', NOW()),
(6, '302', 'DELUXE', 220.00, 2, 'Deluxe corner suite with floor-to-ceiling windows, soundproof design, and private bar.', TRUE, 'https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=1200&h=800&fit=crop&q=80', NOW()),
(7, '401', 'FAMILY', 260.00, 4, 'Family suite featuring interconnecting bedrooms, entertainment center, and children amenities.', TRUE, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=1200&h=800&fit=crop&q=80', NOW()),
(8, '501', 'PRESIDENTIAL', 550.00, 4, 'Presidential Penthouse offering 360-degree views, private butler service, jacuzzi, and dining salon.', TRUE, 'https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=1200&h=800&fit=crop&q=80', NOW());

-- Food Items
INSERT INTO food_items (id, name, description, category, price, image_url, available, preparation_time, is_vegetarian, is_vegan, created_at, updated_at) VALUES
(1, 'Crispy Paneer Tikka', 'Tandoor-roasted cottage cheese marinated in aromatic spices and hung curd, served with mint chutney.', 'STARTER', 14.50, 'https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=800&fit=crop&q=80', TRUE, 20, TRUE, FALSE, NOW(), NOW()),
(2, 'Tandoori Chicken Skewers', 'Tender chicken marinated in spiced yoghurt and chargrilled to perfection.', 'STARTER', 16.00, 'https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=800&fit=crop&q=80', TRUE, 20, FALSE, FALSE, NOW(), NOW()),
(3, 'Paneer Butter Masala', 'Fresh cottage cheese cubes simmered in a velvety, rich tomato and cashew butter gravy.', 'VEG_MAIN', 18.00, 'https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=800&fit=crop&q=80', TRUE, 25, TRUE, FALSE, NOW(), NOW()),
(4, 'Dal Makhani', 'Slow-cooked black lentils simmered overnight with cream, butter, and mild spices.', 'VEG_MAIN', 15.00, 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800&fit=crop&q=80', TRUE, 20, TRUE, FALSE, NOW(), NOW()),
(5, 'Royal Butter Chicken', 'Classic chicken tikka cooked in a mild, silken tomato, butter, and cream reduction.', 'NON_VEG_MAIN', 21.00, 'https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?w=800&fit=crop&q=80', TRUE, 25, FALSE, FALSE, NOW(), NOW()),
(6, 'Mutton Rogan Josh', 'Slow-braised lamb in Kashmiri aromatic gravy with notes of fennel and dry ginger.', 'NON_VEG_MAIN', 24.50, 'https://images.unsplash.com/photo-1545247181-516773cae754?w=800&fit=crop&q=80', TRUE, 30, FALSE, FALSE, NOW(), NOW()),
(7, 'Butter Garlic Naan', 'Clay-oven baked leavened flatbread brushed with crushed garlic and melted herb butter.', 'BREAD', 4.50, 'https://images.unsplash.com/photo-1601050690597-df0568f70950?w=800&fit=crop&q=80', TRUE, 10, TRUE, FALSE, NOW(), NOW()),
(8, 'Royal Hyderabadi Dum Biryani', 'Aged Basmati rice layered with spiced tender chicken, saffron, and fried shallots.', 'RICE', 22.00, 'https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=800&fit=crop&q=80', TRUE, 30, FALSE, FALSE, NOW(), NOW()),
(9, 'Warm Gulab Jamun', 'Golden milk dumplings infused with cardamom syrup, served with crushed pistachios.', 'DESSERT', 8.50, 'https://images.unsplash.com/photo-1589119908995-c6837fa14d48?w=800&fit=crop&q=80', TRUE, 10, TRUE, FALSE, NOW(), NOW()),
(10, 'Mango Kulfi', 'Traditional slow-churned Indian ice cream infused with Alphonso mango pulp and saffron.', 'DESSERT', 9.00, 'https://images.unsplash.com/photo-1579954115545-a95591f28bfc?w=800&fit=crop&q=80', TRUE, 10, TRUE, FALSE, NOW(), NOW()),
(11, 'Signature Masala Chai', 'Freshly brewed black tea simmered with whole spices, ginger, and organic milk.', 'BEVERAGE', 4.00, 'https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=800&fit=crop&q=80', TRUE, 10, TRUE, FALSE, NOW(), NOW()),
(12, 'Fresh Sweet Lime & Mint Cooler', 'Chilled citrus cooler with crushed garden mint leaves and black rock salt.', 'BEVERAGE', 6.00, 'https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=800&fit=crop&q=80', TRUE, 10, TRUE, TRUE, NOW(), NOW());

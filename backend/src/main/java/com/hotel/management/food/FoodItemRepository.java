package com.hotel.management.food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    List<FoodItem> findByAvailableTrue();
    List<FoodItem> findByCategoryAndAvailableTrue(FoodCategory category);
    List<FoodItem> findByCategory(FoodCategory category);
}

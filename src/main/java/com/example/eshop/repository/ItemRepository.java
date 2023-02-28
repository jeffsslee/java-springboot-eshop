package com.example.eshop.repository;

import com.example.eshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

// When extending to JpaRepository, no need to explicit '@Repository' annotation
public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

  List<Item> findByItemName(String itemName);
  List<Item> findByPriceLessThan(BigDecimal price);
  List<Item> findByPriceLessThanOrderByPriceDesc(BigDecimal price);

  @Query("select i from Item i " +
      "where i.itemDetail like %:itemDetail% " +
      "order by i.price desc")
  List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
  @Query(value = "select * from item i " +
      "where i.item_detail like %:itemDetail% " +
      "order by i.price desc", nativeQuery = true)
  List<Item> findByItemDetailWithNativeQuery(@Param("itemDetail") String itemDetail);

}

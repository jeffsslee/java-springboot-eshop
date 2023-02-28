package com.example.eshop.repository;

import com.example.eshop.constant.ItemSellStatus;
import com.example.eshop.entity.Item;
import com.example.eshop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("local-h2")
class ItemRepositoryTest {

  @PersistenceContext
  EntityManager em;

  @Autowired
  ItemRepository itemRepository;

  @Test
  @DisplayName("To save test for item")
  public void createItemTest(){
    Item item = Item.builder()
        .itemName("Shirts1")
        .price(new BigDecimal("20.09"))
        .itemDetail("Shirts for spring 2023")
        .itemSellStatus(ItemSellStatus.FOR_SALE)
        .stockNumber(50)
        .regTime(LocalDateTime.now())
        .updateTime(LocalDateTime.now())
        .build()
        ;

    Item savedItem = itemRepository.save(item);
    System.out.println(savedItem.toString());
  }

  public void createItemList(){
    for (int i = 0; i < 20; i++) {
      Item item = Item.builder()
          .itemName("Shirts " + i)
          .price(new BigDecimal("20.09").add(new BigDecimal(i)))
          .itemDetail("Shirts for spring 2023 with ID : " + i)
          .itemSellStatus(ItemSellStatus.FOR_SALE)
          .stockNumber(50)
          .regTime(LocalDateTime.now())
          .updateTime(LocalDateTime.now())
          .build()
          ;
      itemRepository.save(item);
    }
  }

  public void createItemList2(){
    for (int i = 0; i < 20; i++) {
      Item item = Item.builder()
          .itemName("Shirts " + i)
          .price(new BigDecimal("20.09").add(new BigDecimal(i)))
          .itemDetail("Shirts for spring 2023 with ID : " + i)
          .itemSellStatus(ItemSellStatus.FOR_SALE)
          .stockNumber(50)
          .regTime(LocalDateTime.now())
          .updateTime(LocalDateTime.now())
          .build()
          ;
      itemRepository.save(item);
    }
    for (int i = 20; i < 40; i++) {
      Item item = Item.builder()
          .itemName("Shirts " + i)
          .price(new BigDecimal("20.09").add(new BigDecimal(i)))
          .itemDetail("Shirts for spring 2023 with ID : " + i)
          .itemSellStatus(ItemSellStatus.SOLD_OUT)
          .stockNumber(50)
          .regTime(LocalDateTime.now())
          .updateTime(LocalDateTime.now())
          .build()
          ;
      itemRepository.save(item);
    }
  }

  @Test
  @DisplayName("To find item by itemName Test")
  public void findByItemNameTest(){
    this.createItemList();
    List<Item> itemList = itemRepository.findByItemName("Shirts 7");
    for (Item item : itemList) {
      System.out.println(item.toString());
    }
  }

  @Test
  @DisplayName("To find item by price Test")
  public void findByPriceLessThenTest(){
    this.createItemList();
    List<Item> itemList = itemRepository.findByPriceLessThan(new BigDecimal("30.09"));
    for (Item item : itemList) {
      System.out.println(item.toString());
    }
  }
  @Test
  @DisplayName("To find item by price order byTest")
  public void findByPriceLessThenOrderByTest(){
    this.createItemList();
    List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(new BigDecimal("30.09"));
    for (Item item : itemList) {
      System.out.println(item.toString());
    }
  }
  @Test
  @DisplayName("To find item by itemDetail Test")
  public void findByItemDetail(){
    this.createItemList();
    List<Item> itemList = itemRepository.findByItemDetail("7");
    for (Item item : itemList) {
      System.out.println(item.toString());
    }
  }
  @Test
  @DisplayName("To find item by itemDetail With Native Query Test")
  public void findByItemDetailWithNativeQuery(){
    this.createItemList();
    List<Item> itemList = itemRepository.findByItemDetailWithNativeQuery("8");
    for (Item item : itemList) {
      System.out.println(item.toString());
    }
  }

  @Test
  @DisplayName("QueryDsl Tset")
  public void queryDslTest(){
    this.createItemList();
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QItem qItem = QItem.item;
    JPAQuery<Item> query = queryFactory
        .selectFrom(qItem)
        .where(qItem.itemSellStatus.eq(ItemSellStatus.FOR_SALE))
        .where(qItem.itemDetail.like("%" + "8" + "%"))
        .orderBy(qItem.price.desc());

    List<Item> itemList = query.fetch();

    for (Item item : itemList) {
      System.out.println(item.toString());
    }
  }

  @Test
  @DisplayName("Querydsl test 2")
  public void queryDslTest2(){
    this.createItemList2();

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    QItem item = QItem.item;
    String itemDetail = "Shirts for spring 2023 with ID";
    BigDecimal price = new BigDecimal("32.09");
    String itemSellStat = "FOR_SALE";

    booleanBuilder.and(item.itemDetail.like("%"+ itemDetail +"%"));
    booleanBuilder.and(item.price.gt(price));

    if(StringUtils.equals(itemSellStat, ItemSellStatus.FOR_SALE)){
      booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.FOR_SALE));
    }

    Pageable pageable = PageRequest.of(0, 5);
    Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);

    System.out.println("Total elements : " + itemPagingResult.getTotalElements());
    List<Item> itemList = itemPagingResult.getContent();
    for (Item item1 : itemList) {
      System.out.println(item1.toString());
    }


  }


}
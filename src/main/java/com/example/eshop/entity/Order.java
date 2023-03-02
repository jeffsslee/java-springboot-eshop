package com.example.eshop.entity;

import com.example.eshop.constant.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Orders vs Member
 * A member may have many orders : One(Member) vs Many(Orders)
 */
@Entity
@Table(name = "orders") // table name : orders >> cause 'order' is a reserved key word in SQL
@Getter @Setter @ToString(exclude = {"member", "orderItems"})
@NoArgsConstructor @AllArgsConstructor @Builder
public class Order  extends BaseEntity{

  @Id @GeneratedValue
  @Column(name = "order_id")
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;
  private LocalDateTime orderDate;
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;
//  private LocalDateTime regTime;
//  private LocalDateTime updateTime;

  // following field is a "bidirectional relationship" with OrderItem
  @OneToMany(
      mappedBy = "order"  // mappedBy = "order" >> 대상 entity "멤버필드명" 그대로....
      , cascade = CascadeType.ALL
      , orphanRemoval = true
      , fetch = FetchType.LAZY
  )
  private List<OrderItem> orderItemList = new ArrayList<>();

}

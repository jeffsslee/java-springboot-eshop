package com.example.eshop.entity;

import lombok.*;

import javax.persistence.*;

/**
 * CartItem 클래스
 *
 * CartItem은 중계 테이블~~: 중계 테이블 자체는 many의 역활
 * 여러개의 CartItem 들을 하나의 Cart 에 담을 수 ~~ : CartItem vs Cart = Many to One
 * 여러개의 CartItem 들은 각각 하나의 Item 은 과 연결 ~~ : CartItem vs Item = Many to One
 *
 * cartItemId, Cart(id), Item(id), count
 */

@Entity
@Table(name = "cart_item")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CartItem  extends BaseEntity{

  @Id @GeneratedValue
  @Column(name = "cart_item_id")
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id")
  private Cart cart;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;
  private int count;
}

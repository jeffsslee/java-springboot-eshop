package com.example.eshop.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Cart 클래스
 *
 * Member 별로 오직 하나의 카트만 생성
 * 아이템을 카트에 담을때 최초로 카트를 생성
 * 생성된 카트는 추후 재 사용 *
 *
 */
@Entity
@Table(name = "cart")
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor @Builder
public class Cart  extends BaseEntity{

  @Id @GeneratedValue
  @Column(name = "cart_id")
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

}

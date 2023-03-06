package com.example.eshop.entity;

import lombok.*;

import javax.persistence.*;

/**
 * ItemImg.java
 * item_img_id, item_id, ori_img_name, img_name, img_url, ref_img_yn
 */

@Entity
@Table(name = "item_img")
@Getter @Setter @ToString(exclude = {"item"})
@NoArgsConstructor @AllArgsConstructor @Builder
public class ItemImg extends BaseEntity{

  @Id @GeneratedValue
  @Column(name = "item_img_id")
  private Long id;
  private String imgName;
  private String oriImgName;
  private String imgUrl;
  private String refImgYn;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;

  public void updateItemImg(String imgName, String oriImgName, String imgUrl) {
    this.imgName = imgName;
    this.oriImgName = oriImgName;
    this.imgUrl = imgUrl;
  }
}

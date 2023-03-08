package com.example.eshop.repository;

import com.example.eshop.dto.CartDetailDto;
import com.example.eshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  CartItem findByCartIdAndItemId(Long cartId, Long itemId);

  // 일반 클래스로 데이터 축출시
  // 연관관계 맵핑으로 추가적인 쿼리문이 실행될 수 있음
  // 이때 DTO의 생성자를 이용하면 성능최적화를 할 수 있음

  // from entity, entity.... (여기서는 2개의 entity에서 축출)
  // join ci. item i ... ci(CartItem)에 있는 item 과 join
  // JPQL 파라미터 대상 형태 ( = :cartId)
  // 파라미터 사용시 @Param 어노테이션 사용
  @Query("select new com.example.eshop.dto.CartDetailDto(ci.id, i.itemName, i.price, ci.count, im.imgUrl) " +
      "from CartItem ci, ItemImg im " +
      "join ci.item i " +
      "where ci.cart.id = :cartId " +
      "and im.item.id = ci.item.id " +
      "and im.repImgYn = 'Y' " +
      "order by ci.regTime desc")
  List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);
}

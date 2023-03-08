package com.example.eshop.service;

import com.example.eshop.dto.OrderItemDto;
import com.example.eshop.dto.OrderDto;
import com.example.eshop.dto.OrderHistoryDto;
import com.example.eshop.entity.*;
import com.example.eshop.repository.ItemImgRepository;
import com.example.eshop.repository.ItemRepository;
import com.example.eshop.repository.MemberRepository;
import com.example.eshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

  private final ItemRepository itemRepository;
  private final MemberRepository memberRepository;
  private final OrderRepository orderRepository;
  private final ItemImgRepository itemImgRepository;

  public Long order(OrderDto orderDto, String email){
    Item item = itemRepository.findById(orderDto.getItemId())
        .orElseThrow(EntityNotFoundException::new);
    Member member = memberRepository.findByEmail(email);
    List<OrderItem> orderItemList = new ArrayList<>();

    OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
    orderItemList.add(orderItem);

    Order order = Order.createOrder(member, orderItemList);
    orderRepository.save(order);

    return order.getId();
  }

  @Transactional(readOnly = true)
  public Page<OrderHistoryDto> getOrderList(String email, Pageable pageable){
    List<Order> orderList = orderRepository.findOrders(email, pageable);
    Long totalCount = orderRepository.countOrder(email);
    List<OrderHistoryDto> orderHistoryDtoList = new ArrayList<>();
    for (Order order : orderList) {
      OrderHistoryDto orderHistoryDto = new OrderHistoryDto(order);
      List<OrderItem> orderItemList = order.getOrderItemList();
      for (OrderItem orderItem : orderItemList) {
        ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");
        OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
        orderHistoryDto.addOrderItemDto(orderItemDto);
      }
      orderHistoryDtoList.add(orderHistoryDto);
    }
    return new PageImpl<OrderHistoryDto>(orderHistoryDtoList, pageable, totalCount);
  }

  @Transactional(readOnly = true)
  public boolean validateOrder(Long orderId, String email){
    Member member = memberRepository.findByEmail(email);
    Order order = orderRepository.findById(orderId)
        .orElseThrow(EntityNotFoundException::new);
    Member savedMember = order.getMember();
    if(!StringUtils.equals(member.getEmail(), savedMember.getEmail())){
      return false;
    }
    return true;
  }

  public void cancelOrder(Long orderId){
    Order order = orderRepository.findById(orderId)
        .orElseThrow(EntityNotFoundException::new);
    order.cancelOrder();
  }

  public Long orders(List<OrderDto> orderDtoList, String email){
    Member member = memberRepository.findByEmail(email);
    List<OrderItem> orderItemList = new ArrayList<>();

    for (OrderDto orderDto : orderDtoList) {
      Item item = itemRepository.findById(orderDto.getItemId())
          .orElseThrow(EntityNotFoundException::new);

      OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
      orderItemList.add(orderItem);
    }

    Order order = Order.createOrder(member, orderItemList);
    orderRepository.save(order);

    return order.getId();
  }

}

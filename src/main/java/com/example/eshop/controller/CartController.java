package com.example.eshop.controller;

import com.example.eshop.dto.CartDetailDto;
import com.example.eshop.dto.CartItemDto;
import com.example.eshop.dto.CartOrderDto;
import com.example.eshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping("/cart")
  public @ResponseBody ResponseEntity order(
      @RequestBody @Valid CartItemDto cartItemDto
      , BindingResult bindingResult
      , Principal principal){

    if(bindingResult.hasErrors()){
      StringBuilder sb = new StringBuilder();
      List<FieldError> fieldErrors = bindingResult.getFieldErrors();
      for (FieldError fieldError : fieldErrors) {
        sb.append(fieldError.getDefaultMessage());
      }
      return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
    }

    String email = principal.getName();
    Long cartItemId;

    try {
      cartItemId = cartService.addCart(cartItemDto, email);
    } catch (Exception e) {
      return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);

  }

  @GetMapping("/cart")
  public String cartHistory(Principal principal, Model model){
    List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal.getName());
    model.addAttribute("cartItems", cartDetailDtoList);
    return "cart/cartList";
  }

  @PatchMapping("/cartItem/{cartItemId}")
  public @ResponseBody ResponseEntity updateCartItem(
      @PathVariable("cartItemId") Long cartItemId
      , int count
      , Principal principal){
    // count 값을 url 의 get 방식으로 전송하고, 스프링 컨트롤러에서 매개 변수로 받았음

    if(count <=0){
      return new ResponseEntity<String>("Min order quantity is at least 1", HttpStatus.BAD_REQUEST);
    }else if(!cartService.validateCartItem(cartItemId, principal.getName())){
      return new ResponseEntity<String>("No right to change!", HttpStatus.FORBIDDEN);
    }

    cartService.updateCartItemCount(cartItemId, count);
    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);

  }

  @DeleteMapping("/cartItem/{cartItemId}")
  public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){
    if(!cartService.validateCartItem(cartItemId, principal.getName())){
      return new ResponseEntity<String>("No right to delete!", HttpStatus.FORBIDDEN);
    }
    cartService.deleteCartItem(cartItemId);
    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
  }

  @PostMapping("/cart/orders")
  public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){
    List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

    if(cartOrderDtoList == null || cartOrderDtoList.size() == 0){
      return new ResponseEntity<String>("Input order item(s)!", HttpStatus.BAD_REQUEST);
    }

    for (CartOrderDto orderDto : cartOrderDtoList) {
      if (!cartService.validateCartItem(orderDto.getCartItemId(), principal.getName())) {
        return new ResponseEntity<String>("No right to order!", HttpStatus.FORBIDDEN);
      }
    }
      Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
      return new ResponseEntity<Long>(orderId, HttpStatus.OK);

  }

}

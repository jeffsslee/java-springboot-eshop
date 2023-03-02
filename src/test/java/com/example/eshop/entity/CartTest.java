package com.example.eshop.entity;

import com.example.eshop.dto.MemberFormDto;
import com.example.eshop.repository.CartRepository;
import com.example.eshop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@ActiveProfiles("local-h2")
class CartTest {

  @Autowired
  CartRepository cartRepository;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  PasswordEncoder passwordEncoder;
  @PersistenceContext
  EntityManager em;

  public Member createMember(){
    MemberFormDto dto = new MemberFormDto();
    dto.setEmail("test@mail.com");
    dto.setName("John");
    dto.setAddress("100 Mount Waverley VIC");
    dto.setPassword("1111");
    return Member.createMember(dto, passwordEncoder);
  }

  @Test
  @DisplayName("To test mapping cart with member")
  public void findCartAndMemberTest(){
    // Given
    Member member = this.createMember();
    memberRepository.save(member);

    Cart cart = new Cart();
    cart.setMember(member);
    cartRepository.save(cart);

    // When
    em.flush();
    em.clear();

    Cart savedCart = cartRepository.findById(cart.getId())
        .orElseThrow(EntityNotFoundException::new);

    // Then
    System.out.println("Result ##################");

    assertEquals(savedCart.getMember().getId(), member.getId());
    assertEquals(savedCart.getMember().getAddress(), member.getAddress());
    System.out.println("Member ID : " + member.getId());
    System.out.println("Member Address : " + member.getAddress());
  }
}
package com.example.eshop.service;

import com.example.eshop.dto.MemberFormDto;
import com.example.eshop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("local-h2")
class MemberServiceTest {

  @Autowired
  MemberService memberService;

  @Autowired
  PasswordEncoder passwordEncoder;

  public Member createMember(){
    MemberFormDto dto = new MemberFormDto();
    dto.setEmail("newMember@mail.com");
    dto.setName("newBie");
    dto.setAddress("100 Mount Waverley VIC");
    dto.setPassword("1111");
    return Member.createMember(dto, passwordEncoder);
  }

  @Test
  @DisplayName("Test to save a member")
  public void saveMemberTest(){
    Member member = this.createMember();
    Member savedMember = memberService.saveMember(member);

    System.out.println("Result #################");
    assertEquals(member.getEmail(), savedMember.getEmail());
    assertEquals(member.getName(), savedMember.getName());
    assertEquals(member.getAddress(), savedMember.getAddress());
    assertEquals(member.getRole(), savedMember.getRole());
  }
  @Test
  @DisplayName("Test to save a duplicated member")
  public void saveDuplicatedMemberTest(){

    // Given
    Member member1 = this.createMember();
    Member member2 = this.createMember();
    Member savedMember = memberService.saveMember(member1);

    // When
    Throwable e =  assertThrows(IllegalStateException.class, () -> {
      memberService.saveMember(member2);
    });

    // Then
    System.out.println("Result #################");
    assertEquals("The member had been already registered!", e.getMessage());
  }

}
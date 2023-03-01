package com.example.eshop.controller;

import com.example.eshop.dto.MemberFormDto;
import com.example.eshop.entity.Member;
import com.example.eshop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@Transactional
@ActiveProfiles("local-h2")
@AutoConfigureMockMvc
class MemberControllerTest {

  @Autowired
  private MemberService memberService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  PasswordEncoder passwordEncoder;

  public Member createMember(String email, String password){
    MemberFormDto dto = new MemberFormDto();
    dto.setEmail(email);
    dto.setName("John");
    dto.setAddress("100 Mount Waverley VIC");
    dto.setPassword(password);
    Member member = Member.createMember(dto, passwordEncoder);
    return memberService.saveMember(member);
  }

  @Test
  @DisplayName("Test to login with success")
  public void loginSuccessTest() throws Exception {
    String email = "test@mail.com";
    String password = "1111";
    this.createMember(email, password);

    mockMvc.perform(
        formLogin()
            .userParameter("email")
            .loginProcessingUrl("/members/login")
            .user(email)
            .password(password)
        )
        .andExpect(SecurityMockMvcResultMatchers.authenticated());
  }
  @Test
  @DisplayName("Test to login with failure")
  public void loginFailureTest() throws Exception {
    String email = "test@mail.com";
    String password = "1111";
    this.createMember(email, password);

    mockMvc.perform(
        formLogin()
            .userParameter("email")
            .loginProcessingUrl("/members/login")
            .user(email)
            .password("1234")
        )
        .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
  }
}
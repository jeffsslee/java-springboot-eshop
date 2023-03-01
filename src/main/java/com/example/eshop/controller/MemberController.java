package com.example.eshop.controller;

import com.example.eshop.dto.MemberFormDto;
import com.example.eshop.entity.Member;
import com.example.eshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/members/new")
  public String memberForm(Model model){
    model.addAttribute("memberFormDto", new MemberFormDto());
    return "member/memberForm";
  }

  @PostMapping("/members/new")
  public String memberForm(@Valid MemberFormDto dto, BindingResult bindingResult, Model model){

    if(bindingResult.hasErrors()){
      return "member/memberForm";
    }

    try {
      Member newMember = Member.createMember(dto, passwordEncoder);
      memberService.saveMember(newMember);
    } catch (Exception e) {
      model.addAttribute("errorMessage", e.getMessage());
      return "member/memberForm";
    }

    return "redirect:/members/login";
  }

  @GetMapping("/members/login")
  public String loginMember(){
    return "/member/memberLoginForm";
  }
  @GetMapping("/members/login/error")
  public String loginError(Model model){
    model.addAttribute("loginErrorMsg", "Check your ID or PW and try again!");
    return "/member/memberLoginForm";
  }
}

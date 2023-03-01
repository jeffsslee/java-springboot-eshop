package com.example.eshop.service;

import com.example.eshop.entity.Member;
import com.example.eshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;

  public Member saveMember(Member member){

    validateDuplicateMember(member);

    return memberRepository.save(member);
  }

  private void validateDuplicateMember(Member member) {
    Member dbMember = memberRepository.findByEmail(member.getEmail());
    if(dbMember != null){
      throw new IllegalStateException("The member had been already registered!");
    }
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email);
    if(member == null){
      throw new UsernameNotFoundException(email);
    }

    // 여기의 User 클래스는...
    //  org.springframework.security.core.userdetails.User 의 클래스로 UserDetails 인터페이스를 구현하고 있음
    return User.builder()
        .username(member.getEmail())
        .password(member.getPassword())
        .roles(member.getRole().toString())
        .build();
  }
}

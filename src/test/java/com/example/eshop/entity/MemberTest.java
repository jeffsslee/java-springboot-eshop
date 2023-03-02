package com.example.eshop.entity;

import com.example.eshop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("local-h2")
class MemberTest {

  @Autowired
  MemberRepository memberRepository;
  @PersistenceContext
  EntityManager em;

  @Test
  @DisplayName("Auditing test")
  @WithMockUser(username = "John", roles = "USER")
  public void auditingTest(){
    Member newMember = new Member();
    memberRepository.save(newMember);

    em.flush();
    em.clear();

    Member member = memberRepository.findById(newMember.getId())
        .orElseThrow(EntityNotFoundException::new);

    System.out.println("RegTime : " + member.getRegTime());
    System.out.println("updateTime : " + member.getUpdateTime());
    System.out.println("createdBy : " + member.getCreatedBy());
    System.out.println("modifiedBy : " + member.getModifiedBy());
  }

}
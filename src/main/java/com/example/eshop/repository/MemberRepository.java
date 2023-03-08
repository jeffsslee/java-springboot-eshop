package com.example.eshop.repository;

import com.example.eshop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findByEmail(String email);

}

package com.example.eshop.entity;

import com.example.eshop.constant.Role;
import com.example.eshop.dto.MemberFormDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor @Builder
public class Member extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "member_id")
  private Long id;
  private String name;
  @Column(unique = true)
  private String email;
  private String password;
  private String address;
  @Enumerated(EnumType.STRING)
  private Role role;

  public static Member createMember(MemberFormDto dto, PasswordEncoder passwordEncoder){
    return Member.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .address(dto.getAddress())
        .password(passwordEncoder.encode(dto.getPassword()))
        .role(Role.ADMIN)
//        .role(Role.USER)
        .build()
        ;
  }
}

package com.example.eshop.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("local-h2")
@AutoConfigureMockMvc
class ItemControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("Test to access under authorization")
  @WithMockUser(username = "admin", roles = "ADMIN")
  public void itemFormAccessSuccessTest() throws Exception {
    mockMvc.perform(
            MockMvcRequestBuilders.get("/admin/item/new")
        )
        .andDo(print()) // print() write to result code (html)
        .andExpect(status().isOk())
        ;
  }
  @Test
  @DisplayName("Test to access without authorization")
  @WithMockUser(username = "user", roles = "USER")
  public void itemFormAccessFailureTest() throws Exception {
    mockMvc.perform(
            MockMvcRequestBuilders.get("/admin/item/new")
        )
        .andDo(print()) // print() write to result code (html)
        .andExpect(status().isForbidden())
        ;
  }
}
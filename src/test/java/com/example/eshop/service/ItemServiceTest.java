package com.example.eshop.service;

import com.example.eshop.constant.ItemSellStatus;
import com.example.eshop.dto.ItemFormDto;
import com.example.eshop.entity.Item;
import com.example.eshop.entity.ItemImg;
import com.example.eshop.repository.ItemImgRepository;
import com.example.eshop.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("local-h2")
class ItemServiceTest {

  @Autowired
  ItemService itemService;
  @Autowired
  ItemRepository itemRepository;
  @Autowired
  ItemImgRepository itemImgRepository;

  List<MultipartFile> createMultipartFiles(){
    List<MultipartFile> multipartFileList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      String path = "D:/devspaces/intellij/demoProjects/eshopspace02/img";
      String imageName = "testImg" + i + ".jpg";
      MockMultipartFile mockMultipartFile =
          new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
      multipartFileList.add(mockMultipartFile);
    }
    return multipartFileList;
  }

  @Test
  @DisplayName("Test to save item with image")
  @WithMockUser(username = "admin", roles = "ADMIN")
  public void saveItem() throws Exception {
    ItemFormDto itemFormDto = new ItemFormDto();
    itemFormDto.setItemName("Test Item");
    itemFormDto.setPrice(new BigDecimal("20.09"));
    itemFormDto.setStockNumber(100);
    itemFormDto.setItemDetail("Test Item Detail");
    itemFormDto.setItemSellStatus(ItemSellStatus.FOR_SALE);

    List<MultipartFile> multipartFileList = this.createMultipartFiles();

    Long itemId = itemService.saveItem(itemFormDto, multipartFileList);
    List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
    Item savedItem = itemRepository.findById(itemId)
        .orElseThrow(EntityNotFoundException::new);

    System.out.println("Result /////////////////////////");
    Assertions.assertEquals(itemFormDto.getItemName(), savedItem.getItemName());
    Assertions.assertEquals(itemFormDto.getPrice(), savedItem.getPrice());

  }

}
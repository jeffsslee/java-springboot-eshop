package com.example.eshop.repository;

import com.example.eshop.dto.ItemSearchDto;
import com.example.eshop.dto.MainItemDto;
import com.example.eshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

  Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
  Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}

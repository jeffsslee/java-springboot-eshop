package com.example.eshop.service;

import com.example.eshop.dto.ItemFormDto;
import com.example.eshop.dto.ItemImgDto;
import com.example.eshop.dto.ItemSearchDto;
import com.example.eshop.dto.MainItemDto;
import com.example.eshop.entity.Item;
import com.example.eshop.entity.ItemImg;
import com.example.eshop.repository.ItemImgRepository;
import com.example.eshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;
  private final ItemImgService itemImgService;
  private final ItemImgRepository itemImgRepository;

  public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

    // To save item
    Item item = itemFormDto.createItem();
    itemRepository.save(item);

    // To save image
    for (int i = 0; i < itemImgFileList.size(); i++) {
      ItemImg itemImg = new ItemImg();
      itemImg.setItem(item);
      if(i == 0){
        itemImg.setRepImgYn("Y");
      }else{
        itemImg.setRepImgYn("N");
      }
      itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
    }

    return item.getId();
  }

  @Transactional(readOnly = true)
  public ItemFormDto getItemDetail(Long itemId){
    List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
    List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    for (ItemImg itemImg : itemImgList) {
      ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
      itemImgDtoList.add(itemImgDto);
    }

    Item item = itemRepository.findById(itemId)
        .orElseThrow(EntityNotFoundException::new);

    ItemFormDto itemFormDto = ItemFormDto.of(item);
    itemFormDto.setItemImgDtoList(itemImgDtoList);
    return itemFormDto;
  }

  public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
    // to update item
    Item item = itemRepository.findById(itemFormDto.getId())
        .orElseThrow(EntityNotFoundException::new);
    item.updateItem(itemFormDto);

    List<Long> itemImgIdList = itemFormDto.getItemImgIdList();

    // to update item image
    for (int i = 0; i < itemImgFileList.size(); i++) {
      itemImgService.updateItemImg(itemImgIdList.get(i), itemImgFileList.get(i));
    }

    return item.getId();
  }

  @Transactional(readOnly = true)
  public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
    return itemRepository.getAdminItemPage(itemSearchDto, pageable);
  }

  @Transactional(readOnly = true)
  public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
    return itemRepository.getMainItemPage(itemSearchDto, pageable);
  }


}

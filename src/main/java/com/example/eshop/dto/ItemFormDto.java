package com.example.eshop.dto;

import com.example.eshop.constant.ItemSellStatus;
import com.example.eshop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemFormDto {

  private Long id;
  @NotBlank(message = "Item Name is required!")
  private String itemName;
  // DTO price data type : should be reference(class) ; when object creation, the value set as 'null'
  // in entity, price data -> BigDecimal
  @NotNull(message = "Price is required!")
  private BigDecimal price;
  @NotBlank(message = "Item Detail is required!")
  private String itemDetail;
  @NotNull(message = "Stock quantity is required!")
  private Integer stockNumber;
  private ItemSellStatus itemSellStatus;
  private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
  private List<Long> itemImgIdList = new ArrayList<>();

  private static ModelMapper modelMapper = new ModelMapper();

  public Item createItem(){
    return modelMapper.map(this, Item.class);
  }

  public static ItemFormDto of(Item item){
    return modelMapper.map(item, ItemFormDto.class);
  }
}

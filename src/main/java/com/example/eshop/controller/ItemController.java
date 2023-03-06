package com.example.eshop.controller;

import com.example.eshop.dto.ItemFormDto;
import com.example.eshop.dto.ItemSearchDto;
import com.example.eshop.entity.Item;
import com.example.eshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping("/admin/item/new")
  public String adminItemForm(Model model) {
    model.addAttribute("itemFormDto", new ItemFormDto());
    return "item/itemForm";
  }

  @PostMapping("/admin/item/new")
  public String adminNewItem(
      @Valid ItemFormDto itemFormDto
      , BindingResult bindingResult
      , Model model
      , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

    if (bindingResult.hasErrors()) {
      return "item/itemForm";
    }

    if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
      model.addAttribute("errorMessage", "The 1st img file should be added!");
      return "item/itemForm";
    }

    try {
      itemService.saveItem(itemFormDto, itemImgFileList);
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Oops.... Error occurs while saving item....");
      return "item/itemForm";
    }

    return "redirect:/";    // if success... go to item list....!!
  }

  @GetMapping("/admin/item/{itemId}")
  public String adminItemDetail(@PathVariable("itemId") Long itemId, Model model) {
    try {
      ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
      model.addAttribute("itemFormDto", itemFormDto);
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Error occurs while reading item data!");
      model.addAttribute("itemFormDto", new ItemFormDto());
      return "item/itemForm";
    }
    return "item/itemForm";
  }

  @PostMapping("/admin/item/{itemId}")
  public String adminItemUpdate(
      @Valid ItemFormDto itemFormDto
      , BindingResult bindingResult
      , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList
      , Model model) {

    if (bindingResult.hasErrors()) {
      return "item/itemForm";
    }

    if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
      model.addAttribute("errorMessage", "The 1st img file should be added!");
      return "item/itemForm";
    }

    try {
      itemService.updateItem(itemFormDto, itemImgFileList);
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Error occurs while updating item data!");
      return "item/itemForm";
    }
    return "redirect:/";
  }

  @GetMapping({"/admin/items", "/admin/items/{page}"})
  public String adminItemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {

    Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
    Page<Item> itemPage = itemService.getAdminItemPage(itemSearchDto, pageable);
    model.addAttribute("items", itemPage);
    model.addAttribute("itemSearchDto", itemSearchDto);
    model.addAttribute("maxPage", 5);
    return "item/itemManage";
  }

  @GetMapping({"/item/{itemId}"})
  public String userItemDetail(@PathVariable("itemId") Long itemId, Model model) {

    ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
    model.addAttribute("item", itemFormDto);
    return "item/itemDetail";
  }

}

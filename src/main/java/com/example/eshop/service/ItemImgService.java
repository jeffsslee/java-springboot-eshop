package com.example.eshop.service;

import com.example.eshop.entity.ItemImg;
import com.example.eshop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

  @Value("${itemImgLocation}")
  private String itemImgLocation;
  // itemImgLocation: "D:/devspaces/intellij/demoProjects/eshopspace02/img"

  private final ItemImgRepository itemImgRepository;
  private final FileService fileService;

  public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {

    String oriImgName = itemImgFile.getOriginalFilename();
    String imgName = "";
    String imgUrl = "";

    // File upload
    if(!StringUtils.isEmpty(oriImgName)){
      imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
      imgUrl = "/images/img/" + imgName;
    }

    // To save ItemImg
    itemImg.updateItemImg(imgName, oriImgName, imgUrl);
    itemImgRepository.save(itemImg);
  }

  public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {

    // if img file exist ( to replace)
    if(!itemImgFile.isEmpty()){
      ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
          .orElseThrow(ExceptionInInitializerError::new);

      // 1. delete savedImg file from disk
      if(!StringUtils.isEmpty(savedItemImg.getImgName())){
        fileService.deleteFile(itemImgLocation + File.separator + savedItemImg.getImgName());
      }

      // 2. upload new img file to disk
      String oriImgName = itemImgFile.getOriginalFilename();
      String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
      String imgUrl = "/images/img/" + imgName;

      // 3. update itemImg with new one
      savedItemImg.updateItemImg(imgName, oriImgName, imgUrl);
    }
  }


}

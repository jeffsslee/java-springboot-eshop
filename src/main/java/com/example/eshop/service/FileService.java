package com.example.eshop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    /*
    Path with JAVA

    >> before Java 11
        Path path1 = Paths.get("D:\\devspaces\\intellij\\demoProjects\\jshopspace\\img\\");
        Path path2 = Paths.get("D:/devspaces/intellij/demoProjects/jshopspace/img");
        Path path3 = Paths.get(new URI("file:///D:/devspaces/intellij/demoProjects/jshopspace/img/"));
    >> From Java 11
        Path path 2 = Path.of();

*/
  public String uploadFile(String itemImgLocation, String originalFileName, byte[] fileData) throws Exception {
    UUID uuid = UUID.randomUUID();
    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
    String savedFileName = uuid.toString() + extension;
    String fileUploadFullUrl = itemImgLocation + File.separator + savedFileName;
    // itemImgLocation ..... D:\devspaces\intellij\demoProjects\eshopspace02\img
    FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
    fos.write(fileData);
    fos.close();
    return savedFileName;
  }

  public void deleteFile(String filePath) throws Exception{

    File fileToDelete = new File(filePath);

    if(fileToDelete.exists()){
      fileToDelete.delete();
      log.info("File (" + fileToDelete + ") has been deleted!");
    }else{
      log.info("File (" + fileToDelete + ") does NOT exist!");
    }
  }
}

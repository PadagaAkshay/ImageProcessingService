package com.akshay.image_processing_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file){
        try {
            String newFileName = imageService.processandsave(file);
            return ResponseEntity.ok("Its a success"+newFileName);
        } catch(Exception e){
            return ResponseEntity.internalServerError().body("FAILED"+e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<ImageMetadata>> listImages(){
        try{
            List<ImageMetadata> images=imageService.getAllImages();
            return ResponseEntity.ok(images);
        } catch(IOException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/view/{newfileName}")
    public ResponseEntity<FileSystemResource> getImage(@PathVariable String newfileName){
        try{
            File file=new File("uploads/"+newfileName);
            if(!file.exists()){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new FileSystemResource(file));
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

}

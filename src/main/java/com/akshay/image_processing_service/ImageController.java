package com.akshay.image_processing_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

}

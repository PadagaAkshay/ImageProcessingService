package com.akshay.image_processing_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @GetMapping("/view/{newfileName:.+}")
    public ResponseEntity<FileSystemResource> getImage(@PathVariable String newfileName){
        try{
            Path filePath= Paths.get(System.getProperty("user.dir")).resolve("uploads").resolve(newfileName);
            File file=filePath.toFile();
            System.out.println("Looking for file at: " + file.getAbsolutePath());
            if(!file.exists()){
                return ResponseEntity.notFound().build();
            }
            String contentType=java.nio.file.Files.probeContentType(filePath);
            if(contentType==null){
                contentType="image/jpeg";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new FileSystemResource(file));
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteImage(@PathVariable String fileName){
        boolean isDeleted= imageService.deleteImage(fileName);
        if(isDeleted){
            return ResponseEntity.ok("File deleted Successfully");
        }
        else{
            return ResponseEntity.status(404).body("File not found or could'nt delete");
        }
    }

}

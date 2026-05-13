package com.akshay.image_processing_service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ImageMetadata{
    @Id
    private String id;
     private String fileName;
     private String originalName;
     private long fileSize;
    public ImageMetadata(String fileName, String originalName, long fileSize) {
        this.fileName = fileName;
        this.originalName = originalName;
        this.fileSize = fileSize;
    }
}
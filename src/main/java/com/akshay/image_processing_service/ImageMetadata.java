package com.akshay.image_processing_service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageMetadata {
    private String fileName;
    private String originalName;
    private long fileSize;
}

package com.akshay.image_processing_service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends MongoRepository<ImageMetadata,Long> {
    void deleteByFileName(String fileName);
}

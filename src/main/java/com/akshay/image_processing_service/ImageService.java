package com.akshay.image_processing_service;
import jakarta.annotation.PostConstruct;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {
    private final Path root = Paths.get(System.getProperty("user.dir")).resolve("uploads");
    @Autowired
    private ImageRepository imageRepository;
    @PostConstruct
    public void init(){
        try{
            if(!Files.exists(root)){
                Files.createDirectories(root);
                System.out.println("Directory created at"+root.toAbsolutePath());
            }
        }catch(IOException e){
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }
    public String processandsave(MultipartFile file) throws IOException{

        if(!Files.exists(root)){
            Files.createDirectories(root);

        }
        String originalName=file.getOriginalFilename();
        String baseName=originalName!=null?originalName.replaceAll("\\s+","_"):"image";
        if(baseName.contains(".")){
            baseName=baseName.substring(0,baseName.lastIndexOf("."));
        }
        String finalFileName = "gray_" + System.currentTimeMillis() + "_" + baseName + ".jpg";
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage grayImage=Scalr.apply(originalImage,Scalr.OP_GRAYSCALE);
        BufferedImage thumbnail = Scalr.resize(grayImage,300);
        Path filePath = this.root.resolve(finalFileName);
        BufferedImage rgbImage=new BufferedImage(thumbnail.getWidth(),thumbnail.getHeight(),BufferedImage.TYPE_INT_RGB);
        rgbImage.getGraphics().drawImage(thumbnail,0,0,null);
        boolean wasWritten=ImageIO.write(thumbnail,"jpeg",filePath.toFile());
        if(!wasWritten){
            wasWritten=ImageIO.write(thumbnail,"png",filePath.toFile());
            System.out.println("Fallback to PNG used: " + wasWritten);
        }
        ImageMetadata metadata=new ImageMetadata(finalFileName, file.getOriginalFilename(), file.getSize());
        imageRepository.save(metadata);
        System.out.println("Saving file to: " + filePath.toAbsolutePath());
        return finalFileName;
    }

    public List<ImageMetadata> getAllImages() throws IOException{
        if(!Files.exists(root)){
            return List.of();
        }
       return imageRepository.findAll();
    }

    public boolean deleteImage(String fileName){
        try {
            Path filePath= this.root.resolve(fileName);
            boolean diskDeleted=Files.deleteIfExists(filePath);
            if (diskDeleted){
                imageRepository.deleteByFileName(fileName);
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }

    }

}

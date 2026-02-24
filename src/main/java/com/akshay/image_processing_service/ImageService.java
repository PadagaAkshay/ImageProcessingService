package com.akshay.image_processing_service;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;

@Service
public class ImageService {
    private final Path root=Paths.get("uploads");
    public String processandsave(MultipartFile file) throws IOException{
        if(!Files.exists(root)){
            Files.createDirectories(root);

        }
        String fileName=System.currentTimeMillis()+' '+file.getOriginalFilename();

        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage thumbnail = Scalr.resize(originalImage,300);
        Path filePath = this.root.resolve(fileName);

        ImageIO.write(thumbnail,"jpg",filePath.toFile());

        return fileName;
    }
}

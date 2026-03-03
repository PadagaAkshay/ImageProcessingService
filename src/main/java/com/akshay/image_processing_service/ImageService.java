package com.akshay.image_processing_service;
import org.imgscalr.Scalr;
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

    private final Path root=Paths.get("uploads");
    public String processandsave(MultipartFile file) throws IOException{
        if(!Files.exists(root)){
            Files.createDirectories(root);

        }
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage grayImage=Scalr.apply(originalImage,Scalr.OP_GRAYSCALE);
        BufferedImage thumbnail = Scalr.resize(grayImage,300);
        String fileName="gray"+System.currentTimeMillis()+'_'+file.getOriginalFilename();

        Path filePath = this.root.resolve(fileName);

        ImageIO.write(thumbnail,"jpg",filePath.toFile());

        return fileName;
    }

    public List<ImageMetadata> getAllImages() throws IOException{
        if(!Files.exists(root)){
            return List.of();
        }
        return Files.list(root)
                .map(path->{
                    File file=path.toFile();
                    return new ImageMetadata(
                            file.getName(),
                            file.getName().substring(file.getName().indexOf("_")+1),
                            file.length()
                    );
                })
                .collect(Collectors.toList());
    }
}

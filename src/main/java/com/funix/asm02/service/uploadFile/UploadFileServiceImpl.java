package com.funix.asm02.service.uploadFile;

import com.funix.asm02.entity.CV;
import com.funix.asm02.service.cv.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

@Service
public class UploadFileServiceImpl implements UploadFileService{
    private final Path rootLocation;
    private CVService cvService;
    @Autowired
    public UploadFileServiceImpl(CVService cvService) {
        this.rootLocation = Paths.get(".","uploads");
        this.cvService = cvService;
    }

    @Override
    public void store(MultipartFile file, String type, int id) {
        /*
        type: cv or images
         */
        Path newPath = this.rootLocation.resolve(type).resolve(String.valueOf(id));
        try{
            if(!Files.exists(newPath)){
                Files.createDirectories(newPath);
            }
            try(InputStream inputStream = file.getInputStream()){
                Path destinationPath = newPath.resolve(
                        Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
                Files.copy(inputStream,destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int idCv, String type, int id) {

        CV cv = cvService.findById(idCv);
        Path filePath = this.rootLocation.resolve(type).resolve(String.valueOf(id)).resolve(cv.getFileName());
        try {
            Files.delete(filePath);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


package com.funix.asm02.service.uploadFile;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    void store(MultipartFile file,String type, int id);
    void delete(int idCv, String type, int id);
}

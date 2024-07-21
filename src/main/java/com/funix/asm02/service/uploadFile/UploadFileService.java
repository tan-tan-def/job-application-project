package com.funix.asm02.service.uploadFile;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadFileService {
    String upload(MultipartFile file) throws IOException;
}

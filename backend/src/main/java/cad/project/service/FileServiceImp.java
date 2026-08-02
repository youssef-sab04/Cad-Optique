package cad.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service

public class FileServiceImp  implements  FileService{
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId + originalFileName.substring(originalFileName.lastIndexOf('.'));

        File folder = new File(path).getAbsoluteFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File destFile = new File(folder, fileName);
        file.transferTo(destFile);
        return fileName;
    }
}

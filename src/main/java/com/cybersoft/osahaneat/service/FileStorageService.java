package com.cybersoft.osahaneat.service;

import com.cybersoft.osahaneat.service.imp.FileStorageServiceImp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService implements FileStorageServiceImp {

    @Value("${upload.path}")
    private String path;
    private Path root;
    // resole : /
    // buger.png
    @Override
    public boolean saveFiles(MultipartFile file) {
        try{
            System.out.println("kiemtra path " + path);
            init();
            Files.copy(file.getInputStream(),root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING); // /uploads/bugerking.png

            return true;
        }catch (Exception e){
            System.out.println("Error save file " + e.getMessage());
            return  false;
        }
    }

    @Override
    public Resource load(String fileName) {
        try{
            Path file = root.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }else {
                return null;
            }
        }catch (Exception e){
            System.out.println("Error load file " + e.getMessage());
            return null;
        }


    }

    private void init(){
        try{
            root = Paths.get(path);
            if(!Files.exists(root)){
                Files.createDirectories(root);
            }
        }catch (Exception e){
            System.out.println("Error create root folder " + e.getMessage());
        }

    }

}

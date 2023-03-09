package com.cybersoft.osahaneat.controller;

import com.cybersoft.osahaneat.payload.ResponseData;
import com.cybersoft.osahaneat.service.imp.FileStorageServiceImp;
import com.cybersoft.osahaneat.service.imp.MenuServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    FileStorageServiceImp fileStorageServiceImp;

    @Autowired
    MenuServiceImp menuServiceImp;

    //Ở API add menu cho phép người dùng tạo ra một menu mới và upload hình ảnh của menu này
    //Lấy danh sách menu và hình ảnh của menu
    //Redis

    //form-data : stream
    //request body : Chuyển file về base64 -> sẽ bị x1.5 dung lượng file
    //Lưu ở database : file phải chuyển về base64, file phải là dạng byte -> Không khuyến khích
    //Lưu ở ổ đĩa : file upload sẽ được lưu vào ổ đĩa cứng -> Luôn sử dụng
    @PostMapping("")
    public ResponseEntity<?> addMenu(
            @RequestParam MultipartFile file,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam String instruction,
            @RequestParam int cate_res_id
    ){
        ResponseData responseData = new ResponseData();
        boolean isSuccess = menuServiceImp.insertFood(file,name,description,price,instruction,cate_res_id);
        responseData.setData(isSuccess);

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllMenu(){
        ResponseData responseData = new ResponseData();
        responseData.setData(menuServiceImp.getAllFood());

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/files/{filesname:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filesname){
        Resource resource = fileStorageServiceImp.load(filesname);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + filesname + "\"")
                .body(resource);
    }

}

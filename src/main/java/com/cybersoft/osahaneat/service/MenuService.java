package com.cybersoft.osahaneat.service;

import com.cybersoft.osahaneat.dto.FoodDTO;
import com.cybersoft.osahaneat.entity.CategoryRestaurant;
import com.cybersoft.osahaneat.entity.Food;
import com.cybersoft.osahaneat.repository.FoodRepository;
import com.cybersoft.osahaneat.service.imp.FileStorageServiceImp;
import com.cybersoft.osahaneat.service.imp.MenuServiceImp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService implements MenuServiceImp {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    FileStorageServiceImp fileStorageServiceImp;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean insertFood(MultipartFile file, String name, String description, double price, String instruction, int cate_res_id) {
        boolean isInsertSuccess = false;
        boolean isSuccess = fileStorageServiceImp.saveFiles(file);
        if(isSuccess){
            //Lưu dữ liệu khi file đã được lưu thành công
            try{
                Food food = new Food();
                food.setName(name);
                food.setDesc(description);
                food.setPrice(price);
                food.setIntruction(instruction);
                food.setImage(file.getOriginalFilename());

                CategoryRestaurant categoryRestaurant = new CategoryRestaurant();
                categoryRestaurant.setId(cate_res_id);

                food.setCategoryRestaurant(categoryRestaurant);

                foodRepository.save(food);

                isInsertSuccess = true;
            }catch (Exception e){
                System.out.println("Error insert Food " + e.getMessage());
            }
        }

        return isInsertSuccess;
    }

    @Override
//    @Cacheable("food")
    public List<FoodDTO> getAllFood() {
        //Kỹ thuật đánh index trong database
        System.out.println("Kiem tra cache");
        List<Food> list;
        List<FoodDTO> dtoList = new ArrayList<>();
        Gson gson = new Gson();

        String data = (String) redisTemplate.opsForValue().get("foods");
        if(data == null){
            list = foodRepository.findAll();
            for (Food food:list) {
                FoodDTO foodDTO = new FoodDTO();
                foodDTO.setName(food.getName());
                foodDTO.setImage(food.getImage());
                foodDTO.setDesc(food.getDesc());

                dtoList.add(foodDTO);
            }
            redisTemplate.opsForValue().set("foods",gson.toJson(dtoList));
        }else{
            dtoList = gson.fromJson(data,new TypeToken<List<FoodDTO>>(){}.getType());
        }
        System.out.println("Kiemtra " + data);

        return dtoList;
    }

}

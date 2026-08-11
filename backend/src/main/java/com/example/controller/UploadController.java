package com.example.controller;

import java.io.File;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.pojo.Result;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
public class UploadController {
    @PostMapping("/upload")
    public Result upload(String name, Integer age, MultipartFile file){
        log.info("接收参数: {},{},{}",name,age,file);
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取后缀
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成新文件名
        String newFileName = UUID.randomUUID().toString() + extension;

        //保存文件
        try{
            file.transferTo(new File("D:/image/" + newFileName));
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return Result.success();

    }
}

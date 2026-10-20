package com.tlias.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tlias.pojo.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传接口。
 * <p>
 * 存储目录由 {@code app.upload.dir} 配置（默认 {@code ./uploads}），
 * 生产环境可通过环境变量 {@code UPLOAD_DIR} 覆盖。
 */
@Slf4j
@RestController
public class UploadController {

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    @PostMapping("/upload")
    public Result upload(String name, Integer age, MultipartFile file) {
        log.info("接收参数: {},{},{}", name, age, file);
        if (file == null || file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取后缀
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成新文件名，避免重名覆盖
        String newFileName = UUID.randomUUID().toString() + extension;

        //保存文件
        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);
            file.transferTo(new File(dir.toFile(), newFileName));
        } catch (IOException e) {
            log.error("文件上传失败: {}", originalFilename, e);
            return Result.error("文件上传失败");
        }

        return Result.success(newFileName);

    }
}

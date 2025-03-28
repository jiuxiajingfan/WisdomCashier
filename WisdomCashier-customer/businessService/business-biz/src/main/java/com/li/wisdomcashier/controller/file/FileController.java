package com.li.wisdomcashier.controller.file;

import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.utils.MinioUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;

/**
 * @ClassName FileController
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/28 15:40
 * @Version 1.0
 */
@RestController
@RequestMapping("/file")
@Tag(name = "文件上传下载相关")
@Slf4j
@RefreshScope
public class FileController {
    @Resource
    private MinioUtils minIoUtils;
    @Value("${minio.endpoint}")
    private String address;
    @Value("${minio.bucketName}")
    private String bucketName;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        String upload;
        try {
            upload = minIoUtils.upload(file);
        } catch (MaxUploadSizeExceededException e) {
            return R.error("文件大小过大！");
        }
        return R.ok(address + "/" + bucketName + "/" + upload);
    }
}
package com.li.wisdomcashier.customer.controller;

import com.alibaba.fastjson2.JSONObject;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.common.UnCheck;
import com.li.wisdomcashier.base.util.MinioUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;

/**
 * @ClassName FileController
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/28 15:40
 * @Version 1.0
 */
@RestController
@RequestMapping("/file")
@Api(tags = "文件上传下载相关")
@Slf4j
public class FileController {
    @Resource
    private MinioUtils minIoUtils;
    @Value("${minio.endpoint}")
    private String address;
    @Value("${minio.bucketName}")
    private String bucketName;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        String upload = minIoUtils.upload(file);
        return R.ok(address+"/"+bucketName+"/"+upload);
    }
}
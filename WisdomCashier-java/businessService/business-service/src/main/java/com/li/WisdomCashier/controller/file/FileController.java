package com.li.WisdomCashier.controller.file;

import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.utils.MinioUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

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
        String upload;
        try {
            upload = minIoUtils.upload(file);
        } catch (MaxUploadSizeExceededException e) {
            return R.error("文件大小过大！");
        }
        return R.ok(address+"/"+bucketName+"/"+upload);
    }
}
package com.leyou.controller;

import com.leyou.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/21 17:48
 * @desc
 */
@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    /**
     * 上传图片
     * Content-Disposition: form-data; name="file"; filename="2.jpg"
     * Content-Type: image/jpeg
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImg(@RequestParam("file") MultipartFile file) throws Exception {
        String filePath = uploadService.uploadImg(file);
        if(StringUtils.isBlank(filePath)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return  ResponseEntity.ok(filePath);
    }
}

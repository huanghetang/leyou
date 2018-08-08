package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhoumo
 * @datetime 2018/7/21 19:06
 * @desc
 */
@Service
public class UploadService {
    //允许上传的图片类型
    private List<String> allowMediaType = Arrays.asList("image/jpg", "image/png", "image/jpeg");
    //日志
    private Logger logger = LoggerFactory.getLogger(UploadService.class);

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    /**
     * 上传图片,单机版
     *
     * @param file
     * @return 图片保存的路径
     * @throws IOException
     */
    public String uploadImg1(MultipartFile file) throws IOException {
        //获取文件名称
        String originalFilename = file.getOriginalFilename();
        //获取文件类型
        String fileType = file.getContentType();
        if (!allowMediaType.contains(fileType)) {
            logger.error("文件扩展名不匹配!");
            return null;
        }
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) {
            logger.error("文件类型不匹配!");
            return null;
        }
        //设置根目录
        File imgRoot = new File("E:\\nginx-1.8.0\\html");
        if (!imgRoot.exists()) {
            imgRoot.mkdirs();
        }
        //设置上传文件的目录
        File newFile = new File(imgRoot, originalFilename);
        //上传图片
        file.transferTo(newFile);
        System.out.println("newFile.getAbsolutePath() = " + newFile.getAbsolutePath());
        return "E:\\nginx-1.8.0\\html\\" + originalFilename;
    }


    /**
     * 上传图片,fastDFS
     *
     * @param file
     * @return 图片保存的路径
     * @throws IOException
     */
    public String uploadImg(MultipartFile file) throws IOException {
        //获取文件名称
        String originalFilename = file.getOriginalFilename();
        //获取文件类型
        String fileType = file.getContentType();
        if (!allowMediaType.contains(fileType)) {
            logger.error("文件扩展名不匹配!");
            return null;
        }
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) {
            logger.error("文件类型不匹配!");
            return null;
        }
        //扩展名
        String extensionName = StringUtils.substringAfterLast(originalFilename, ".");
        // 上传文件
        StorePath storePath = this.storageClient.uploadFile(file.getInputStream(),
                file.getSize(), extensionName, null);
        // 带分组的路径
//        System.out.println(storePath.getFullPath());
        // 不带分组的路径
//        System.out.println(storePath.getPath());
        String imgUrl = "http://image.leyou.com/" + storePath.getFullPath();
        System.out.println("imgUrl = " + imgUrl);
        return imgUrl;
    }

    /**
     * 上传图片并生成缩略图到fastDFS
     *
     * @param file
     * @param thumbImageOrNot 要不要返回缩略图路径 true 返回缩略图路径,false 返回原图片路径
     * @return 图片保存的缩略图路径
     * @throws IOException
     */
    public String uploadThumbImg(MultipartFile file, boolean thumbImageOrNot) throws IOException {
        //获取文件名称
        String originalFilename = file.getOriginalFilename();
        //获取文件类型
        String fileType = file.getContentType();
        if (!allowMediaType.contains(fileType)) {
            logger.error("文件扩展名不匹配!");
            return null;
        }
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) {
            logger.error("文件类型不匹配!");
            return null;
        }
        //扩展名
        String extensionName = StringUtils.substringAfterLast(originalFilename, ".");
        // 上传图片并生成缩略图
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                extensionName, null);
        String imgUrl = "http://image.leyou.com/";
        //获取缩略图路径
        if (thumbImageOrNot) {
            String thumbImagePath = thumbImageConfig.getThumbImagePath(storePath.getFullPath());
            imgUrl += thumbImagePath;
        } else {
            //获取原图路径
            imgUrl += storePath.getFullPath();
        }
        System.out.println("imgUrl = " + imgUrl);
        logger.info("上传了一张图片:url="+imgUrl);
        return imgUrl;
    }
}

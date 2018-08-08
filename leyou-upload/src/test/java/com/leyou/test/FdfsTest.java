package com.leyou.test;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.UploadServiceApp;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UploadServiceApp.class)
public class FdfsTest {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Test
    public void testUpload() throws FileNotFoundException {
        System.out.println("storageClient = " + storageClient);
        File file = new File("D:\\images\\1528357697561285.jpg");
        // 上传
        StorePath storePath = this.storageClient.uploadFile(
                new FileInputStream(file), file.length(), "jpg", null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
    }

    @Test
    public void testUploadAndCreateThumb() throws FileNotFoundException {
        File file = new File("D:\\images\\1.jpg");
        // 上传并且生成缩略图
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                new FileInputStream(file), file.length(), "png", null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
//        System.out.println(storePath.getPath());
        // 获取缩略图路径
        String path = thumbImageConfig.getThumbImagePath(storePath.getPath());
        String path1 = thumbImageConfig.getThumbImagePath(storePath.getFullPath());
        System.out.println(path);
        System.out.println("path1 = " + path1);

    }

    @Test
    public void testDelete() {
        //http://image.leyou.com/group1/M00/00/00/wKgZhVtTS4KAY55LAACenmGVJ5o413.jpg
        String url = "http://image.leyou.com/group1/M00/00/00/wKgZhVtTS4KAY55LAACenmGVJ5o413.jpg";
        String path = StringUtils.substringAfter(url, "http://image.leyou.com/");
        storageClient.deleteFile(path);
        System.out.println("123 ");
    }



}
package com.wmz.blog.crawling.controller;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.wmz.blog.comment.utils.OssUtils;
import com.wmz.blog.picture.model.TPicture;
import com.wmz.blog.picture.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class CrawlingController {
    private static Integer i = 491;

    @Autowired
    private PictureService pictureService;
    public static void main(String[] args) throws Exception {
        getData();
    }

    @GetMapping("/crawling")
    public void saveImage() {
        OssUtils ossUtils = new OssUtils();
        List<TPicture> tPictureList = new ArrayList<>();
        for (int i = 1; i <= 493; i++) {
            String imgUrl = ossUtils.getImgUrl(i + ".jpg");
            TPicture tPicture = new TPicture();
            tPicture.setId(IdWorker.getIdStr());
            tPicture.setCreateTime(LocalDateTime.now());
            tPicture.setStatus("1");
            tPicture.setType("2");
            tPicture.setUrl(imgUrl);
            tPictureList.add(tPicture);
        }
        pictureService.saveBatch(tPictureList);
    }

    public static List<Map<String,String>> getData() throws Exception {
        //读取文件
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("E:\\学习\\ce.txt")), "UTF-8")); //这里可以控制编码
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //源码文件正文
        String fileContest = new String(sb);
        String getPrefix = "http://"; //正则匹配缩略图路径前缀
        String getSuffix = "&amp;rf=photolist&amp;t=5"; //正则匹配缩略图路径后缀
        //1、匹配子串
        String regex = getPrefix + "(.*?)" + getSuffix;
        //2、获取匹配器
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileContest);
        while (matcher.find()) {
            //获取匹配缩略图路径
            String imageUrl = matcher.group();
            // 转化为文件
            getFile(imageUrl);
        }
        return null;
    }

    /**
     * 网络url转为文件
     * @param url
     * @return
     * @throws Exception
     */
    public static File getFile(String url) throws Exception {
        //对本地文件命名
        String fileName = url.substring(url.lastIndexOf("."));
        File file = null;

        URL urlfile;
        InputStream inStream = null;
        OutputStream os = null;
        String filePath = "E:\\学习\\xiaofei\\";
        try {
            file = new File(filePath + i + ".jpg");
            //下载
            urlfile = new URL(url);
            inStream = urlfile.openStream();
            os = new FileOutputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            i ++ ;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != inStream) {
                    inStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }
}

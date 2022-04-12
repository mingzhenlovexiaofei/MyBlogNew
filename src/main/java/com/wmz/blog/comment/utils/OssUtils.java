package com.wmz.blog.comment.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OssUtils {

    public static final String endpoint = "oss-cn-beijing.aliyuncs.com";
    public static final String accessKeyId = "LTAI4G6MNcfignGwyXg6Hx5S";
    public static final String accessKeySecret = "oUJ8biYyf3Cj3N0sxcLHzSEKTJaUax";
    public static final String bucketName = "wmz-xz";
    // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
    public static final String objectName = "xiaofei/";

    public static OSS ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

    public static void main(String[] args) throws Exception {
        OssUtils ossUtils = new OssUtils();
        for (int i = 1; i <= 493; i++) {
            String imgUrl = ossUtils.getImgUrl(i + ".jpg");
            System.out.println(imgUrl);
        }
    }

    /**
     * 上传文件
     * @param fileName   上传的文件名
     * @param folderName 上传到哪个文件夹
     * @param path       准备上传的文件的路径
     */
    public static void uploadFile(String fileName, String folderName, String path) throws Exception {
        PutObjectResult result = ossClient.putObject(bucketName, folderName + fileName, new File(path + fileName));
        String eTag = result.getETag();
        String requestId = result.getRequestId();
        ossClient.shutdown();
    }

    public static void uploadAllFile(List<String> allFile, String folderName, String path) throws Exception {
        if (!CollectionUtils.isEmpty(allFile)) {
            for (String fileName : allFile) {
                ossClient.putObject(bucketName, folderName + fileName, new File(path + fileName));
                // 上传成功 记录
            }
        }
        ossClient.shutdown();
        System.out.println("全部文件上传完成!");
    }

    /**
     * 获取指定目录下的所有文件
     *
     * @param directoryName 指定目录    dev/
     * @return
     */
    public static List<String> getAllFilesBySpecifyDirectory(String directoryName) {
        List<String> list = new ArrayList<>();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        listObjectsRequest.setPrefix(directoryName); //指定下一级文件
        listObjectsRequest.setDelimiter("/");//跳出递归循环，只去指定目录下的文件。使用它时 Prefix文件路径要以“/”结尾
        ObjectListing listing = ossClient.listObjects(listObjectsRequest);
        List<OSSObjectSummary> objectSummaries = listing.getObjectSummaries();
        for (OSSObjectSummary summary : objectSummaries) {
            // 获取文件名
            // key = dev/code.zip[rsong].1575342717.zip
            String key = summary.getKey();
            list.add(key);
        }
        // 关闭OSSClient
        ossClient.shutdown();
        System.out.println("查询文件完成！");
        return list;
    }

    // 流式下载
    public static String upload(File file) {
        String name = file.getName();
        // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, file);
        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
        ossClient.putObject(putObjectRequest);
        return null;
    }

    /**
     * 获取图片路径
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            String url =  this.getUrl(objectName + split[split.length - 1]);
//                log.info(url);
//                String[] spilt1 = url.split("\\?");
//                return spilt1[0];
            return url;
        }
        return null;
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10 * 10);
        // 生成URL
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }
}

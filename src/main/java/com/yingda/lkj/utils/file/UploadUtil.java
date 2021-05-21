package com.yingda.lkj.utils.file;

import com.yingda.lkj.beans.entity.system.UploadImage;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author hood  2020/1/9
 */
public class UploadUtil {
    public static final String RESOURCES_URL = "/upload";
    public static String RESOURCE_LOCATIONS;

    public static String saveToUploadPath(MultipartFile rawFile) throws CustomException, IOException {
        File baseDir = new File(RESOURCE_LOCATIONS);
        if (!baseDir.exists())
            throw new CustomException(new Json(JsonMessage.SYS_ERROR, "服务器上传目录设置不正确，请联系管理员检查 config.properties.resourcesPath"));

        // 月目录
        String month = new SimpleDateFormat("yyyy_MM").format(new Date());
        File monthDir = new File(baseDir, month);
        if (!monthDir.exists())
            monthDir.mkdir();

        // 日目录
        String day = new SimpleDateFormat("dd").format(new Date());
        File uploadDir = new File(monthDir, day);
        if (!uploadDir.exists())
            uploadDir.mkdir();

        // 源文件后缀
        String originalFilename = rawFile.getOriginalFilename();
        String uploadFileSuffix = originalFilename.split("\\.")[1];

        // 生成文件
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + uploadFileSuffix;
        File uploadFile = new File(uploadDir, fileName);
        uploadFile.createNewFile();

        // 读取源文件到生成的文件
        rawFile.transferTo(uploadFile);

        return RESOURCES_URL + "/" + month + "/" + day + "/" + fileName;
    }

    public static void main(String[] args) {
        File file = new File("/upload/2021_03/16/fabe1bb48cf04b31bd215d9fe1eaa507.exe");
        System.out.println(file);
    }

    public static String saveToUploadPath(byte[] rawData, String originalFilename) throws CustomException, IOException {
        File baseDir = new File(RESOURCE_LOCATIONS);
        if (!baseDir.exists())
            throw new CustomException(new Json(JsonMessage.SYS_ERROR, "服务器上传目录设置不正确，请联系管理员检查 config.properties.upload_path"));

        // 月目录
        String month = new SimpleDateFormat("yyyy_MM").format(new Date());
        File monthDir = new File(baseDir, month);
        if (!monthDir.exists())
            monthDir.mkdir();

        // 日目录
        String day = new SimpleDateFormat("dd").format(new Date());
        File uploadDir = new File(monthDir, day);
        if (!uploadDir.exists())
            uploadDir.mkdir();

        // 源文件后缀
        String uploadFileSuffix = getAppUploadImageSuffix(originalFilename);

        // 生成文件
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + uploadFileSuffix;
        File uploadFile = new File(uploadDir, fileName);
        uploadFile.createNewFile();

        // 读取源文件到生成的文件
        new FileOutputStream(uploadFile).write(rawData);

        return RESOURCES_URL + "/" + month + "/" + day + "/" + fileName;
    }

    public static String getRealPath(UploadImage image) {
        String url = image.getUrl();
        if (StringUtils.isNotEmpty(url))
            return url.replace(RESOURCES_URL, RESOURCE_LOCATIONS);

        return "";
    }

    private static String getAppUploadImageSuffix(String fileName) {
        fileName = getImageName(fileName);
        return fileName.split("\\.")[1];
    }

    public static String getAppUploadImageFileName(String fileName) {
        fileName = getImageName(fileName);
        return fileName.split("\\.")[0];
    }

    private static String getImageName(String rawPictureName) {
        String[] rawPictureNameArr = rawPictureName.split("/");
        return rawPictureNameArr[rawPictureNameArr.length - 1];
    }

}

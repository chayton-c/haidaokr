package com.yingda.lkj.controller.app;

import com.yingda.lkj.beans.entity.system.UploadImage;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.service.system.UploadImageService;
import com.yingda.lkj.utils.JsonUtils;
import com.yingda.lkj.utils.file.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author hood  2020/6/25
 */
@Controller
@RequestMapping("/app/upload")
public class AppUploadController extends BaseController {

    @Autowired
    private BaseService<UploadImage> uploadImageBaseService;
    @Autowired
    private UploadImageService uploadImageService;


    @RequestMapping("/uploadImage")
    @ResponseBody
    public Json uploadImage() throws Exception {
        String inputStream = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        List<UploadFile> parses = JsonUtils.parseList(inputStream, UploadFile.class);
        UploadFile parse = parses.get(0);

        String base64Image = parse.getFile();
        String fileName = parse.getFileName();

        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);

        String path = UploadUtil.saveToUploadPath(imageBytes, fileName);
        fileName = UploadUtil.getAppUploadImageFileName(fileName);

        uploadImageService.save(fileName, path);

        return new Json(JsonMessage.SUCCESS);
    }

    class UploadFile {
        String file;
        String fileName;

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}

package com.yingda.lkj.service.system;

import com.yingda.lkj.beans.entity.system.UploadImage;

import java.util.List;
import java.util.Map;

/**
 * @author hood  2020/6/29
 */
public interface UploadImageService {
    UploadImage getById(String id);

    /**
     * @return key: uploadImage.id value:uploadImage
     */
    Map<String, UploadImage> getByIds(List<String> ids);

    /**
     * 保存上传文件记录
     * @param id 为什么要传id:因为上传文件的接口与上传携带文件的数据的接口可能不是一个接口，所以需要上传的设备提供id，然后两个接口使用提供的id
     * 例如上传光电缆标识图片逻辑：
     * @see com.yingda.lkj.controller.app.upload.UploadOpcController
     * @see com.yingda.lkj.controller.app.AppUploadController
     * @param url 保存的文件路径
     */
    UploadImage save(String id, String url);

    void delete(List<String> ids);
}

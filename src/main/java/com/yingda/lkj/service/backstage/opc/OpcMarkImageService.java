package com.yingda.lkj.service.backstage.opc;

import com.yingda.lkj.beans.entity.backstage.opc.OpcMarkImage;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.system.UploadImageService;
import com.yingda.lkj.utils.StreamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("opcMarkImageService")
public class OpcMarkImageService {
    @Autowired
    private BaseDao<OpcMarkImage> opcMarkImageBaseDao;
    @Autowired
    private UploadImageService uploadImageService;

    public void save(String opcMarkId, String uploadImageId) {
        OpcMarkImage opcMarkImage = new OpcMarkImage();
        opcMarkImage.setId(UUID.randomUUID().toString());
        opcMarkImage.setOpcMarkId(opcMarkId);
        opcMarkImage.setUploadImageId(uploadImageId);
        opcMarkImage.setAddTime(new Timestamp(System.currentTimeMillis()));
        opcMarkImageBaseDao.saveOrUpdate(opcMarkImage);
    }

    /**
     * 检查相同的图片，如果有，删掉重复的
     */
    public void checkDuplicated(String opcMarkId) {
        List<OpcMarkImage> duplicatedOpcMarkImages = getByOpcMarkId(opcMarkId);
        List<OpcMarkImage> fixedOpcMarkImages = duplicatedOpcMarkImages.stream().filter(StreamUtil.distinct(OpcMarkImage::getUploadImageId)).collect(Collectors.toList());
        List<String> fixedOpcMarkImagesIds = StreamUtil.getList(fixedOpcMarkImages, OpcMarkImage::getId);
        for (OpcMarkImage opcMarkImage : duplicatedOpcMarkImages) {
            if (!fixedOpcMarkImagesIds.contains(opcMarkImage.getId()))
                opcMarkImageBaseDao.delete(opcMarkImage);
        }
    }

    public List<OpcMarkImage> getByOpcMarkId(String opcMarkId) {
        return opcMarkImageBaseDao.find(
                "from OpcMarkImage where opcMarkId = :opcMarkId",
                Map.of("opcMarkId", opcMarkId)
        );
    }

}

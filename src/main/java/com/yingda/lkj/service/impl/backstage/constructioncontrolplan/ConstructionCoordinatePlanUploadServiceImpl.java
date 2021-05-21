package com.yingda.lkj.service.impl.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionCoordinatePlanUpload;
import com.yingda.lkj.beans.entity.system.UploadImage;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionCoordinatePlanUploadService;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.file.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("constructionCoordinatePlanUploadService")
public class ConstructionCoordinatePlanUploadServiceImpl implements ConstructionCoordinatePlanUploadService {

    @Autowired
    private BaseDao<ConstructionCoordinatePlanUpload> constructionCoordinatePlanUploadBaseDao;
    @Autowired
    private BaseDao<UploadImage> uploadImageBaseDao;

    private ConstructionCoordinatePlanUpload getById(String id) {
        return constructionCoordinatePlanUploadBaseDao.get(
                "from ConstructionCoordinatePlanUpload where id = :id",
                Map.of("id", id)
        );
    }

    /**
     * 根据ID删除文件、文件表记录、方案与文件关联表记录
     * @param constructionCoordinatePlanUploadId
     */
    @Override
    public void deleteDataAndFile(String constructionCoordinatePlanUploadId) {
        ConstructionCoordinatePlanUpload constructionCoordinatePlanUpload = constructionCoordinatePlanUploadBaseDao.get(
                "from ConstructionCoordinatePlanUpload where id = :constructionCoordinatePlanUploadId",
                Map.of("constructionCoordinatePlanUploadId", constructionCoordinatePlanUploadId)
        );

        if (constructionCoordinatePlanUpload != null) {
            String uploadImageId = constructionCoordinatePlanUpload.getUploadImageId();
            UploadImage uploadImage = uploadImageBaseDao.get(
                    "from UploadImage where id = :uploadImageId",
                    Map.of("uploadImageId", uploadImageId)
            );

            if (uploadImage != null) {
                String fileUrl = uploadImage.getUrl();

                if (StringUtils.isNotEmpty(fileUrl)) {
                    File file = new File(fileUrl.replace(UploadUtil.RESOURCES_URL, UploadUtil.RESOURCE_LOCATIONS));
                    if (file.isFile() && file.exists())
                        file.delete();
                }

                String constructionCoordinatePlanId = constructionCoordinatePlanUpload.getConstructionCoordinatePlanId();
                constructionCoordinatePlanUploadBaseDao.executeHql(
                        "delete from ConstructionCoordinatePlanUpload where construction_coordinate_plan_id = " +
                                ":constructionCoordinatePlanId and upload_image_id = :uploadImageId",
                        Map.of("constructionCoordinatePlanId", constructionCoordinatePlanId, "uploadImageId", uploadImageId)
                );

                uploadImageBaseDao.delete(uploadImage);
            }
        }
    }

    @Override
    public void deleteByConstructionControlPlan(ConstructionControlPlan constructionControlPlan) {
        if (constructionControlPlan != null) {

            String sql = """
                SELECT
                	construction_coordinate_plan_upload.id,
                	construction_coordinate_plan_upload.add_time AS addTime,
                	construction_coordinate_plan_upload.construction_coordinate_plan_id AS constructionCoordinatePlanId,
                	construction_coordinate_plan_upload.file_type AS fileType,
                	construction_coordinate_plan_upload.upload_image_id AS uploadImageId,
                	construction_coordinate_plan_upload.fileName AS fileName,
                	construction_coordinate_plan_upload.fileUrl AS fileUrl 
                FROM
                	construction_coordinate_plan_upload
                WHERE
                	construction_coordinate_plan_id = :constructionCoordinatePlanId 
                	AND file_type = :fileType 
                """;

            List<ConstructionCoordinatePlanUpload> constructionCoordinatePlanUploads = new ArrayList<>();

            if(constructionControlPlan.getPlanStatus() == ConstructionControlPlan.TECH_COUNTERSIGN) {
                constructionCoordinatePlanUploads = constructionCoordinatePlanUploadBaseDao.findSQL(
                        sql,
                        Map.of("constructionCoordinatePlanId", constructionControlPlan.getId(),"fileType",ConstructionCoordinatePlanUpload.COOPERATIVE_SCHEME),
                        ConstructionCoordinatePlanUpload.class
                );
            }

            if(constructionControlPlan.getPlanStatus() == ConstructionControlPlan.SAFE_COUNTERSIGN) {
                constructionCoordinatePlanUploads = constructionCoordinatePlanUploadBaseDao.findSQL(
                        sql,
                        Map.of("constructionCoordinatePlanId", constructionControlPlan.getId(), "fileType", ConstructionCoordinatePlanUpload.SAFETY_PROTOCOL),
                        ConstructionCoordinatePlanUpload.class
                );
            }

            constructionCoordinatePlanUploads.forEach(constructionCoordinatePlanUpload -> {
                deleteDataAndFile(constructionCoordinatePlanUpload.getId());
            });
        }
    }

    @Override
    public void getByConstructionControlPlanId(String constructionControlPlanId) {

    }


}

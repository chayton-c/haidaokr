package com.yingda.lkj.controller.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.Constant;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionCoordinatePlanUpload;
import com.yingda.lkj.beans.entity.system.UploadImage;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionControlPlanService;
import com.yingda.lkj.service.backstage.constructioncontrolplan.ConstructionCoordinatePlanUploadService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.service.system.UploadImageService;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.file.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/backstage/constructionCoordinatePlanUpload")
@RestController
public class ConstructionCoordinatePlanUploadController extends BaseController {

    @Autowired
    private BaseService<ConstructionCoordinatePlanUpload> constructionCoordinatePlanUploadBaseService;
    @Autowired
    private BaseService<ConstructionControlPlan> constructionControlPlanBaseService;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;
    @Autowired
    private ConstructionCoordinatePlanUploadService constructionCoordinatePlanUploadService;
    @Autowired
    private BaseService<UploadImage> uploadImageBaseService;
    @Autowired
    private UploadImageService uploadImageService;
    public ConstructionCoordinatePlanUpload pageConstructionCoordinatePlanUpload;

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        String constructionControlPlanId = req.getParameter("constructionControlPlanId");
        Map<String, Object> params = new HashMap<>();

        String sql = """
                SELECT
                	coordinatePlanUpload.construction_coordinate_plan_id AS constructionCoordinatePlanId,
                	coordinatePlanUpload.id,
                	coordinatePlanUpload.add_time AS addTime,
                	coordinatePlanUpload.file_type AS fileType,
                	coordinatePlanUpload.upload_image_id AS uploadImageId,
                	uploadImage.url AS fileUrl
                FROM
                	construction_coordinate_plan_upload AS coordinatePlanUpload
                	INNER JOIN construction_control_plan AS controlPlan ON coordinatePlanUpload.construction_coordinate_plan_id = controlPlan.id
                	INNER JOIN upload_image AS uploadImage ON coordinatePlanUpload.upload_image_id = uploadImage.id 
                WHERE
                	1 = 1
                """;
        if (StringUtils.isNotEmpty(constructionControlPlanId)) {
            sql += "AND coordinatePlanUpload.construction_coordinate_plan_id = :constructionControlPlanId\n";
            params.put("constructionControlPlanId", constructionControlPlanId);
        }

        sql += "ORDER BY coordinatePlanUpload.add_time desc";
        List<ConstructionCoordinatePlanUpload> constructionCoordinatePlanUploads = constructionCoordinatePlanUploadBaseService
                .findSQL(sql, params, ConstructionCoordinatePlanUpload.class, page.getCurrentPage(), page.getPageSize());

        constructionCoordinatePlanUploads.forEach(data -> {
            String fileUrl = data.getFileUrl();
            data.setFileName(fileUrl.substring(fileUrl.lastIndexOf("/") + 1));
        });

        attributes.put("constructionCoordinatePlanUploads", constructionCoordinatePlanUploads);
        attributes.put("constructionControlPlan", constructionControlPlanService.getById(constructionControlPlanId));
        setObjectNum(sql, params);
        attributes.put("page", page);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    /**
     * 上传配合方案
     */
    @RequestMapping("/uploadCooperativeSchemeFile")
    public Json uploadCooperativeSchemeFile(MultipartFile file) throws Exception {
        String constructionCoordinatePlanId = req.getParameter("id");

        String path = UploadUtil.saveToUploadPath(file);

        UploadImage uploadImage = uploadImageService.save(UUID.randomUUID().toString(), path);

        ConstructionCoordinatePlanUpload constructionCoordinatePlanUpload = new ConstructionCoordinatePlanUpload();
        constructionCoordinatePlanUpload.setId(UUID.randomUUID().toString());
        constructionCoordinatePlanUpload.setFileType(ConstructionCoordinatePlanUpload.COOPERATIVE_SCHEME);
        constructionCoordinatePlanUpload.setConstructionCoordinatePlanId(constructionCoordinatePlanId);
        constructionCoordinatePlanUpload.setUploadImageId(uploadImage.getId());
        constructionCoordinatePlanUpload.setAddTime(current());
        constructionCoordinatePlanUploadBaseService.saveOrUpdate(constructionCoordinatePlanUpload);

        constructionControlPlanBaseService.executeHql(
                "update ConstructionControlPlan set hasUploadedCooperativeScheme = :true, updateTime = now() where id = :id",
                Map.of("true", Constant.TRUE, "id", constructionCoordinatePlanId)
        );

        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 上传安全协议
     */
    @RequestMapping("/uploadSafetyProtocol")
    public Json uploadSafetyProtocol(MultipartFile file) throws Exception {

        String constructionCoordinatePlanId = req.getParameter("id");

        String path = UploadUtil.saveToUploadPath(file);

        UploadImage uploadImage = uploadImageService.save(UUID.randomUUID().toString(), path);

        ConstructionCoordinatePlanUpload constructionCoordinatePlanUpload = new ConstructionCoordinatePlanUpload();
        constructionCoordinatePlanUpload.setId(UUID.randomUUID().toString());
        constructionCoordinatePlanUpload.setFileType(ConstructionCoordinatePlanUpload.SAFETY_PROTOCOL);
        constructionCoordinatePlanUpload.setConstructionCoordinatePlanId(constructionCoordinatePlanId);
        constructionCoordinatePlanUpload.setUploadImageId(uploadImage.getId());
        constructionCoordinatePlanUpload.setAddTime(current());
        constructionCoordinatePlanUploadBaseService.saveOrUpdate(constructionCoordinatePlanUpload);

        constructionControlPlanBaseService.executeHql(
                "update ConstructionControlPlan set hasUploadedSafetyProtocol = :true, updateTime = now() where id = :id",
                Map.of("true", Constant.TRUE, "id", constructionCoordinatePlanId)
        );

        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/delete")
    public Json delete() {
        String id = req.getParameter("id");
        constructionCoordinatePlanUploadService.deleteDataAndFile(id);
        return new Json(JsonMessage.SUCCESS);
    }

    @ModelAttribute
    public void setPageConstructionCoordinatePlanUpload(ConstructionCoordinatePlanUpload pageConstructionCoordinatePlanUpload) {
        this.pageConstructionCoordinatePlanUpload = pageConstructionCoordinatePlanUpload;
    }
}

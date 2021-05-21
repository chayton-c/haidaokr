package com.yingda.lkj.controller.backstage.opc;

import com.yingda.lkj.beans.entity.backstage.opc.OpcMarkImage;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/backstage/opcMarkImage")
public class OpcMarkImageController extends BaseController {
    @Autowired
    private BaseService<OpcMarkImage> opcMarkImageBaseService;

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String opcMarkId = req.getParameter("opcMarkId");
        String sql = """
                SELECT
                    opcMarkImage.*,
                    uploadImage.url as url
                FROM
                    opc_mark_image opcMarkImage
                    LEFT JOIN upload_image uploadImage ON uploadImage.id = opcMarkImage.upload_image_id
                WHERE
                    opcMarkImage.opc_mark_id = :opcMarkId
                ORDER BY
                    opcMarkImage.add_time DESC
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("opcMarkId", opcMarkId);

        List<OpcMarkImage> opcMarkImages = opcMarkImageBaseService.findSQL(
                sql, params, OpcMarkImage.class
        );

        attributes.put("opcMarkImages", opcMarkImages);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

}

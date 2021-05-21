package com.yingda.lkj.controller.app.upload;

import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.beans.entity.backstage.opc.OpcMark;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.opc.OpcMarkImageService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.JsonUtils;
import com.yingda.lkj.utils.StreamUtil;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.file.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/upload/opc")
public class UploadOpcController extends BaseController {

    @Autowired
    private BaseService<Opc> opcBaseService;
    @Autowired
    private BaseService<OpcMark> opcMarkBaseService;
    @Autowired
    private BaseService<Location> locationBaseService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private OpcMarkImageService opcMarkImageService;

    @RequestMapping("")
    public Json importOpc() throws Exception {
        String opcsStr = req.getParameter("opcs");
        String opcMarksStr = req.getParameter("opcMarks");
        String locationsStr = req.getParameter("locations");
        List<Opc> opcs = JsonUtils.parseList(opcsStr, Opc.class, "yyyy-MM-dd HH:mm:ss");
        for (Opc opc : opcs) {
            opc.setAddTime(current());
            if (StringUtils.isEmpty(opc.getRightStationId()))
                opc.setRightStationId(opc.getLeftStationId());
        }
        opcBaseService.bulkInsert(opcs);

        locationBaseService.executeHql(
                "delete from Location where dataId in :opcIds",
                Map.of("opcIds", StreamUtil.getList(opcs, Opc::getId))
        );
        opcMarkBaseService.executeHql(
                "delete from OpcMark where opcId in :opcIds",
                Map.of("opcIds", StreamUtil.getList(opcs, Opc::getId))
        );

        List<OpcMark> opcMarks = JsonUtils.parseList(opcMarksStr, OpcMark.class);
        opcMarks.forEach(x -> x.setAddTime(current()));
        opcMarkBaseService.bulkInsert(opcMarks);

        // 保存上传图片到中间表
        for (OpcMark opcMark : opcMarks) {
            String photoPathNames = opcMark.getPhotoPathName();
            if (StringUtils.isEmpty(photoPathNames)) continue;

            String[] photoPathNameArr = photoPathNames.split(",");
            for (String photoPathName : photoPathNameArr) {
                if (StringUtils.isEmpty(photoPathName.trim())) continue;

                String appUploadImageFileName = UploadUtil.getAppUploadImageFileName(photoPathName);

                opcMarkImageService.save(opcMark.getId(), appUploadImageFileName);
            }
            opcMarkImageService.checkDuplicated(opcMark.getId());
        }

        opcMarkBaseService.executeHql(
                "delete from Location where dataId in :opcMarkIds",
                Map.of("opcMarkIds", StreamUtil.getList(opcMarks, OpcMark::getId))
        );

        List<Location> locations = JsonUtils.parseList(locationsStr, Location.class);
        locations.forEach(x -> x.setAddTime(current()));
        locationService.bulkInsert(locations);
//        locationBaseService.bulkInsert(locations);

        return new Json(JsonMessage.SUCCESS);
    }

}

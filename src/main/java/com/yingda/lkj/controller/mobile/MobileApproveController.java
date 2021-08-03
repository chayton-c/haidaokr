package com.yingda.lkj.controller.mobile;

import com.yingda.lkj.beans.entity.backstage.approve.ApproveAppeal;
import com.yingda.lkj.beans.entity.backstage.approve.ApproveDetail;
import com.yingda.lkj.beans.pojo.approve.ApproveCount;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.approve.ApproveAppealService;
import com.yingda.lkj.service.backstage.approve.ApproveDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/mobile/mobileApprove")
@RestController
public class MobileApproveController extends BaseController {
    @Autowired
    private ApproveDetailService approveDetailService;
    @Autowired
    private ApproveAppealService approveAppealService;

    @RequestMapping("/userApproveList")
    public Json userApproveList() {
        Map<String, Object> attributes = new HashMap<>();
        String userId = req.getParameter("userId");

        List<ApproveDetail> approveDetails = approveDetailService.getByBenifitierId(userId);
        attributes.put("approveDetails", approveDetails);

        ApproveCount approveCount = ApproveCount.count(approveDetails);
        attributes.put("approveCount", approveCount);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/detail")
    public Json detail() {
        Map<String, Object> attributes = new HashMap<>();

        String id = req.getParameter("id");

        ApproveDetail approveDetail = approveDetailService.getById(id);
        ApproveAppeal approveAppeal = approveAppealService.getByApproveDetailId(id);

        attributes.put("approveDetail", approveDetail);
        if (approveAppeal != null)
            attributes.put("approveAppeal", approveAppeal);


        return new Json(JsonMessage.SUCCESS, attributes);
    }

}

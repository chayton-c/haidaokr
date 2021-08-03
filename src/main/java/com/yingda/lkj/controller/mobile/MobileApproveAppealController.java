package com.yingda.lkj.controller.mobile;

import com.yingda.lkj.beans.entity.backstage.approve.ApproveAppeal;
import com.yingda.lkj.beans.entity.backstage.approve.ApproveDetail;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.approve.ApproveAppealService;
import com.yingda.lkj.service.backstage.approve.ApproveDetailService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.wechat.enterprise.EnterpriseWeChatMessageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/mobile/mobileApproveAppeal")
@RestController
public class MobileApproveAppealController extends BaseController {
    @Autowired
    private ApproveAppealService approveAppealService;
    @Autowired
    private ApproveDetailService approveDetailService;

    private ApproveAppeal pageApproveAppeal;

    @RequestMapping("/detail")
    public Json detail() {
        Map<String, Object> attributes = new HashMap<>();

        String approveDetailId = req.getParameter("approveDetailId");
        ApproveAppeal approveAppeal = approveAppealService.getByApproveDetailId(approveDetailId);
        ApproveDetail approveDetail = approveDetailService.getById(approveDetailId);

        if (approveAppeal != null)
            attributes.put("approveAppeal", approveAppeal);
        attributes.put("approveDetail", approveDetail);
        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/createAppeal")
    public Json createAppeal() {
        ApproveAppeal approveAppeal = approveAppealService.saveOrUpdate(pageApproveAppeal);
        approveAppealService.sendAppearlCreateMessage(approveAppeal);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/handleAppeal")
    public Json handleAppeal() {
        ApproveAppeal approveAppeal = approveAppealService.saveOrUpdate(pageApproveAppeal);
        approveAppealService.sendAppearlHandleMessage(approveAppeal);
        return new Json(JsonMessage.SUCCESS);
    }

    @ModelAttribute
    public void setPageApproveAppeal(ApproveAppeal pageApproveAppeal) {
        this.pageApproveAppeal = pageApproveAppeal;
    }
}

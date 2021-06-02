package com.yingda.lkj.controller.backstage.salestarget;

import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.beans.entity.backstage.salestarget.SalesTarget;
import com.yingda.lkj.beans.entity.system.User;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.salestarget.SalesTargetService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.wechat.enterprise.EnterpriseWeChatGroupBotClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/backstage/salesTarget")
public class SalesTargetController extends BaseController {

    @Autowired
    private BaseService<SalesTarget> salesTargetBaseService;
    @Autowired
    private SalesTargetService salesTargetService;

    private SalesTarget pageSalesTarget;

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        Map<String, String> conditions = new HashMap<>();

        List<SalesTarget> salesTargets = salesTargetBaseService.getObjcetPagination(
                SalesTarget.class, params, conditions, page.getCurrentPage(), page.getPageSize(), "order by add_time desc"
        );
        attributes.put("salesTargets", salesTargets);
        page.setDataTotal(salesTargetBaseService.getObjectNum(SalesTarget.class, params, conditions));
        attributes.put("page", page);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/info")
    public Json info() {
        Map<String, Object> attributes = new HashMap<>();

        String id = req.getParameter("id");

        SalesTarget salesTarget = StringUtils.isEmpty(id) ? new SalesTarget() : salesTargetService.getById(id);
        attributes.put("salesTarget", salesTarget);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    @RequestMapping("/saveOrUpdate")
    public Json saveOrUpdate() throws CustomException {
        String id = pageSalesTarget.getId();
        if (StringUtils.isEmpty(id)) {
            User user = getUser();
            pageSalesTarget.setExecutorId(user.getId());
            pageSalesTarget.setExecutorName(user.getDisplayName());
        }

        salesTargetService.saveOrUpdate(pageSalesTarget);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/delete")
    public Json delete() {
        String id = req.getParameter("id");
        salesTargetService.delete(id);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/hide")
    public Json hide() {
        String id = req.getParameter("id");
        salesTargetService.hide(id);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/show")
    public Json show() {
        String id = req.getParameter("id");
        salesTargetService.show(id);
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/sendBot")
    public Json sendBot() {
        EnterpriseWeChatGroupBotClient.sendSalesTargetList();
        return new Json(JsonMessage.SUCCESS);
    }

    @ModelAttribute
    public void setPageSalesTarget(SalesTarget pageSalesTarget) {
        this.pageSalesTarget = pageSalesTarget;
    }
}

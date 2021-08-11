package com.yingda.lkj.controller.client;

import com.yingda.lkj.beans.entity.backstage.wechat.EnterpriseWechatDepartment;
import com.yingda.lkj.beans.entity.backstage.wechat.WeChatUser;
import com.yingda.lkj.beans.pojo.enterprisewechat.department.WechatDepartmentAndUserTreeNode;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.wechat.EnterpriseWechatDepartmentService;
import com.yingda.lkj.service.backstage.wechat.WeChatUserService;
import com.yingda.lkj.utils.StreamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/client/enterpriseWechatDepartmentClient")
@RestController
public class EnterpriseWechatDepartmentClientController extends BaseController {

    @Autowired
    private WeChatUserService weChatUserService;
    @Autowired
    private EnterpriseWechatDepartmentService enterpriseWechatDepartmentService;

    @RequestMapping("/loadDepartmentTree")
    public Json loadDepartmentTree() {
        Map<String, Object> attributes = new HashMap<>();

        List<EnterpriseWechatDepartment> enterpriseWechatDepartments = enterpriseWechatDepartmentService.showdown();
        List<WeChatUser> weChatUsers = weChatUserService.showdown();

        List<EnterpriseWechatDepartment> rawRoot = enterpriseWechatDepartmentService.getByParentId(EnterpriseWechatDepartment.ROOT_DEPARTMENT_ID);
        List<WechatDepartmentAndUserTreeNode> root = StreamUtil.getList(rawRoot, WechatDepartmentAndUserTreeNode::new);

        List<WechatDepartmentAndUserTreeNode> wechatDepartmentAndUserTreeNodes =
                WechatDepartmentAndUserTreeNode.convertListToTree(
                        root,
                        enterpriseWechatDepartments,
                        weChatUsers
                );

        attributes.put("wechatDepartmentAndUserTreeNodes", wechatDepartmentAndUserTreeNodes);

        return new Json(JsonMessage.SUCCESS, attributes);
    }
}

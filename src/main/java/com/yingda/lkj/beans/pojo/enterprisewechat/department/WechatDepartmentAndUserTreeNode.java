package com.yingda.lkj.beans.pojo.enterprisewechat.department;

import com.yingda.lkj.beans.Constant;
import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlanPrimaryNode;
import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlanSecondaryNode;
import com.yingda.lkj.beans.entity.backstage.wechat.EnterpriseWechatDepartment;
import com.yingda.lkj.beans.entity.backstage.wechat.WeChatUser;
import com.yingda.lkj.utils.StreamUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WechatDepartmentAndUserTreeNode {

    public static final WechatDepartmentAndUserTreeNode ROOT_NODE = new WechatDepartmentAndUserTreeNode("0");

    private static final byte DEPARTMENT_NODE = 0;
    private static final byte USER_NODE = 1;

    // 这四个都是前端用的
    private String key;
    private String title;
    private String avatar;
    private String icon;

    private String id;
    private String name;
    private byte type;
    private List<WechatDepartmentAndUserTreeNode> children;

    public WechatDepartmentAndUserTreeNode() {

    }

    public WechatDepartmentAndUserTreeNode(String id) {
        this.id = id;
        this.key = id;
    }

    public static List<WechatDepartmentAndUserTreeNode> convertListToTree(
            List<WechatDepartmentAndUserTreeNode> parentNodes,
            List<EnterpriseWechatDepartment> enterpriseWechatDepartments,
            List<WeChatUser> weChatUsers
    ) {
        if (enterpriseWechatDepartments.isEmpty() && weChatUsers.isEmpty()) return parentNodes;

        for (WechatDepartmentAndUserTreeNode parentNode : parentNodes) {
            List<WechatDepartmentAndUserTreeNode> childrenNodes = new ArrayList<>();

            List<WeChatUser> childrenUsers =
                    weChatUsers
                            .stream()
                            .filter(x -> x.getDepartmentId().equals(parentNode.getId()))
                            .collect(Collectors.toList());
            weChatUsers.removeAll(childrenUsers);
            childrenNodes.addAll(StreamUtil.getList(childrenUsers, WechatDepartmentAndUserTreeNode::new));

            List<EnterpriseWechatDepartment> childrenDepartments =
                    enterpriseWechatDepartments
                            .stream()
                            .filter(x -> x.getParentId().equals(parentNode.getId()))
                            .collect(Collectors.toList());
            List<WechatDepartmentAndUserTreeNode> departmentChildrenNodes = StreamUtil.getList(childrenDepartments, WechatDepartmentAndUserTreeNode::new);
            childrenNodes.addAll(departmentChildrenNodes);
            enterpriseWechatDepartments.removeAll(childrenDepartments);

            parentNode.setChildren(childrenNodes);

            convertListToTree(departmentChildrenNodes, enterpriseWechatDepartments, weChatUsers);
        }
        return parentNodes;
    }

    public WechatDepartmentAndUserTreeNode(EnterpriseWechatDepartment enterpriseWechatDepartment) {
        this.key = enterpriseWechatDepartment.getId();
        this.id = this.key;
        this.name = enterpriseWechatDepartment.getName();
        this.title = this.name;
        this.type = DEPARTMENT_NODE;
    }

    public WechatDepartmentAndUserTreeNode(WeChatUser weChatUser) {
        // 注意：为了避免企业微信给我的userId和departmentId重复，key加了个前缀
        this.key = "wechatuser-" + weChatUser.getId(); // 公审霍尔蒂
        this.id = weChatUser.getId();
        this.name = weChatUser.getName();

        this.title = this.name;
        this.avatar = weChatUser.getAvatar();
        this.icon = this.avatar;

        this.type = USER_NODE;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public List<WechatDepartmentAndUserTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<WechatDepartmentAndUserTreeNode> children) {
        this.children = children;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

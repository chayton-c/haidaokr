package com.yingda.lkj.beans.pojo.organization;

import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.utils.StreamUtil;

import java.util.List;

public class OrganizationSelectTreeNode {
    private String key;
    private String value;
    private String title;
    private String label;
    private List<OrganizationSelectTreeNode> children;

    public OrganizationSelectTreeNode(Organization organization) {
        this.key = organization.getId();
        this.value = this.key;
        this.title = organization.getName();
        this.label = this.title;
        List<Organization> organizationList = organization.getOrganizationList();
        if (organizationList != null)
            this.children = StreamUtil.getList(organizationList, OrganizationSelectTreeNode::new);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OrganizationSelectTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationSelectTreeNode> children) {
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

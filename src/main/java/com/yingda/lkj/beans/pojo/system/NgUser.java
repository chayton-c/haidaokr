package com.yingda.lkj.beans.pojo.system;

import com.yingda.lkj.beans.entity.system.Role;
import com.yingda.lkj.beans.entity.system.User;

import java.util.List;
import java.util.ResourceBundle;

/**
 * @author hood  2020/11/11
 */
public class NgUser {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("config");

    private String name;
    private String avatar;
    private String email;
    private String userId;
    private String sectionId;
    private String workshopId;
    private List<String> menuIds;
    private String roleName;

    public NgUser() {
        this.name = "";
        this.avatar = "./assets/tmp/img/avatar.jpg";
        this.email = "";
    }

    public NgUser(User user, Role role) {
        this.userId = user.getId();
        this.sectionId = user.getSectionId();
        this.workshopId = user.getWorkshopId();
        this.name = user.getDisplayName();
        this.avatar = "./assets/tmp/img/avatar.jpg";
        this.email = "";
        this.roleName = role.getName();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<String> menuIds) {
        this.menuIds = menuIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(String workshopId) {
        this.workshopId = workshopId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

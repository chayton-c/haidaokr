package com.yingda.lkj.service.system;

import com.yingda.lkj.beans.entity.system.Permission;
import com.yingda.lkj.beans.entity.system.User;

import java.util.List;

public interface PermissionAuthService {

    boolean hasAccess(String roleId, String permissionId);

    List<Permission> getByUser(User user);

    void addPermissionAuth(String roleId, String permissionId);

    void removePermissionAuth(String roleId, String permissionId);

}

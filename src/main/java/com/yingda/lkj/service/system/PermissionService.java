package com.yingda.lkj.service.system;

import com.yingda.lkj.beans.entity.system.Permission;

import java.util.List;

public interface PermissionService {

    // getAllObjects
    List<Permission> showDown();

    List<Permission> getByMenu(String menuId);
}

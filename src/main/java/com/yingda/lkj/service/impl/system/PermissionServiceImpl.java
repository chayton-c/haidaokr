package com.yingda.lkj.service.impl.system;

import com.yingda.lkj.beans.entity.system.Permission;
import com.yingda.lkj.beans.system.cache.CacheMap;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {


    /**
     * key:id
     */
    private static final Map<String, Permission> PERMISSION_MAP = new CacheMap<>();

    @Autowired
    private BaseDao<Permission> permissionBaseDao;

    @Override
    public List<Permission> showDown() {
        init();
        List<Permission> permissions = new ArrayList<>(new ArrayList<>(PERMISSION_MAP.values()));
        permissions = permissions.stream().sorted(Comparator.comparingInt(Permission::getSeq)).collect(Collectors.toList());
        return permissions;
    }

    @Override
    public List<Permission> getByMenu(String menuId) {
        return new ArrayList<>(showDown()).stream().filter(permission -> permission.getMenuId().equals(menuId)).collect(Collectors.toList());
    }

    private void init() {
        if (!PERMISSION_MAP.isEmpty()) return;

        List<Permission> permissions = permissionBaseDao.find("from Permission");

        PERMISSION_MAP.putAll(permissions.stream().collect(Collectors.toMap(Permission::getId, permission -> permission)));
    }
}

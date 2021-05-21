package com.yingda.lkj.service.impl.system;

import com.yingda.lkj.beans.entity.system.Permission;
import com.yingda.lkj.beans.entity.system.PermissionRole;
import com.yingda.lkj.beans.entity.system.User;
import com.yingda.lkj.beans.system.cache.CacheMap;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.system.PermissionAuthService;
import com.yingda.lkj.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("permissionAuthService")
public class PermissionAuthServiceImpl implements PermissionAuthService {

    // key为 permissionId + roleId 注意不是id
    private static final Map<String, PermissionRole> PERMISSION_ROLE_MAP = new CacheMap<>();

    @Autowired
    private BaseDao<PermissionRole> permissionRoleBaseDao;
    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean hasAccess(String roleId, String permissionId) {
        init();
        return getByRoleAndPermissionId(roleId, permissionId) != null;
    }

    @Override
    public List<Permission> getByUser(User user) {
        init();
        return getByRole(user.getRoleId());
    }

    @Override
    public void addPermissionAuth(String roleId, String permissionId) {
        init();
        if (hasAccess(roleId, permissionId)) return;
        PermissionRole permissionRole = new PermissionRole(roleId, permissionId);
        PERMISSION_ROLE_MAP.put(permissionId + roleId, permissionRole);
        permissionRoleBaseDao.saveOrUpdate(permissionRole);
    }

    @Override
    public void removePermissionAuth(String roleId, String permissionId) {
        init();
        if (!hasAccess(roleId, permissionId)) return;
        PermissionRole permissionRole = getByRoleAndPermissionId(roleId, permissionId);
        PERMISSION_ROLE_MAP.remove(roleId + permissionId);
        permissionRoleBaseDao.delete(permissionRole);
    }

    private PermissionRole getByRoleAndPermissionId(String roleId, String permissionId) {
        return PERMISSION_ROLE_MAP.get(permissionId + roleId);
    }

    private List<Permission> getByRole(String roleId) {
        init();
        List<Permission> permissions = permissionService.showDown();
        return permissions.stream().filter(permission -> hasAccess(roleId, permission.getId())).collect(Collectors.toList());
    }

    private void init() {
        if (!PERMISSION_ROLE_MAP.isEmpty())
            return;
        List<PermissionRole> permissionRoles = permissionRoleBaseDao.find("from PermissionRole");
        PERMISSION_ROLE_MAP.putAll(permissionRoles.stream().collect(Collectors.toMap(x -> x.getPermissionId() + x.getRoleId(), x -> x)));
    }

}

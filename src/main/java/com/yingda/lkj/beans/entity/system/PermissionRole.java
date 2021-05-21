package com.yingda.lkj.beans.entity.system;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

/**
 * 页面功能权限表
 * 用户角色，页面功能中间表
 */
@Entity
@Table(name = "permission_role", schema = "opc_measurement", catalog = "")
public class PermissionRole {
    private String id;
    private String roleId;
    private String permissionId;
    private Timestamp addTime;

    public PermissionRole() {
    }

    public PermissionRole(String roleId, String permissionId) {
        this.id = UUID.randomUUID().toString();
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.addTime = new Timestamp(System.currentTimeMillis());
    }

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "role_id", nullable = false, length = 36)
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "permission_id", nullable = false, length = 36)
    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    @Basic
    @Column(name = "add_time", nullable = true)
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionRole that = (PermissionRole) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(roleId, that.roleId) &&
                Objects.equals(permissionId, that.permissionId) &&
                Objects.equals(addTime, that.addTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleId, permissionId, addTime);
    }
}

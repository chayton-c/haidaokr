package com.yingda.lkj.beans.entity.backstage.equipment;

import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.pojo.location.ContainsLocation;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * 施工设备
 */
@Entity
@Table(name = "equipment")
public class Equipment implements ContainsLocation {
    // status字段
    public static final byte NORMAL = 0;
    public static final byte STOPPED = 1;
    public static final byte ERROR = 2;

    // equipmentType字段
    public static final byte SUPERVISOR = 0;
    public static final byte MACHINE = 1;

    private String id;
    private String imei; // app使用的imei
    private byte equipmentType; // 设备类型
    private String workshopId; // Organization.id
    private String name;
    private byte status; // 设备状态
    private String linkmanPhoneNumber; // 联系人手机号
    private Timestamp addTime;
    private Timestamp updateTime;

    // pageFields
    private String workshopName;
    private List<Location> locations;
    private boolean using;
    private double distanceToOpc;
    private double influenceRadius;
    private String constructionControlPlanId;

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "imei", nullable = false, length = 255)
    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Basic
    @Column(name = "workshop_id", nullable = false, length = 36)
    public String getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(String workshopId) {
        this.workshopId = workshopId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "add_time", nullable = true)
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Basic
    @Column(name = "update_time", nullable = true)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return status == equipment.status &&
                Objects.equals(id, equipment.id) &&
                Objects.equals(imei, equipment.imei) &&
                Objects.equals(workshopId, equipment.workshopId) &&
                Objects.equals(name, equipment.name) &&
                Objects.equals(addTime, equipment.addTime) &&
                Objects.equals(updateTime, equipment.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imei, workshopId, name, status, addTime, updateTime);
    }

    @Transient
    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    @Basic
    @Column(name = "equipment_type", nullable = false)
    public byte getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(byte equipmentType) {
        this.equipmentType = equipmentType;
    }

    @Transient
    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @Transient
    public boolean isUsing() {
        return using;
    }

    public void setUsing(boolean using) {
        this.using = using;
    }

    @Transient
    public double getDistanceToOpc() {
        return distanceToOpc;
    }

    public void setDistanceToOpc(double distanceToOpc) {
        this.distanceToOpc = distanceToOpc;
    }

    @Basic
    @Column(name = "linkman_phone_number", nullable = true, length = 36)
    public String getLinkmanPhoneNumber() {
        return linkmanPhoneNumber;
    }

    public void setLinkmanPhoneNumber(String linkmanPhoneNumber) {
        this.linkmanPhoneNumber = linkmanPhoneNumber;
    }

    @Transient
    public double getInfluenceRadius() {
        return influenceRadius;
    }

    public void setInfluenceRadius(double influenceRadius) {
        this.influenceRadius = influenceRadius;
    }

    @Transient
    public String getConstructionControlPlanId() {
        return constructionControlPlanId;
    }

    public void setConstructionControlPlanId(String constructionControlPlanId) {
        this.constructionControlPlanId = constructionControlPlanId;
    }
}

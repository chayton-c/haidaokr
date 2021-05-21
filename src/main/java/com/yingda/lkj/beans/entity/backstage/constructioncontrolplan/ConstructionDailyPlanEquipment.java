package com.yingda.lkj.beans.entity.backstage.constructioncontrolplan;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

/**
 * 日计划使用设备中间表
 */
@Entity
@Table(name = "construction_daily_plan_equipment", schema = "opc_measurement")
public class ConstructionDailyPlanEquipment {
    private String id;
    private String constructionDailyPlanId;
    private String equipmentId;
    private Timestamp addTime;

    public ConstructionDailyPlanEquipment() {
    }

    public ConstructionDailyPlanEquipment(String constructionDailyPlanId, String equipmentId) {
        this.id = UUID.randomUUID().toString();
        this.constructionDailyPlanId = constructionDailyPlanId;
        this.equipmentId = equipmentId;
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
    @Column(name = "construction_daily_plan_id", nullable = false, length = 36)
    public String getConstructionDailyPlanId() {
        return constructionDailyPlanId;
    }

    public void setConstructionDailyPlanId(String constructionDailyPlanId) {
        this.constructionDailyPlanId = constructionDailyPlanId;
    }

    @Basic
    @Column(name = "equipment_id", nullable = false, length = 36)
    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
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
        ConstructionDailyPlanEquipment that = (ConstructionDailyPlanEquipment) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(constructionDailyPlanId, that.constructionDailyPlanId) &&
                Objects.equals(equipmentId, that.equipmentId) &&
                Objects.equals(addTime, that.addTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, constructionDailyPlanId, equipmentId, addTime);
    }
}

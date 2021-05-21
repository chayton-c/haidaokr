package com.yingda.lkj.beans.entity.backstage.constructioncontrolplan;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

/**
 * 施工计划设备关联表
 */
@Entity
@Table(name = "construction_control_plan_equipment", schema = "opc_measurement", catalog = "")
public class ConstructionControlPlanEquipment {
    private String id;
    private String constructionControlPlanId; // ConstructionControlPlan.id
    private String equipmentId; // Equipment.id
    private Timestamp addTime;
    private double influenceRadius; // 对于该方案，机具的影响范围

    public ConstructionControlPlanEquipment() {
    }

    public ConstructionControlPlanEquipment(String constructionControlPlanId, String equipmentId) {
        this.id = UUID.randomUUID().toString();
        this.constructionControlPlanId = constructionControlPlanId;
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
    @Column(name = "construction_control_plan_id", nullable = false, length = 36)
    public String getConstructionControlPlanId() {
        return constructionControlPlanId;
    }

    public void setConstructionControlPlanId(String constructionControlPlanId) {
        this.constructionControlPlanId = constructionControlPlanId;
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
        ConstructionControlPlanEquipment that = (ConstructionControlPlanEquipment) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(constructionControlPlanId, that.constructionControlPlanId) &&
                Objects.equals(equipmentId, that.equipmentId) &&
                Objects.equals(addTime, that.addTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, constructionControlPlanId, equipmentId, addTime);
    }

    @Basic
    @Column(name = "influence_radius", nullable = false, precision = 0)
    public double getInfluenceRadius() {
        return influenceRadius;
    }

    public void setInfluenceRadius(double influenceRadius) {
        this.influenceRadius = influenceRadius;
    }
}

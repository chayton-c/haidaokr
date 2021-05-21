package com.yingda.lkj.beans.entity.backstage.constructioncontrolplan;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "construction_control_plan_equipment_history", schema = "opc_measurement", catalog = "")
public class ConstructionControlPlanEquipmentHistory {
    private String id;
    private String equipmentId;
    private String constructionControlPlanId;
    private Timestamp startTime; // 历史记录开始时间
    private Timestamp endTime; // 历史记录结束时间
    private Timestamp addTime;

    public ConstructionControlPlanEquipmentHistory() {
    }

    public ConstructionControlPlanEquipmentHistory(String equipmentId, String constructionControlPlanId) {
        this.id = UUID.randomUUID().toString();
        this.equipmentId = equipmentId;
        this.constructionControlPlanId = constructionControlPlanId;
        Timestamp current = this.startTime = new Timestamp(System.currentTimeMillis());
//        this.endTime = endTime;
        this.addTime = current;
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
    @Column(name = "equipment_id", nullable = false, length = 36)
    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
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
    @Column(name = "start_time", nullable = true)
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time", nullable = true)
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
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
        ConstructionControlPlanEquipmentHistory that = (ConstructionControlPlanEquipmentHistory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(equipmentId, that.equipmentId) &&
                Objects.equals(constructionControlPlanId, that.constructionControlPlanId) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(addTime, that.addTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, equipmentId, constructionControlPlanId, startTime, endTime, addTime);
    }
}

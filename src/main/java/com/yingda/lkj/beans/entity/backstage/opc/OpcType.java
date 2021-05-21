package com.yingda.lkj.beans.entity.backstage.opc;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "opc_type", schema = "opc_measurement")
public class OpcType {
    private String id;
    private String name;
    private String color; // 地图上线的颜色
    private Timestamp addTime;
    private Timestamp updateTime;

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        OpcType opcType = (OpcType) o;
        return Objects.equals(id, opcType.id) &&
                Objects.equals(name, opcType.name) &&
                Objects.equals(addTime, opcType.addTime) &&
                Objects.equals(updateTime, opcType.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, addTime, updateTime);
    }

    @Basic
    @Column(name = "color", nullable = false, length = 255)
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

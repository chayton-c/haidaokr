package com.yingda.lkj.beans.entity.backstage.opc;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author hood  2020/12/14
 */
@Entity
@Table(name = "opc_mark_type", schema = "opc_measurement", catalog = "")
public class OpcMarkType {
    private String id;
    private String name;
    private String code;
    private Timestamp addTime;
    private byte linkedList; // 是线性数据，像桥梁，隧道这类数据(opc_mark)的locations字段包含多个location，不是一个
    private byte asABasisOfCutting; // 是断路（地图上标虚线）

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
    @Column(name = "code", nullable = true, length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        OpcMarkType that = (OpcMarkType) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(addTime, that.addTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, addTime);
    }

    @Basic
    @Column(name = "linked_list", nullable = false)
    public byte getLinkedList() {
        return linkedList;
    }

    public void setLinkedList(byte linkedList) {
        this.linkedList = linkedList;
    }

    @Basic
    @Column(name = "as_a_basis_of_cutting", nullable = false)
    public byte getAsABasisOfCutting() {
        return asABasisOfCutting;
    }

    public void setAsABasisOfCutting(byte asABasisOfCutting) {
        this.asABasisOfCutting = asABasisOfCutting;
    }
}

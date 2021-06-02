package com.yingda.lkj.beans.entity.backstage.salestarget;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "sales_target", schema = "opc_measurement", catalog = "")
public class SalesTarget {
    private String id;
    private String shopName;
    private double salesTarget;
    private Double sales;
    private String executorId;
    private String executorName;
    private byte hide;
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
    @Column(name = "shop_name", nullable = false, length = 255)
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Basic
    @Column(name = "sales_target", nullable = false, precision = 0)
    public double getSalesTarget() {
        return salesTarget;
    }

    public void setSalesTarget(double salesTarget) {
        this.salesTarget = salesTarget;
    }

    @Basic
    @Column(name = "sales", nullable = true, precision = 0)
    public Double getSales() {
        return sales;
    }

    public void setSales(Double sales) {
        this.sales = sales;
    }

    @Basic
    @Column(name = "executor_id", nullable = false, length = 36)
    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    @Basic
    @Column(name = "executor_name", nullable = true, length = 255)
    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    @Basic
    @Column(name = "hide", nullable = false)
    public byte getHide() {
        return hide;
    }

    public void setHide(byte hide) {
        this.hide = hide;
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
        SalesTarget that = (SalesTarget) o;
        return Double.compare(that.salesTarget, salesTarget) == 0 && hide == that.hide && Objects.equals(id, that.id) && Objects.equals(shopName, that.shopName) && Objects.equals(sales, that.sales) && Objects.equals(executorId, that.executorId) && Objects.equals(executorName, that.executorName) && Objects.equals(addTime, that.addTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shopName, salesTarget, sales, executorId, executorName, hide, addTime, updateTime);
    }
}

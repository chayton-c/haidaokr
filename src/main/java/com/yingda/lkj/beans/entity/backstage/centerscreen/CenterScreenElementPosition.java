package com.yingda.lkj.beans.entity.backstage.centerscreen;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "center_screen_element_position", schema = "opc_measurement")
public class CenterScreenElementPosition {
    private String id;
    private String railwayLineId;
    private String stationId;
    private String xPosition;
    private String yPosition;
    private Timestamp addTime;

    public CenterScreenElementPosition() {}

    public CenterScreenElementPosition(CenterScreenElementPosition pageCenterScreenElementPosition) {
        this.id = UUID.randomUUID().toString();
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
    @Column(name = "railwayLineId", nullable = true, length = 36)
    public String getRailwayLineId() {
        return railwayLineId;
    }

    public void setRailwayLineId(String railwayLineId) {
        this.railwayLineId = railwayLineId;
    }

    @Basic
    @Column(name = "stationId", nullable = true, length = 36)
    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    @Basic
    @Column(name = "x_position", nullable = true, length = 50)
    public String getxPosition() {
        return xPosition;
    }

    public void setxPosition(String xPosition) {
        this.xPosition = xPosition;
    }

    @Basic
    @Column(name = "y_position", nullable = true, length = 50)
    public String getyPosition() {
        return yPosition;
    }

    public void setyPosition(String yPosition) {
        this.yPosition = yPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CenterScreenElementPosition that = (CenterScreenElementPosition) o;
        return Objects.equals(id, that.id) && Objects.equals(railwayLineId, that.railwayLineId) && Objects.equals(stationId, that.stationId) && Objects.equals(xPosition, that.xPosition) && Objects.equals(yPosition, that.yPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, railwayLineId, stationId, xPosition, yPosition);
    }

    @Basic
    @Column(name = "add_time", nullable = true)
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }
}

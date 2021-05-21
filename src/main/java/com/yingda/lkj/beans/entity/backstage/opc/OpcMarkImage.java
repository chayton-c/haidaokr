package com.yingda.lkj.beans.entity.backstage.opc;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "opc_mark_image", schema = "opc_measurement", catalog = "")
public class OpcMarkImage {
    private String id;
    private String uploadImageId;
    private String opcMarkId;
    private Timestamp addTime;

    // pageFields
    private String url;

    @Id
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "upload_image_id", nullable = false, length = 36)
    public String getUploadImageId() {
        return uploadImageId;
    }

    public void setUploadImageId(String uploadImageId) {
        this.uploadImageId = uploadImageId;
    }

    @Basic
    @Column(name = "opc_mark_id", nullable = false, length = 36)
    public String getOpcMarkId() {
        return opcMarkId;
    }

    public void setOpcMarkId(String opcMarkId) {
        this.opcMarkId = opcMarkId;
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
        OpcMarkImage that = (OpcMarkImage) o;
        return Objects.equals(id, that.id) && Objects.equals(uploadImageId, that.uploadImageId) && Objects.equals(opcMarkId,
                that.opcMarkId) && Objects.equals(addTime, that.addTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uploadImageId, opcMarkId, addTime);
    }

    @Transient
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package com.yingda.lkj.beans.entity.backstage.constructioncontrolplan;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 *
 */
@Entity
@Table(name = "construction_coordinate_plan_upload", schema = "opc_measurement")
public class ConstructionCoordinatePlanUpload {

    // fileType
    public static final byte COOPERATIVE_SCHEME = 0; // 类型-配合方案附件
    public static final byte SAFETY_PROTOCOL = 1; // 类型-安全协议

    private String id;
    private String uploadImageId;
    private String constructionCoordinatePlanId; // ConstructionControlPlan.id
    private byte fileType;
    private Timestamp addTime;

    // pageFields
    private String fileUrl;
    private String fileName;

    public ConstructionCoordinatePlanUpload() {

    }

    public ConstructionCoordinatePlanUpload(String id) {
        this.id = id;
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
    @Column(name = "upload_image_id", nullable = false, length = 36)
    public String getUploadImageId() {
        return uploadImageId;
    }

    public void setUploadImageId(String uploadImageId) {
        this.uploadImageId = uploadImageId;
    }

    @Basic
    @Column(name = "construction_coordinate_plan_id", nullable = false, length = 36)
    public String getConstructionCoordinatePlanId() {
        return constructionCoordinatePlanId;
    }

    public void setConstructionCoordinatePlanId(String constructionCoordinatePlanId) {
        this.constructionCoordinatePlanId = constructionCoordinatePlanId;
    }

    @Basic
    @Column(name = "file_type", nullable = false)
    public byte getFileType() {
        return fileType;
    }

    public void setFileType(byte fileType) {
        this.fileType = fileType;
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
        ConstructionCoordinatePlanUpload that = (ConstructionCoordinatePlanUpload) o;
        return fileType == that.fileType &&
                Objects.equals(id, that.id) &&
                Objects.equals(uploadImageId, that.uploadImageId) &&
                Objects.equals(constructionCoordinatePlanId, that.constructionCoordinatePlanId) &&
                Objects.equals(addTime, that.addTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uploadImageId, constructionCoordinatePlanId, fileType, addTime);
    }


    @Transient
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Transient
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}

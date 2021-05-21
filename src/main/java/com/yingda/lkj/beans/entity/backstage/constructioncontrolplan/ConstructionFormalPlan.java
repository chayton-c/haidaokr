package com.yingda.lkj.beans.entity.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 正式计划表
 */
@Entity
@Table(name = "construction_formal_plan", schema = "opc_measurement")
public class ConstructionFormalPlan {
    // relevanceStatus
    public static final byte PENDING_RELEVANCING = 0; // 待关联
    public static final byte RELEVANCED = 1; // 已关联

    private String id;
    private byte relevanceStatus; // 是否已关联了预计划方案
    private String constructionControlPlanId; // 关联的预计划方案
    private String planType; // 类别
    private String planCode; // 计划编号(excel中的电报号 + excel中的序号)
    private String railwayLineName; // 线路
    private String downriver; // 行别
    private String constructionProject; // 施工项目
    private String workTime; // 施工日期及时间
    private String constructionSite; // 施工地点
    private String constructionContentAndInfluenceArea; // 施工内容
    private String constructionMachinery; // 施工机械
    private String constructDepartment; // 建设单位
    private String constructionDepartmentAndPrincipalName; // 施工单位及负责人
    private String supervisionDepartmentAndPrincipalName; // 监理单位及负责人
    private String equipmentMonitoringDepartmentAndPrincipalName; // 设备监护单位及负责人
    private String auditDepartment; // 审核处室
    private String remarks; // 备注
    private String starRating; // 星级
    private Timestamp addTime;
    // pageFields
    private String constructionControlPlanCode; // 关联方案编号
    private String constructionProjectInfo; // 关联方案施工项目

    public ConstructionFormalPlan() {
    }

    public static ConstructionFormalPlan getInstanceByExcel(List<String> cells, int lineNumber, int sheetNumber) throws CustomException {
        try {
            ConstructionFormalPlan constructionFormalPlan = new ConstructionFormalPlan();

            String telegramNumber = cells.get(16);
            if (StringUtils.isEmpty(telegramNumber))
                throw new CustomException(JsonMessage.PARAM_INVALID, "缺少电报号");


            constructionFormalPlan.setId(UUID.randomUUID().toString());
            constructionFormalPlan.setRelevanceStatus(ConstructionFormalPlan.PENDING_RELEVANCING);
            constructionFormalPlan.setPlanCode(telegramNumber + cells.get(0));
            constructionFormalPlan.setPlanType(cells.get(1));
            constructionFormalPlan.setRailwayLineName(cells.get(2));
            constructionFormalPlan.setDownriver(cells.get(3));
            constructionFormalPlan.setConstructionProject(cells.get(4));
            constructionFormalPlan.setWorkTime(cells.get(5));
            constructionFormalPlan.setConstructionSite(cells.get(6));
            if (cells.get(7).length() > 1000)
                throw new CustomException(JsonMessage.PARAM_INVALID, String.format("第%d页%d行发生错误：施工内容超过1000字", sheetNumber, lineNumber));
            constructionFormalPlan.setConstructionContentAndInfluenceArea(cells.get(7));
            constructionFormalPlan.setConstructionMachinery(cells.get(8));
            constructionFormalPlan.setConstructDepartment(cells.get(9));
            constructionFormalPlan.setConstructionDepartmentAndPrincipalName(cells.get(10));
            constructionFormalPlan.setSupervisionDepartmentAndPrincipalName(cells.get(11));
            constructionFormalPlan.setEquipmentMonitoringDepartmentAndPrincipalName(cells.get(12));
            constructionFormalPlan.setAuditDepartment(cells.get(13));
            constructionFormalPlan.setRemarks(cells.get(14));
            constructionFormalPlan.setStarRating(cells.get(15));

            constructionFormalPlan.setAddTime(new Timestamp(System.currentTimeMillis()));

            return constructionFormalPlan;
        } catch (CustomException customException) {
            throw customException;
        } catch (Exception e) {
            throw new CustomException(JsonMessage.PARAM_INVALID, String.format("第%d页%d行发生错误", sheetNumber, lineNumber));
        }
    }

    public static ConstructionFormalPlan getInstance(ConstructionControlPlan constructionControlPlan) {
        Timestamp current = new Timestamp(System.currentTimeMillis());

        ConstructionFormalPlan constructionFormalPlan = new ConstructionFormalPlan();

        BeanUtils.copyProperties(constructionControlPlan, constructionFormalPlan, "id", "addTime", "updateTime");
        constructionFormalPlan.setId(UUID.randomUUID().toString());

        return constructionFormalPlan;
    }

    @Basic
    @Column(name = "construction_department_and_principal_name", nullable = true, length = 255)
    public String getConstructionDepartmentAndPrincipalName() {
        return constructionDepartmentAndPrincipalName;
    }

    public void setConstructionDepartmentAndPrincipalName(String constructionDepartmentAndPrincipalName) {
        this.constructionDepartmentAndPrincipalName = constructionDepartmentAndPrincipalName;
    }

    @Basic
    @Column(name = "supervision_department_and_principal_name", nullable = true, length = 255)
    public String getSupervisionDepartmentAndPrincipalName() {
        return supervisionDepartmentAndPrincipalName;
    }

    public void setSupervisionDepartmentAndPrincipalName(String supervisionDepartmentAndPrincipalName) {
        this.supervisionDepartmentAndPrincipalName = supervisionDepartmentAndPrincipalName;
    }

    @Basic
    @Column(name = "equipment_monitoring_department_and_principal_name", nullable = true, length = 255)
    public String getEquipmentMonitoringDepartmentAndPrincipalName() {
        return equipmentMonitoringDepartmentAndPrincipalName;
    }

    public void setEquipmentMonitoringDepartmentAndPrincipalName(String equipmentMonitoringDepartmentAndPrincipalName) {
        this.equipmentMonitoringDepartmentAndPrincipalName = equipmentMonitoringDepartmentAndPrincipalName;
    }

    @Basic
    @Column(name = "remarks", nullable = true, length = 255)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Basic
    @Column(name = "star_rating", nullable = true, length = 255)
    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    @Basic
    @Column(name = "audit_department", nullable = true, length = 255)
    public String getAuditDepartment() {
        return auditDepartment;
    }

    public void setAuditDepartment(String auditDepartment) {
        this.auditDepartment = auditDepartment;
    }

    @Basic
    @Column(name = "construction_control_plan_id", nullable = true, length = 36)
    public String getConstructionControlPlanId() {
        return constructionControlPlanId;
    }

    public void setConstructionControlPlanId(String constructionControlPlanId) {
        this.constructionControlPlanId = constructionControlPlanId;
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
    @Column(name = "plan_code", nullable = true, length = 255)
    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    @Basic
    @Column(name = "plan_type", nullable = true, length = 255)
    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    @Basic
    @Column(name = "railway_line_name", nullable = true, length = 255)
    public String getRailwayLineName() {
        return railwayLineName;
    }

    public void setRailwayLineName(String railwayLineName) {
        this.railwayLineName = railwayLineName;
    }

    @Basic
    @Column(name = "downriver", nullable = true, length = 255)
    public String getDownriver() {
        return downriver;
    }

    public void setDownriver(String downriver) {
        this.downriver = downriver;
    }

    @Basic
    @Column(name = "construction_project", nullable = true, length = 255)
    public String getConstructionProject() {
        return constructionProject;
    }

    public void setConstructionProject(String constructionProject) {
        this.constructionProject = constructionProject;
    }

    @Basic
    @Column(name = "construction_site", nullable = true, length = 255)
    public String getConstructionSite() {
        return constructionSite;
    }

    public void setConstructionSite(String constructionSite) {
        this.constructionSite = constructionSite;
    }

    @Basic
    @Column(name = "construction_content_and_influence_area", nullable = true, length = 255)
    public String getConstructionContentAndInfluenceArea() {
        return constructionContentAndInfluenceArea;
    }

    public void setConstructionContentAndInfluenceArea(String constructionContentAndInfluenceArea) {
        this.constructionContentAndInfluenceArea = constructionContentAndInfluenceArea;
    }

    @Basic
    @Column(name = "construction_machinery", nullable = true, length = 1000)
    public String getConstructionMachinery() {
        return constructionMachinery;
    }

    public void setConstructionMachinery(String constructionMachinery) {
        this.constructionMachinery = constructionMachinery;
    }

    @Basic
    @Column(name = "construct_department", nullable = true, length = 255)
    public String getConstructDepartment() {
        return constructDepartment;
    }

    public void setConstructDepartment(String constructDepartment) {
        this.constructDepartment = constructDepartment;
    }

    @Basic
    @Column(name = "add_time", nullable = true)
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Transient
    public String getConstructionControlPlanCode() {
        return constructionControlPlanCode;
    }

    public void setConstructionControlPlanCode(String constructionControlPlanCode) {
        this.constructionControlPlanCode = constructionControlPlanCode;
    }

    @Transient
    public String getConstructionProjectInfo() {
        return constructionProjectInfo;
    }

    public void setConstructionProjectInfo(String constructionProjectInfo) {
        this.constructionProjectInfo = constructionProjectInfo;
    }

    @Basic
    @Column(name = "relevance_status", nullable = false)
    public byte getRelevanceStatus() {
        return relevanceStatus;
    }

    public void setRelevanceStatus(byte relevanceStatus) {
        this.relevanceStatus = relevanceStatus;
    }

    @Basic
    @Column(name = "work_time", nullable = true, length = 255)
    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstructionFormalPlan that = (ConstructionFormalPlan) o;
        return relevanceStatus == that.relevanceStatus &&
                Objects.equals(id, that.id) &&
                Objects.equals(constructionControlPlanId, that.constructionControlPlanId) &&
                Objects.equals(planType, that.planType) &&
                Objects.equals(planCode, that.planCode) &&
                Objects.equals(railwayLineName, that.railwayLineName) &&
                Objects.equals(downriver, that.downriver) &&
                Objects.equals(constructionProject, that.constructionProject) &&
                Objects.equals(constructionSite, that.constructionSite) &&
                Objects.equals(constructionContentAndInfluenceArea, that.constructionContentAndInfluenceArea) &&
                Objects.equals(constructionMachinery, that.constructionMachinery) &&
                Objects.equals(constructDepartment, that.constructDepartment) &&
                Objects.equals(constructionDepartmentAndPrincipalName, that.constructionDepartmentAndPrincipalName) &&
                Objects.equals(supervisionDepartmentAndPrincipalName, that.supervisionDepartmentAndPrincipalName) &&
                Objects.equals(equipmentMonitoringDepartmentAndPrincipalName, that.equipmentMonitoringDepartmentAndPrincipalName) &&
                Objects.equals(remarks, that.remarks) &&
                Objects.equals(starRating, that.starRating) &&
                Objects.equals(auditDepartment, that.auditDepartment) &&
                Objects.equals(addTime, that.addTime) &&
                Objects.equals(workTime, that.workTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, constructionControlPlanId, relevanceStatus, planCode, planType, railwayLineName, downriver,
                constructionProject, constructionSite, constructionContentAndInfluenceArea, constructionMachinery, constructDepartment,
                workTime, constructionDepartmentAndPrincipalName, supervisionDepartmentAndPrincipalName, equipmentMonitoringDepartmentAndPrincipalName, addTime, starRating, auditDepartment, remarks);
    }
}

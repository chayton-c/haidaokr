package com.yingda.lkj.service.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.WarningInfo;
import com.yingda.lkj.beans.exception.CustomException;

import java.util.List;

public interface WarningInfoService {

    WarningInfo getById(String id);

    /**
     * 获取全部预警信息，推送给WebSocket
     * @return
     */
    List<WarningInfo> sendWarningInfoToWebsocket() throws Exception;

    /**
     * 检查设备所在位置,在该设备对应的日计划上是否距离电缆过近,过近的话生成warningInfo记录
     */
    void checkEquipmentOnContructionControlPlan(String equipmentId) throws Exception;

    void processWarningBySendSms(String warningInfoId) throws Exception;
    void processWarningByDoNothing(String warningInfoId);
}

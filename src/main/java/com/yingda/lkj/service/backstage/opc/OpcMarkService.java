package com.yingda.lkj.service.backstage.opc;

import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.beans.entity.backstage.opc.OpcMark;

import java.util.List;

/**
 * @author hood  2020/12/15
 */
public interface OpcMarkService {
    List<OpcMark> getByOpc(String opcId);

    List<OpcMark> getByOpcs(List<Opc> opcs);

    void delete(String id);

    void saveOrUpdate(OpcMark pageOpcMark);
}

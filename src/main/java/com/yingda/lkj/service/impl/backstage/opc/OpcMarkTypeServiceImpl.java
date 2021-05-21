package com.yingda.lkj.service.impl.backstage.opc;

import com.yingda.lkj.beans.entity.backstage.opc.OpcMarkType;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.opc.OpcMarkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("opcMarkTypeService")
public class OpcMarkTypeServiceImpl implements OpcMarkTypeService {

    @Autowired
    private BaseDao<OpcMarkType> opcMarkTypeBaseDao;

    @Override
    public List<OpcMarkType> getAll() {
        return opcMarkTypeBaseDao.find("from OpcMarkType");
    }
}

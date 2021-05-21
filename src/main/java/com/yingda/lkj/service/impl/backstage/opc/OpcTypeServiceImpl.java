package com.yingda.lkj.service.impl.backstage.opc;

import com.yingda.lkj.beans.entity.backstage.opc.OpcType;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.opc.OpcTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("opcTypeService")
public class OpcTypeServiceImpl implements OpcTypeService {

    @Autowired
    private BaseDao<OpcType> opcTypeBaseDao;

    @Override
    public List<OpcType> getAll() {
        return opcTypeBaseDao.find(
                "from OpcType"
        );
    }
}

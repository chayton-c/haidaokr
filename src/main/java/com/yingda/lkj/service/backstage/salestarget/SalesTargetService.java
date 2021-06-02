package com.yingda.lkj.service.backstage.salestarget;

import com.yingda.lkj.beans.Constant;
import com.yingda.lkj.beans.entity.backstage.salestarget.SalesTarget;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

@Service("salesTargetService")
public class SalesTargetService {

    @Autowired
    private BaseDao<SalesTarget> salesTargetBaseDao;

    public SalesTarget getById(String id) {
        return salesTargetBaseDao.get(SalesTarget.class, id);
    }

    public void saveOrUpdate(SalesTarget pageSalesTarget) {
        String id = pageSalesTarget.getId();

        SalesTarget salesTarget = StringUtils.isEmpty(id) ? new SalesTarget() : getById(id);

        BeanUtils.copyProperties(pageSalesTarget, salesTarget, "addTime");
        salesTarget.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        if (StringUtils.isEmpty(id)) {
            salesTarget.setId(UUID.randomUUID().toString());
            salesTarget.setAddTime(new Timestamp(System.currentTimeMillis()));
        }

        salesTargetBaseDao.saveOrUpdate(salesTarget);
    }

    public void show(String id) {
        SalesTarget salesTarget = getById(id);
        if (salesTarget == null) return;

        salesTarget.setHide(Constant.SHOW);
        salesTarget.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        salesTargetBaseDao.saveOrUpdate(salesTarget);
    }

    public void hide(String id) {
        SalesTarget salesTarget = getById(id);
        if (salesTarget == null) return;

        salesTarget.setHide(Constant.HIDE);
        salesTarget.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        salesTargetBaseDao.saveOrUpdate(salesTarget);
    }

    public void delete(String id) {
        salesTargetBaseDao.executeHql(
                "delete from SalesTarget where id = :id",
                Map.of("id", id)
        );
    }
}

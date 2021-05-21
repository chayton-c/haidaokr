package com.yingda.lkj.service.backstage.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlan;
import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.ConstructionControlPlanWorkTime;
import com.yingda.lkj.dao.BaseDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ConstructionControlPlanWorkTimeService {
    @Autowired
    private BaseDao<ConstructionControlPlanWorkTime> constructionControlPlanWorkTimeBaseDao;
    @Autowired
    private ConstructionControlPlanService constructionControlPlanService;

    public ConstructionControlPlanWorkTime getById(String id) {
        return constructionControlPlanWorkTimeBaseDao.get(ConstructionControlPlanWorkTime.class, id);
    }

    public List<ConstructionControlPlanWorkTime> getByConstructionControlPlanId(String constructionControlPlanId) {
        return constructionControlPlanWorkTimeBaseDao.find(
               "FROM ConstructionControlPlanWorkTime WHERE constructionControlPlanId = :constructionControlPlanId",
               Map.of("constructionControlPlanId", constructionControlPlanId)
        );
    }

    public void saveOrUpdate(ConstructionControlPlanWorkTime pageConstructionControlPlanWorkTime) {
        ConstructionControlPlanWorkTime constructionControlPlanWorkTime =
                Optional.ofNullable(getById(pageConstructionControlPlanWorkTime.getId())).orElse(new ConstructionControlPlanWorkTime());

        BeanUtils.copyProperties(pageConstructionControlPlanWorkTime, constructionControlPlanWorkTime, "addTime");
        if (constructionControlPlanWorkTime.getAddTime() == null)
            constructionControlPlanWorkTime.setAddTime(new Timestamp(System.currentTimeMillis()));

        constructionControlPlanWorkTime.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        constructionControlPlanWorkTimeBaseDao.saveOrUpdate(constructionControlPlanWorkTime);
    }

    public void delete(String id) {
        constructionControlPlanWorkTimeBaseDao.executeHql(
                "delete from ConstructionControlPlanWorkTime where id = :id",
                Map.of("id", id)
        );
    }

    public void checkDeleteWhenDestropPage(String constructionControlPlanId) {
        ConstructionControlPlan constructionControlPlan = constructionControlPlanService.getById(constructionControlPlanId);
        if (constructionControlPlan != null) return;

        constructionControlPlanWorkTimeBaseDao.executeHql(
                "delete from ConstructionControlPlanWorkTime where constructionControlPlanId = :constructionControlPlanId",
                Map.of("constructionControlPlanId", constructionControlPlanId)
        );
    }

}

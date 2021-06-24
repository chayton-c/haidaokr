package com.yingda.lkj.service.backstage.actionplan;

import com.yingda.lkj.beans.entity.backstage.actionplan.ActionPlan;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service("actionPlanService")
public class ActionPlanService {

    @Autowired
    private BaseDao<ActionPlan> actionPlanBaseDao;

    public ActionPlan getById(String id) {
        return actionPlanBaseDao.get(ActionPlan.class, id);
    }

    public ActionPlan saveOrUpdate(ActionPlan pageActionPlan) {
        String id = pageActionPlan.getId();
        return StringUtils.isEmpty(id) ? save(pageActionPlan) : update(pageActionPlan);
    }

    private ActionPlan save(ActionPlan pageActionPlan) {
        pageActionPlan.setId(UUID.randomUUID().toString());
        pageActionPlan.setActionPlanGroupId(UUID.randomUUID().toString());

        if (pageActionPlan.getAddTime() == null)
            pageActionPlan.setAddTime(new Timestamp(System.currentTimeMillis()));

        pageActionPlan.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        pageActionPlan.setSubmitStatus(ActionPlan.PENDING_SUBMIT);
        pageActionPlan.setFinishedStatus(ActionPlan.PENDING_START);
        pageActionPlan.setAbolishedStatus(ActionPlan.ACTING);
        pageActionPlan.setHistoricalStatus(ActionPlan.NOT_HISTORICAL);

        actionPlanBaseDao.saveOrUpdate(pageActionPlan);
        return pageActionPlan;
    }

    /**
     * 修改时，为了保留历史记录，会新建一条全新的计划，并将旧的历史状态（historicalStatus）置为HISTORICAL
     * @param pageActionPlan
     */
    private ActionPlan update(ActionPlan pageActionPlan) {
        Timestamp current = new Timestamp(System.currentTimeMillis());

        ActionPlan actionPlan = new ActionPlan();
        BeanUtils.copyProperties(pageActionPlan, actionPlan);
        actionPlan.setUpdateTime(current);
        actionPlanBaseDao.saveOrUpdate(actionPlan);

        return actionPlan;
    }

    public void submit(String actionPlanId) {
        ActionPlan actionPlan = getById(actionPlanId);
        actionPlan.setSubmitStatus(ActionPlan.SUBMITTED);
        actionPlanBaseDao.saveOrUpdate(actionPlan);
    }

}

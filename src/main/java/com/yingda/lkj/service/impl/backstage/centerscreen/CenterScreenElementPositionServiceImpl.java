package com.yingda.lkj.service.impl.backstage.centerscreen;

import com.yingda.lkj.beans.entity.backstage.centerscreen.CenterScreenElementPosition;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.centerscreen.CenterScreenElementPositionService;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service("centerScreenElementPositionService")
public class CenterScreenElementPositionServiceImpl  implements CenterScreenElementPositionService {

    @Autowired
    private BaseDao<CenterScreenElementPosition> centerScreenElementPositionBaseDao;

    @Override
    public CenterScreenElementPosition getByStationId(String stationId) {
        return centerScreenElementPositionBaseDao.get("from CenterScreenElementPosition where stationId = :stationId",Map.of("stationId",stationId));
    }

    @Override
    public void saveOrUpdate(CenterScreenElementPosition pageCenterScreenElementPosition) {
        String stationId = pageCenterScreenElementPosition.getStationId();
        // 查询上一个使用该车站的数据，如果有，删除
        CenterScreenElementPosition previousCenterScreenElementPosition = getByStationId(stationId);

        if (previousCenterScreenElementPosition != null) delete(previousCenterScreenElementPosition);

        CenterScreenElementPosition centerScreenElementPosition = new CenterScreenElementPosition();
        BeanUtils.copyProperties(pageCenterScreenElementPosition, centerScreenElementPosition, "id", "addTime");
        // 新增时，添加id和addTime
        if (StringUtils.isEmpty(centerScreenElementPosition.getId())) {
            centerScreenElementPosition.setId(UUID.randomUUID().toString());
            centerScreenElementPosition.setAddTime(new Timestamp(System.currentTimeMillis()));
        }
        centerScreenElementPosition.setxPosition(centerScreenElementPosition.getxPosition() + '%');
        centerScreenElementPosition.setyPosition(centerScreenElementPosition.getyPosition() + '%');

        centerScreenElementPositionBaseDao.saveOrUpdate(centerScreenElementPosition);
    }

    @Override
    public void delete(CenterScreenElementPosition centerScreenElementPosition) {
        centerScreenElementPositionBaseDao.delete(centerScreenElementPosition);
    }

    @Override
    public List<CenterScreenElementPosition> getByStationIds(List<String> stationIds) {
        return centerScreenElementPositionBaseDao.find("from CenterScreenElementPosition where stationId in :stationIds",Map.of("stationIds",stationIds));
    }
}

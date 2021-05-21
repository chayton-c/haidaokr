package com.yingda.lkj.service.impl.backstage.opc;

import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.beans.entity.backstage.opc.OpcMark;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.opc.OpcMarkService;
import com.yingda.lkj.utils.StreamUtil;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hood  2020/12/15
 */
@Service("opcMarkService")
public class OpcMarkServiceImpl implements OpcMarkService {

    @Autowired
    private BaseDao<OpcMark> opcMarkBaseDao;
    @Autowired
    private LocationService locationService;

    @Override
    public List<OpcMark> getByOpc(String opcId) {
        return opcMarkBaseDao.find(
                "from OpcMark where opcId = :opcId",
                Map.of("opcId", opcId)
        );
    }

    @Override
    public List<OpcMark> getByOpcs(List<Opc> opcs) {
        if (opcs.isEmpty())
            return new ArrayList<>();

        List<String> opcIds = StreamUtil.getList(opcs, Opc::getId);
        return opcMarkBaseDao.find(
                "from OpcMark where opcId in :opcIds",
                Map.of("opcIds", opcIds)
        );
    }

    @Override
    public void delete(String id) {
        opcMarkBaseDao.executeHql(
                "delete from OpcMark where id = :id",
                Map.of("id", id)
        );

        locationService.deleteByDataId(id);
    }

    @Override
    public void saveOrUpdate(OpcMark pageOpcMark) {
        String id = pageOpcMark.getId();

        OpcMark opcMark = StringUtils.isEmpty(id) ? new OpcMark() : opcMarkBaseDao.get(OpcMark.class, id);

        // 注意，前端页面没有添加逻辑，这个接口其实只能用来修改
        BeanUtils.copyProperties(pageOpcMark, opcMark, "id", "opcId", "seq", "code", "nextOpcMarkId", "addTime");
        opcMark.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        opcMarkBaseDao.saveOrUpdate(opcMark);
    }
}

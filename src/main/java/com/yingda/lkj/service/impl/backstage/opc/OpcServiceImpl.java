package com.yingda.lkj.service.impl.backstage.opc;

import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.beans.entity.backstage.opc.OpcMark;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.location.LocationService;
import com.yingda.lkj.service.backstage.opc.OpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hood  2020/12/15
 */
@Service("opcService")
public class OpcServiceImpl implements OpcService {

    @Autowired
    private BaseDao<Opc> opcBaseDao;
    @Autowired
    private LocationService locationService;

    @Override
    public Opc getById(String id) {
        return opcBaseDao.get(Opc.class, id);
    }

    @Override
    public List<Opc> getByIds(List<String> ids) {
        return opcBaseDao.find(
                "from Opc where id in :ids",
                Map.of("ids", ids)
        );
    }

    @Override
    public List<Opc> getByStations(String leftStationId, String rightStationId) {
        return opcBaseDao.find(
                "from Opc where leftStationId = :leftStationId AND rightStationId = :rightStationId",
                Map.of("leftStationId", leftStationId, "rightStationId", rightStationId)
        );
    }

    @Override
    public List<Opc> getLineDataByStationId(String stationId) {
        return opcBaseDao.find(
                "from Opc where (leftStationId = :stationId OR rightStationId = :stationId)",
                Map.of("stationId", stationId)
        );
    }

    @Override
    public List<Opc> getOpcByStationId(String stationId) {
        String sql = """
                SELECT
                    opc.id,
                    opc.`name`,
                    opc_type.`name` AS opcTypeName
                FROM
                	opc
                	LEFT JOIN opc_type ON opc.opc_type_id = opc_type.id 
                WHERE
                	opc.left_station_id = :stationId 
                	OR opc.right_station_id = :stationId
                """;
        return opcBaseDao.findSQL(sql, Map.of("stationId", stationId), Opc.class);
    }

    @Override
    public List<Opc> getLineDataByStationIds(List<String> stationIds) {
        return opcBaseDao.find(
                "from Opc where (leftStationId in :stationIds OR rightStationId in :stationIds)",
                Map.of("stationIds", stationIds)
        );
    }

    @Override
    public void cutOpcLocationsByDistance(Opc opc, List<OpcMark> opcMarks, double startDistance, double endDistance) {
        // 防止有精神病反着填
        if (startDistance > endDistance) {
            double cache = startDistance;
            startDistance = endDistance;
            endDistance = cache;
        }

        if (opcMarks == null || opcMarks.size() <= 0) return;
        // 标识按线缆标识排序，同时筛掉没有公里标的标识
        opcMarks = opcMarks.stream().filter(opcMark -> opcMark.getKilometerMark() != null).sorted(Comparator.comparingDouble(OpcMark::getKilometerMark)).collect(Collectors.toList());

        if (opcMarks.size() <= 0) return;

        OpcMark startOpcMark = null;
        OpcMark endOpcMark = null;
        // 按照距离截取线缆标
        for (int i = 0; i < opcMarks.size(); i++) {
            OpcMark opcMark = opcMarks.get(i);
            double kilometerMark = opcMark.getKilometerMark();

            if (kilometerMark <= startDistance) // 如果起始点前还有点，往前额外取一个，尽量让光缆线长一点
                startOpcMark = i > 0 ? opcMarks.get(i - 1) : opcMark;

            if (endOpcMark == null && kilometerMark >= endDistance) // 同理，如果终止点后还有点，往后额外取一个
                endOpcMark = i < opcMarks.size() - 1 ? opcMarks.get(i + 1) : opcMark;
        }
        if (endOpcMark == null) endOpcMark = opcMarks.get(opcMarks.size() - 1);

        List<Location> locations = opc.getLocations();
        List<Location> cutLocations = new ArrayList<>();

        // startOpcMark非空时，找到电缆上与startOpcMark经纬度相同的点作为起始点，endOpcMark经纬度相同的点作为终点截取光电缆
        // startOpcMark为空时只使用endOpcMark截取，endOpcMark时同理，都为空时不截取
        boolean fillIntoCutLocations = false;
        for (Location location : locations) {
            if (startOpcMark == null || locationService.samePoint(location, startOpcMark.getLocations().get(0)))
                fillIntoCutLocations = true;

            if (fillIntoCutLocations)
                cutLocations.add(location);

            if (locationService.samePoint(location, endOpcMark.getLocations().get(0)))
                break;
        }

        opc.setLocations(cutLocations);
    }
}

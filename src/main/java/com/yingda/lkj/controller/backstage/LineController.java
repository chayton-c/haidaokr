package com.yingda.lkj.controller.backstage;

import com.yingda.lkj.beans.entity.backstage.line.RailwayLine;
import com.yingda.lkj.beans.pojo.line.LineSelectTreeNode;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.line.RailwayLineService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 线路管理
 *
 * @author hood  2020/1/2
 */
@RequestMapping("/backstage/line")
@Controller
public class LineController extends BaseController {

    @Autowired
    private BaseService<RailwayLine> railwayLineBaseService;
    @Autowired
    private RailwayLineService railwayLineService;

    private RailwayLine pageRailwayLine;

    @ModelAttribute
    public void setPageRailwayLine(RailwayLine pageRailwayLine) {
        this.pageRailwayLine = pageRailwayLine;
    }

    /**
     * 添加/修改线路
     */
    @RequestMapping("/railwayLine/add")
    @ResponseBody
    public Json addRailyway() throws Exception {
        String id = pageRailwayLine.getId();
        if (StringUtils.isEmpty(id)) {
            pageRailwayLine.setId(UUID.randomUUID().toString());
            pageRailwayLine.setAddTime(current());
        }

        try {
            pageRailwayLine.setUpdateTime(current());
            railwayLineBaseService.saveOrUpdate(pageRailwayLine);
        } catch (JpaSystemException e) {
            String errorMsg = e.getCause().getCause().getCause().getMessage();
            return new Json(JsonMessage.PARAM_INVALID, errorMsg);
        }

        return new Json(JsonMessage.SUCCESS);
    }

    /**
     * 删除线路
     */
    @RequestMapping("/railwayLine/delete")
    @ResponseBody
    public Json deleteRailway(String id) {
        railwayLineService.deleteRailwayLines(Collections.singletonList(id));
        return new Json(JsonMessage.SUCCESS);
    }

    @RequestMapping("/initSelectTrees")
    @ResponseBody
    public Json initSelectTrees() {
        Map<String, Object> attributes = new HashMap<>();

        List<LineSelectTreeNode> lineSelectTreeNodes = railwayLineService.initLineSelectTreeNode();
        attributes.put("lineSelectTreeNodes", lineSelectTreeNodes);

        return new Json(JsonMessage.SUCCESS, attributes);
    }
}

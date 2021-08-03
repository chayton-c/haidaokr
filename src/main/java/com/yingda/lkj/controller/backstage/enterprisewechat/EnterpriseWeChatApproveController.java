package com.yingda.lkj.controller.backstage.enterprisewechat;

import com.yingda.lkj.beans.entity.backstage.approve.ApproveDetail;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.approve.ApproveCount;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.backstage.approve.ApproveDetailService;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.wechat.enterprise.EnterpriseWeChatApproveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/backstage/enterpriseWeChatApprove")
@RestController
public class EnterpriseWeChatApproveController extends BaseController {

    @Autowired
    private BaseService<ApproveDetail> approveDetailBaseService;
    @Autowired
    private BaseService<ApproveCount> approveCountBaseService;

    @RequestMapping("/getList")
    public Json getList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String codeOrBinifitierName = req.getParameter("codeOrBinifitierName");
        String startTimeStr = req.getParameter("startTimeStr");
        String endTimeStr = req.getParameter("endTimeStr");

        String sql = """
                SELECT
                    approve_detail.*
                FROM
                    approve_detail
                WHERE
                    1 = 1
                """;
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.isNotEmpty(codeOrBinifitierName)) {
            sql += "AND (approve_detail.code like :codeOrBinifitierName or approve_detail.benifitier_name like :codeOrBinifitierName)\n";
            params.put("codeOrBinifitierName", "%" + codeOrBinifitierName + "%");
        }
        if (StringUtils.isNotEmpty(startTimeStr)) {
            Timestamp startTime = new Timestamp(Long.parseLong(startTimeStr));
            sql += "AND approve_detail.approve_time > :startTime\n";
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotEmpty(endTimeStr)) {
            Timestamp endTime = new Timestamp(Long.parseLong(endTimeStr));
            sql += "AND approve_detail.approve_time < :endTime\n";
            params.put("endTime", endTime);
        }
        sql += "order by approve_detail.update_time desc";
        List<ApproveDetail> approveDetails = approveDetailBaseService.findSQL(
                sql, params, ApproveDetail.class, page.getCurrentPage(), page.getPageSize()
        );
        attributes.put("approveDetails", approveDetails);
        setObjectNum(sql, params);
        attributes.put("page", page);

        return new Json(JsonMessage.SUCCESS, attributes);
    }


    @RequestMapping("/getSumList")
    public Json getSumList() throws Exception {
        Map<String, Object> attributes = new HashMap<>();

        String codeOrBinifitierName = req.getParameter("codeOrBinifitierName");
        String startTimeStr = req.getParameter("startTimeStr");
        String endTimeStr = req.getParameter("endTimeStr");


        String sql = """
                SELECT
                    approve_detail.benifitier_id as id,
                    approve_detail.benifitier_id as userId,
                    approve_detail.benifitier_name as userName,
                    SUM(approve_detail.money) as total,
                    COUNT(approve_detail.id) as count
                FROM
                    approve_detail
                WHERE
                    1 = 1
                """;

        Map<String, Object> params = new HashMap<>();

        if (StringUtils.isNotEmpty(codeOrBinifitierName)) {
            sql += "AND (approve_detail.code like :codeOrBinifitierName or approve_detail.binifitier_name like :codeOrBinifitierName)\n";
            params.put("codeOrBinifitierName", "%s" + codeOrBinifitierName + "%");
        }
        if (StringUtils.isNotEmpty(startTimeStr)) {
            Timestamp startTime = new Timestamp(Long.parseLong(startTimeStr));
            sql += "AND approve_detail.approve_time > :startTime\n";
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotEmpty(endTimeStr)) {
            Timestamp endTime = new Timestamp(Long.parseLong(endTimeStr));
            sql += "AND approve_detail.approve_time < :endTime\n";
            params.put("endTime", endTime);
        }
        sql += """
                GROUP BY
                    approve_detail.benifitier_id
               """;

        List<ApproveCount> approveDetails = approveCountBaseService.findSQL(
                sql, params, ApproveCount.class, page.getCurrentPage(), page.getPageSize()
        );
        attributes.put("approveDetails", approveDetails);
        setObjectNum(sql, params);
        attributes.put("page", page);


        return new Json(JsonMessage.SUCCESS, attributes);
    }


}

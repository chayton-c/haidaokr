package com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo;

import com.yingda.lkj.beans.entity.backstage.constructioncontrolplan.WarningInfo;
import com.yingda.lkj.beans.enums.constructioncontrolplan.WarnLevel;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.date.DateUtil;

import java.sql.Timestamp;
import java.util.Arrays;

public enum ApproveValueType {

    TABLE("Table-1626613013907", "Table", "奖罚明细"),
    ADD_TIME("item-1494249113679", "Date", "发生日期"),
    REWARD_USER("Contact-1626613020860", "Contact", "被奖罚人"),
    REWARD_AMOUNT("Money-1626613027526", "Money", "奖罚金额"),
    REASON("item-1494249039034", "Textarea", "奖罚所依据的制度规范"),
    REMARK("item-1494249126248", "Textarea", "奖罚内容描述"),
    ;

    public final String id;
    public final String control;
    public final String title;

    ApproveValueType(String id, String control, String title) {
        this.id = id;
        this.control = control;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getControl() {
        return control;
    }

    public String getTitle() {
        return title;
    }
}

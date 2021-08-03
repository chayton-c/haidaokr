package com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo.value;

import com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo.ApproveContent;
import com.yingda.lkj.beans.pojo.enterprisewechat.approve.ApproveRewardDetail;
import com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo.ApproveValue;
import com.yingda.lkj.beans.pojo.enterprisewechat.approvecreatepojo.ApproveValueType;

import java.util.ArrayList;
import java.util.List;

public class ApproveValueTable {
    private List<ApproveContent> list;

    public ApproveValueTable() {
    }

    public ApproveValueTable(ApproveRewardDetail approveRewardDetail) {
        List<ApproveContent> approveContents = new ArrayList<>();
        Double amount = approveRewardDetail.getAmount();
        approveContents.add(new ApproveContent(ApproveValueType.REWARD_AMOUNT, new ApproveValue(amount + "")));

        approveContents.add(new ApproveContent(ApproveValueType.REWARD_USER, new ApproveValue(approveRewardDetail)));
        this.list = approveContents;
    }

    public List<ApproveContent> getList() {
        return list;
    }

    public void setList(List<ApproveContent> list) {
        this.list = list;
    }
}

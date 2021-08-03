package com.yingda.lkj.beans.pojo.approve;

import com.yingda.lkj.beans.entity.backstage.approve.ApproveDetail;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public class ApproveCount {
    private String id;
    private String userId;
    private String userName;
    private double total;
    private BigInteger count;

    public static ApproveCount count(List<ApproveDetail> approveDetails) {
        if (approveDetails.isEmpty()) return null;

        double sum = 0;
        int count = 0;
        for (ApproveDetail approveDetail : approveDetails) {
            sum += approveDetail.getMoney();
            count++;
        }

        ApproveDetail dummy = approveDetails.get(0);

        ApproveCount approveCount = new ApproveCount();

        approveCount.setId(UUID.randomUUID().toString());
        approveCount.setUserId(dummy.getBenifitierId());
        approveCount.setUserName(dummy.getBenifitierName());
        approveCount.setTotal(sum);
        approveCount.setCount(BigInteger.valueOf(count));
        return approveCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

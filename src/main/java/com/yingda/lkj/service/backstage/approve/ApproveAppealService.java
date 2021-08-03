package com.yingda.lkj.service.backstage.approve;

import com.yingda.lkj.beans.Constant;
import com.yingda.lkj.beans.entity.backstage.approve.ApproveAppeal;
import com.yingda.lkj.beans.entity.backstage.approve.ApproveDetail;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.wechat.enterprise.EnterpriseWeChatMessageClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("approveAppealService")
public class ApproveAppealService {

    private static final String APPEAL_HANDLE_REDIRECT_URL = Constant.DOMAIN + "/#/passport/login?tokenUserName=a&mobileRedirectUrl=/approve-mobile/appeal-handle";

    @Autowired
    private BaseDao<ApproveAppeal> approveAppealBaseDao;
    @Autowired
    private ApproveDetailService approveDetailService;

    public ApproveAppeal getById(String id) {
        return approveAppealBaseDao.get(ApproveAppeal.class, id);
    }

    public ApproveAppeal saveOrUpdate(ApproveAppeal pageApproveAppeal) {
        String id = pageApproveAppeal.getId();

        ApproveAppeal approveAppeal = StringUtils.isNotEmpty(id) ? getById(id) : new ApproveAppeal();
        BeanUtils.copyProperties(pageApproveAppeal, approveAppeal);

        if (StringUtils.isEmpty(id))
            approveAppeal.setId(UUID.randomUUID().toString());

        if (approveAppeal.getAddTime() == null)
            approveAppeal.setAddTime(new Timestamp(System.currentTimeMillis()));

        approveAppeal.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        approveAppealBaseDao.saveOrUpdate(approveAppeal);
        return approveAppeal;
    }

    public ApproveAppeal getByApproveDetailId(String approveDetailId) {
        return approveAppealBaseDao.get(
                "from ApproveAppeal where approveDetailId = :approveDetailId",
                Map.of("approveDetailId", approveDetailId)
        );
    }

    public void sendAppearlCreateMessage(ApproveAppeal approveAppeal) {
        ApproveDetail approveDetail = approveDetailService.getById(approveAppeal.getApproveDetailId());

        String title = approveDetail.getName() + "申诉";
        String description = String.format(
                """
                审批类型：%s
                审批单号：%s
                提交用户：%s
                申请说明：%s
                """,
                approveDetail.getName(),
                approveDetail.getCode(),
                approveDetail.getBenifitierName(),
                approveAppeal.getReason()
        );

        EnterpriseWeChatMessageClient.sendLink(
                List.of(approveDetail.getApplyerId()),
                title,
                description,
                APPEAL_HANDLE_REDIRECT_URL + "&approveDetailId=" + approveDetail.getId()
        );
    }

    public void sendAppearlHandleMessage(ApproveAppeal approveAppeal) {
        ApproveDetail approveDetail = approveDetailService.getById(approveAppeal.getApproveDetailId());

        String title = approveDetail.getName() + "申诉已处理";
        String description = String.format(
                """
                审批编号：%s
                申请说明：%s
                处理意见：%s
                """,
                approveDetail.getCode(),
                approveAppeal.getReason(),
                approveAppeal.getHandlingOpinions()
        );

        EnterpriseWeChatMessageClient.sendLink(
                List.of(approveDetail.getBenifitierId()),
                title,
                description,
                ApproveDetailService.REWARD_DETAIL_REDIRECT_URL + "&id=" + approveDetail.getId()
        );
    }

}

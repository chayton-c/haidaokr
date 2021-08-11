package com.yingda.lkj.service.backstage.approve;

import com.yingda.lkj.beans.Constant;
import com.yingda.lkj.beans.entity.backstage.approve.ApproveDetail;
import com.yingda.lkj.beans.entity.backstage.wechat.WeChatUser;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.approve.ApproveCount;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.wechat.WeChatUserService;
import com.yingda.lkj.utils.wechat.enterprise.EnterpriseWeChatMessageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("approveDetailService")
public class ApproveDetailService {

    public static final String REWARD_DETAIL_REDIRECT_URL = Constant.DOMAIN + "/#/passport/login?tokenUserName=a&mobileRedirectUrl=/approve-mobile/user-detail";
    public static final String REWARD_LIST_REDIRECT_URL = Constant.DOMAIN + "/#/passport/login?tokenUserName=a&mobileRedirectUrl=/approve-mobile/appeal-user-list";

    @Autowired
    private BaseDao<ApproveDetail> approveDetailBaseDao;
    @Autowired
    private WeChatUserService weChatUserService;

    public ApproveDetail getById(String id) {
        return approveDetailBaseDao.get(ApproveDetail.class, id);
    }

    public List<ApproveDetail> getByBenifitierId(String benifitierId) {
        return approveDetailBaseDao.find(
                "from ApproveDetail where benifitierId = :benifitierId",
                Map.of("benifitierId", benifitierId)
        );
    }

    public void saveOrUpdate(List<ApproveDetail> approveDetails) throws CustomException {
        // 提交人姓名要额外查询
        for (ApproveDetail approveDetail : approveDetails) {
            String applyerId = approveDetail.getApplyerId();
            WeChatUser applyer = weChatUserService.getById(applyerId);
            approveDetail.setApplyerName(String.format(
                    "%s (%s)",
                    applyer.getName(),
                    applyer.getPosition()
            ));
        }

        approveDetailBaseDao.bulkInsert(approveDetails);
//        sendRewardMessage(approveDetails);
    }

    public ApproveDetail getByCode(String code) {
        return approveDetailBaseDao.get(
                "from ApproveDetail where code = :code",
                Map.of("code", code)
        );
    }

    public static void main(String[] args) {

    }

    public void sendRewardMessage(List<ApproveDetail> approveDetails) {
        for (ApproveDetail approveDetail : approveDetails) {
            String title = "奖罚明细确认";
            String description = String.format(
                    """
                    编号：%s
                    奖罚金额：%f
                    提交人：%s
                    """,
                    approveDetail.getCode(),
                    approveDetail.getMoney(),
                    approveDetail.getApplyerName()
            );

            EnterpriseWeChatMessageClient.sendLink(
                    List.of(approveDetail.getBenifitierId()),
                    title,
                    description,
                    REWARD_DETAIL_REDIRECT_URL + "&id=" + approveDetail.getId()
            );
        }
    }

    /**
     * 发送月底统计消息
     */
    public void sendApproveCountMessage() {
        String userId = "00003922";
        List<ApproveDetail> approveDetails = getByBenifitierId(userId);
        ApproveCount count = ApproveCount.count(approveDetails);


        String title = "本月奖罚统计";
        assert count != null;
        String description = String.format(
                """
                奖罚笔数：%d
                奖罚共计：%.2f元
                """,
                count.getCount().intValue(),
                count.getTotal()
        );

        EnterpriseWeChatMessageClient.sendLink(
                List.of(userId),
                title,
                description,
                REWARD_LIST_REDIRECT_URL + "&userId=" + userId
        );
    }

}

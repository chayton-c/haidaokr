package com.yingda.lkj.service.backstage.wechat;

import com.yingda.lkj.beans.entity.backstage.wechat.WeChatUser;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.enterprisewechat.user.WeChatUserResponse;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.utils.wechat.enterprise.EnterpriseWeChatUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("weChatUserService")
public class WeChatUserService {

    @Autowired
    private BaseDao<WeChatUser> weChatUserBaseDao;

    /**
     * 先从服务器获取，获取不到，从微信拿
     */
    public WeChatUser getById(String id) throws CustomException {
        WeChatUser weChatUser = weChatUserBaseDao.get(WeChatUser.class, id);
        if (weChatUser == null)
            weChatUser = getByWeChat(id);

        return weChatUser;
    }

    private WeChatUser getByWeChat(String id) throws CustomException {
        WeChatUserResponse weChatUserResponse = EnterpriseWeChatUserClient.getApproveDetail(id);
        WeChatUser weChatUser = WeChatUser.createByResponse(weChatUserResponse);
        weChatUserBaseDao.saveOrUpdate(weChatUser);
        return weChatUser;
    }

}

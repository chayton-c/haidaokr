package com.yingda.lkj.service.backstage.wechat;

import com.yingda.lkj.beans.entity.backstage.wechat.WeChatUser;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.enterprisewechat.user.WeChatUserResponse;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.utils.wechat.enterprise.EnterpriseWeChatUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
            weChatUser = getByUserIdFromWechat(id);

        return weChatUser;
    }

    public List<WeChatUser> getByDepartmentId(String departmentId) {
        return weChatUserBaseDao.find(
                "from WeChatUser where departmentId = :departmentId",
                Map.of("departmentId", departmentId)
        );
    }

    public void saveByUserIdsFromWechat(List<String> userIds) throws CustomException {
        for (String userId : userIds) {
            getByUserIdFromWechat(userId);
        }
    }

    public List<WeChatUser> showdown() {
        return weChatUserBaseDao.find("from WeChatUser");
    }

    private WeChatUser getByUserIdFromWechat(String id) throws CustomException {
        WeChatUserResponse weChatUserResponse = EnterpriseWeChatUserClient.getUserDetail(id);
        WeChatUser weChatUser = WeChatUser.createByResponse(weChatUserResponse);
        weChatUserBaseDao.saveOrUpdate(weChatUser);
        return weChatUser;
    }

}

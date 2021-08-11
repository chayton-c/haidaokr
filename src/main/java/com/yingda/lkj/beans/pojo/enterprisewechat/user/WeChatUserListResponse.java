package com.yingda.lkj.beans.pojo.enterprisewechat.user;

import java.util.List;

public class WeChatUserListResponse {
    private List<WeChatUserResponse> userlist;

    public List<WeChatUserResponse> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<WeChatUserResponse> userlist) {
        this.userlist = userlist;
    }
}

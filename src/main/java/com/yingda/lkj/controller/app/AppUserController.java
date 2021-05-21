package com.yingda.lkj.controller.app;

import com.yingda.lkj.beans.entity.system.User;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author hood  2020/2/17
 */
@Controller
@RequestMapping("/app/user")
public class AppUserController extends BaseController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public Json login(String username, String password) {
        User user = userService.getUserByUserName(username);
        if (user == null)
            return new Json(JsonMessage.AUTH_ERROR, "找不到该用户");

        String realPassword = user.getPassword();

        if (!realPassword.equals(password))
            return new Json(JsonMessage.AUTH_ERROR, "密码错误");

        return new Json(JsonMessage.SUCCESS, Map.of("userName", user.getUserName(), "displayName", user.getDisplayName(), "nfcAdministrator", 1), user.getId());
    }
}

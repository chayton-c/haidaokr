package com.yingda.lkj.controller.app.init;

import com.yingda.lkj.beans.entity.system.User;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/init/user")
public class InitUserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("")
    public Json getList() {
        Map<String, Object> attributes = new HashMap<>();

        List<User> users = userService.getAll();
        for (User user : users) {
            String passwordMd5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(passwordMd5);
        }
        attributes.put("users", users);

        return new Json(JsonMessage.SUCCESS, attributes);
    }

    public static void main(String[] args) {
        String s = DigestUtils.md5DigestAsHex("12345678".getBytes());
    }

}

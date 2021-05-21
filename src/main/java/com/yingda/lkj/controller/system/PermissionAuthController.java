package com.yingda.lkj.controller.system;

import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import com.yingda.lkj.service.system.PermissionAuthService;
import com.yingda.lkj.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class PermissionAuthController extends BaseController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PermissionAuthService permissionAuthService;

    @RequestMapping("/togglePermissionAuth")
    @ResponseBody
    public Json togglePermissionAuth() throws CustomException {
        String roleId = req.getParameter("roleId");
        String permissionId = req.getParameter("permissionId");
        String hasAuthStr = req.getParameter("hasAuth");
        checkParameters("roleId", "permissionId", "hasAuth");
        boolean hasAuth = Boolean.parseBoolean(hasAuthStr);

        if (hasAuth)
            permissionAuthService.addPermissionAuth(roleId, permissionId);
        if (!hasAuth)
            permissionAuthService.removePermissionAuth(roleId, permissionId);

        return new Json(JsonMessage.SUCCESS);
    }
}

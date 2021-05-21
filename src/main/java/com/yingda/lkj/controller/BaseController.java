package com.yingda.lkj.controller;

import com.yingda.lkj.beans.entity.system.Role;
import com.yingda.lkj.beans.entity.system.User;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.Page;
import com.yingda.lkj.service.base.BaseService;
import com.yingda.lkj.service.system.RoleService;
import com.yingda.lkj.service.system.UserService;
import com.yingda.lkj.utils.RequestUtil;
import com.yingda.lkj.utils.SpringContextUtil;
import com.yingda.lkj.utils.StringUtils;
import com.yingda.lkj.utils.hql.HqlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author hood  2019/12/19
 */
@Controller
public class BaseController {
    @Autowired
    protected BaseService<BigInteger> bigIntegerBaseService;
    @Autowired
    private UserService userService;

    protected void setObjectNum(String sql, Map<String, Object> params) throws Exception {
        List<BigInteger> count = bigIntegerBaseService.findSQL(HqlUtils.getCountSql(sql), params);
        page.setDataTotal(count);
    }

    protected void checkParameters(String... paramNames) throws CustomException {
        RequestUtil.checkParameters(req, paramNames);
    }

    protected void checkParametersWithErrorMsg(String... paramsInfo) throws CustomException {
        Map<String, String> paramsInfoMap = new LinkedHashMap<>();
        for (int i = 0; i < paramsInfo.length - 1; i += 2) {
            String param = paramsInfo[i];
            String errorMsg = paramsInfo[i + 1];
            paramsInfoMap.put(param, errorMsg);
        }
        RequestUtil.checkParameters(req, paramsInfoMap);
    }

    protected void checkParameters(Map<String, String> paramsMap) throws CustomException {
        RequestUtil.checkParameters(req, paramsMap);
    }

    protected Timestamp current() {
        return new Timestamp(System.currentTimeMillis());
    }

    protected User getUser() throws CustomException {
        String username = RequestUtil.getUserNameFromToken(req);

        if (StringUtils.isNotEmpty(username))
            return userService.getUserByUserName(username);

        return RequestUtil.getUser(req);
    }

    protected String getUserLowestLevelOrganizationId() throws CustomException {
        User user = getUser();
        String workshopId = user.getWorkshopId();
        String sectionId = user.getSectionId();
        String bureauId = user.getBureauId();
        if (StringUtils.isNotEmpty(workshopId))
            return workshopId;
        if (StringUtils.isNotEmpty(sectionId))
            return sectionId;
        return bureauId;
    }

    protected Role getRole() throws CustomException {
        RoleService roleService = (RoleService) SpringContextUtil.getBean("roleService");
        return roleService.getRole(getUser());
    }
    protected boolean isAdmin() throws CustomException {
        Role role = getRole();
        return Role.ADMIN.equals(role.getName());
    }
    protected byte getOrganizationPermission() throws CustomException {
        Role role = getRole();
        return role.getOrganizationPermission();
    }

    protected String getSectionId() {
        return RequestUtil.getSectionId(req);
    }

    @Deprecated
    protected ModelAndView createModelAndView(String url) {
        return new ModelAndView(url, attributes);
    }

    protected Map<String, Object> getAttributes() {
        return RequestUtil.getParameterMap(req);
    }

    protected Page page;

    @Deprecated
    protected Map<String, Object> attributes = new HashMap<>();

    @Deprecated
    protected Map<String, Object> params = new HashMap<>();

    protected Map<String, Object> conditions = new HashMap<>();

    protected HttpServletRequest req;

    protected HttpServletResponse resp;

    protected String createDataAuthSql(String dataName, int organizationLevel, Map<String, Object> params) throws CustomException {
        return HqlUtils.createDataAuthSql(dataName, organizationLevel, getUser(), params);
    }

    @Autowired
    public void setReq(HttpServletRequest req) {
        this.req = req;
    }

    @Autowired
    public void setResp(HttpServletResponse resp) {
        this.resp = resp;
    }


    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void export(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getName();
        fileName = fileName.replace(".", "");
        fileName = fileName.replace(" ", "");

        resp.setContentType("application/force-download");
        resp.addHeader("Content-Disposition", "attachment;fileName=\"" + new String(fileName.getBytes(), "ISO8859-1") + ".xlsx\"");
        resp.setContentLength((int) multipartFile.getSize());
        byte[] buffer = new byte[1024];

        try (InputStream fis = multipartFile.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(fis);) {
            OutputStream os = resp.getOutputStream();
            int len;
            while ((len = bis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            resp.flushBuffer();
        }
    }

    @ModelAttribute
    public void setPage(Page page) {
        if (StringUtils.isEmpty(page.getCurrentPage()))
            page.setCurrentPage(1);

        if (StringUtils.isEmpty(page.getPageSize()))
            page.setPageSize(20);
        this.page = page;
    }

    protected void countTime(long time, int line) {
        System.out.println(line);
        System.out.println(System.currentTimeMillis() - time);
    }
}

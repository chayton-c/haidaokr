package com.yingda.lkj.service.backstage.wechat;

import com.yingda.lkj.beans.entity.backstage.wechat.EnterpriseWechatDepartment;
import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.pojo.enterprisewechat.department.EnterpriseWechatDepartmentResponse;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.utils.StreamUtil;
import com.yingda.lkj.utils.wechat.enterprise.EnterpriseWeChatDepartmentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("enterpriseWechatDepartmentService")
public class EnterpriseWechatDepartmentService {

    @Autowired
    private BaseDao<EnterpriseWechatDepartment> enterpriseWechatDepartmentBaseDao;

    public void saveOrUpdate(EnterpriseWechatDepartment enterpriseWechatDepartment) {
        enterpriseWechatDepartmentBaseDao.saveOrUpdate(enterpriseWechatDepartment);
    }

    public void loadAndSave() throws CustomException {
        List<EnterpriseWechatDepartmentResponse> enterpriseWechatDepartmentResponses = EnterpriseWeChatDepartmentClient.loadDepartments();
        List<EnterpriseWechatDepartment> enterpriseWechatDepartments = StreamUtil.getList(enterpriseWechatDepartmentResponses, EnterpriseWechatDepartment::createFromWechatResponse);
        enterpriseWechatDepartmentBaseDao.bulkInsert(enterpriseWechatDepartments);
    }


}

package com.yingda.lkj.utils.hql;

import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.beans.entity.system.Role;
import com.yingda.lkj.beans.entity.system.User;
import com.yingda.lkj.service.backstage.organization.OrganizationClientService;
import com.yingda.lkj.utils.SpringContextUtil;
import com.yingda.lkj.utils.StreamUtil;
import com.yingda.lkj.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author hood  2019/11/8
 */
public class HqlUtils {

    public static String getCountSql(String sql) {
        return getCountSql(sql, "SELECT COUNT(1) ");
    }

    public static String getCountSql(String sql, String head) {
        return head + sql.substring(sql.indexOf("FROM"));
    }

    public static String createDataAuthSql(String dataName, int organizationLevel, User user, Map<String, Object> params) {
        String workshopId = user.getWorkshopId();
        String sectionId = user.getSectionId();
        String bureauId = user.getBureauId();

        OrganizationClientService organizationClientService = (OrganizationClientService) SpringContextUtil.getBean("organizationClientService");

        if (Role.ADMIN.equals(user.getUserName())) return "";

        if (organizationLevel == Organization.BUREAU) return "";
        if (organizationLevel == Organization.SECTION) {
            params.put("sectionId", sectionId);
            return String.format("AND %s.section_id = :sectionId\n", dataName);
        }
        if (organizationLevel == Organization.WORKSHOP) {
            if (StringUtils.isNotEmpty(workshopId)) {
                params.put("workshopId", workshopId);
                return String.format("AND %s.workshop_id = :workshopId\n", dataName);
            }
            if (StringUtils.isNotEmpty(sectionId)) {
                List<Organization> workshops = organizationClientService.getSlaves(sectionId);
                if (workshops.isEmpty()) { // 用户所在站段下没有车间
                    params.put("workshopId", "ひとみ"); // 传一个肯定不能当id参数，表示空
                    return String.format("AND %s.workshop_id = :workshopId\n", dataName);
                }
                params.put("workshopIds", StreamUtil.getList(workshops, Organization::getId));
                return String.format("AND %s.workshop_id in :workshopIds\n", dataName);
            }
            if (StringUtils.isNotEmpty(bureauId)) { // 这个人只有局id，大概是admin，什么都不给他看
                params.put("workshopId", "ひとみ");
                return String.format("AND %s.workshop_id = :workshopId\n", dataName);
            }
        }
        // 班组不管

        return "";
    }

    public static void main(String[] args) {
    }

}

package com.yingda.lkj.service.impl.backstage.organization;

import com.yingda.lkj.annotation.CacheMethod;
import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.dao.BaseDao;
import com.yingda.lkj.service.backstage.organization.OrganizationService;
import com.yingda.lkj.utils.pojo.PojoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hood  2019/12/26
 */
@CacheMethod
@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private BaseDao<Organization> organizationBaseDao;

    // key:id
    private static final Map<String, Organization> ORGANIZATION_MAP = new HashMap<>();

    @Override
    public void saveOrUpdate(Organization organization) {
        init();
        organizationBaseDao.saveOrUpdate(organization);
        ORGANIZATION_MAP.put(organization.getId(), organization);
    }

    @Override
    public void delete(List<String> ids) {
        init();

        ids.forEach(ORGANIZATION_MAP::remove);
        organizationBaseDao.executeHql(
                "delete from Organization where id in (:ids)",
                Map.of("ids", ids)
        );
    }

    @Override
    public Organization getByName(String name) {
        init();

        return ORGANIZATION_MAP.values().stream().filter(x -> x.getName().equals(name)).reduce(null, (x, y) -> y);
    }

    @Override
    public List<Organization> showDown() {
        init();

        List<Organization> collectFromCache = new ArrayList<>(ORGANIZATION_MAP.values())
                .stream()
                .sorted(Comparator.comparingInt(Organization::getSeq))
                .collect(Collectors.toList());

        try {
            // 直接返回内存，可能会被操作，所以返回一个复制的
            return PojoUtils.copyPojoList(collectFromCache, Organization.class);
        } catch (ReflectiveOperationException e) {
            LOGGER.error("反射异常，不可能出错的，除非改了构造方法", e);
            return null;
        }
    }

    @Override
    public Organization getById(String id) {
        init();
        Organization organizationFromCache = Optional.ofNullable(ORGANIZATION_MAP.get(id)).orElse(new Organization());

        // 直接返回内存，可能会被操作，所以返回一个复制的
        return PojoUtils.copyPojo(organizationFromCache, new Organization());
    }

    @Override
    public List<Organization> getCompleteTree(String sectionId) {
        init();
        List<Organization> returnList = new ArrayList<>();

        Organization section = getById(sectionId);
        returnList.add(section);
        if (section == null)
            return returnList;

        Organization bureau = getById(section.getParentId());
        returnList.add(bureau);

        List<Organization> workshops = showDown().stream().filter(x -> x.getParentId().equals(sectionId)).collect(Collectors.toList());
        returnList.addAll(workshops);
        if (workshops.isEmpty())
            return returnList;

        List<String> workshopIds = workshops.stream().map(Organization::getId).collect(Collectors.toList());

        List<Organization> workAreas = showDown().stream().filter(x -> workshopIds.contains(x.getParentId())).collect(Collectors.toList());
        returnList.addAll(workAreas);

        return jsonified(returnList).stream().sorted(Comparator.comparing(Organization::getSeq)).collect(Collectors.toList());
    }

    @Override
    public List<Organization> jsonified(List<Organization> raw) {
        init();
        raw = raw.stream().filter(x -> x.getLevel() != Organization.ROOT).collect(Collectors.toList());

        List<Organization> result = new ArrayList<>();
        for (Organization organization : raw) {
            String parentId = organization.getParentId();
            Organization parent = raw.stream().filter(x -> x.getId().equals(parentId)).findFirst().orElse(null);
            if (parent == null) {
                result.remove(organization);
                result.add(organization);
                continue;
            }
            List<Organization> childrens = Optional.ofNullable(parent.getOrganizationList()).orElse(new ArrayList<>());
            childrens.remove(organization);
            childrens.add(organization);
            parent.setOrganizationList(childrens);
            result.add(parent);
        }
        if (result.size() == raw.size()) return result;
        return jsonified(result);
    }

    @Override
    public List<Organization> getCompleteTree() {
        init();
        return jsonified(showDown()).stream().sorted(Comparator.comparing(Organization::getSeq)).collect(Collectors.toList());
    }


    private void init() {
//        if (!ORGANIZATION_MAP.isEmpty())
//            return;

        List<Organization> organizations = organizationBaseDao.find(
                "from Organization"
        );

        ORGANIZATION_MAP.putAll(
                organizations.stream().collect(Collectors.toMap(Organization::getId, x -> x))
        );
    }
}

package com.yingda.lkj.service.impl.backstage.organization;

import com.yingda.lkj.beans.entity.backstage.organization.Organization;
import com.yingda.lkj.beans.entity.system.User;
import com.yingda.lkj.service.backstage.organization.OrganizationClientService;
import com.yingda.lkj.service.backstage.organization.OrganizationService;
import com.yingda.lkj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hood  2019/12/27
 */
@Service("organizationClientService")
public class OrganizationClientServiceImpl implements OrganizationClientService {

    @Autowired
    private OrganizationService organizationService;

    @Override
    public Organization getParentTree(Organization organization) {
        Organization parent = getById(organization.getParentId());
        if (parent.getId() == null || parent.getLevel() == Organization.ROOT) return organization;
        parent.setOrganizationList(new ArrayList<>(List.of(organization)));
        return getParentTree(parent);
    }

    @Override
    public List<Organization> getSlaves(String masterId) {
        List<Organization> organizations = organizationService.showDown();
        List<Organization> slaves = organizations.stream().filter(x -> x.getParentId().equals(masterId)).collect(Collectors.toList());

        if (slaves.isEmpty()) return slaves;
        for (Organization slave : slaves)
            slave.setOrganizationList(getSlaves(slave.getId()));
        return slaves;
    }

    @Override
    public List<Organization> getWorkAreas(List<String> workshopId) {
        return getWorkAreas().stream().filter(x -> workshopId.contains(x.getParentId())).collect(Collectors.toList());
    }

    @Override
    public Organization getParent(String organizationId) {
        Organization organization = organizationService.getById(organizationId);
        if (organization == null)
            return null;

        String parentId = organization.getParentId();
        if (StringUtils.isEmpty(parentId))
            return null;

        return organizationService.getById(parentId);
    }

    @Override
    public List<Organization> getBureaus() {
        return organizationService.getCompleteTree();
    }

    @Override
    public List<Organization> getSections() {
        return organizationService.showDown().stream().filter(x -> x.getLevel() == Organization.SECTION).collect(Collectors.toList());
    }

    @Override
    public List<Organization> getWorkshops() {
        return organizationService.showDown().stream().filter(x -> x.getLevel() == Organization.WORKSHOP).collect(Collectors.toList());
    }

    @Override
    public List<Organization> getWorkAreas() {
        return organizationService.showDown().stream().filter(x -> x.getLevel() == Organization.WORK_AREA).collect(Collectors.toList());
    }

    @Override
    public List<Organization> getWorkAreasBySectionId(String sectionId) {
        List<Organization> workshops = getSlaves(sectionId);
        if (workshops.isEmpty())
            return new ArrayList<>();

        List<String> workshopIds = workshops.stream().map(Organization::getId).collect(Collectors.toList());
        return getWorkAreas().stream().filter(x -> workshopIds.contains(x.getParentId())).collect(Collectors.toList());
    }

    @Override
    public List<Organization> getAllWorkAreas() {
        return organizationService.showDown().stream().filter(x -> x.getLevel() == Organization.WORK_AREA).collect(Collectors.toList());
    }

    @Override
    public Organization getById(String id) {
        return organizationService.getById(id);
    }

    @Override
    public List<Organization> getByIds(List<String> ids) {
        return organizationService.showDown().stream().filter(x -> ids.contains(x.getId())).collect(Collectors.toList());
    }

    @Override
    public List<Organization> getByLevel(byte level) {
        return organizationService.showDown().stream().filter(x -> x.getLevel() == level).collect(Collectors.toList());
    }


    @Override
    public Organization getBureauByWorkareaId(String workAreaId) {
        // 内存方法为所欲为，除非内存改用redis
        Organization workArea = getById(workAreaId);
        Organization workshop = getById(workArea.getParentId());
        Organization section = getById(workshop.getParentId());
        return getById(section.getParentId());
    }

    @Override
    public Organization getBureauByCode(String code) {
        return organizationService.showDown().stream().filter(x -> x.getCode().equals(code)).reduce((x, y) -> x).orElse(null);
    }

    @Override
    public List<Organization> getWorkshopsByUser(User user) {
        if (StringUtils.isNotEmpty(user.getWorkAreaId()))
            return List.of(getById(user.getWorkAreaId()));

        if (StringUtils.isNotEmpty(user.getWorkshopId()))
            return List.of(getById(user.getWorkshopId()));

        if (StringUtils.isNotEmpty(user.getSectionId()))
            return getSlaves(user.getSectionId());

        return getWorkshops();
    }

    @Override
    public List<Organization> getBureausByAuth(User user) {
        String sectionId = user.getSectionId();
        if (StringUtils.isEmpty(sectionId)) {
            Organization bureau = getById(user.getBureauId());
        }


        List<Organization> bureaus = getBureaus();
        for (Organization bureau : bureaus) {

        }


        return null;
    }
}

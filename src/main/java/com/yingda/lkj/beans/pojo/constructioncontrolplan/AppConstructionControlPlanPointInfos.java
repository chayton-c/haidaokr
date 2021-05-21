package com.yingda.lkj.beans.pojo.constructioncontrolplan;

import com.yingda.lkj.beans.entity.backstage.location.Location;

import java.util.List;

public class AppConstructionControlPlanPointInfos {
    private List<AppConstructionControlPlanPoint> constructionControlPlanPoints;
    private List<Location> locations;

    public List<AppConstructionControlPlanPoint> getConstructionControlPlanPoints() {
        return constructionControlPlanPoints;
    }

    public void setConstructionControlPlanPoints(List<AppConstructionControlPlanPoint> constructionControlPlanPoints) {
        this.constructionControlPlanPoints = constructionControlPlanPoints;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}

package com.yingda.lkj.beans.pojo.app;

import java.util.List;

public class AppRailwayLineSectionInfo {
    private List<AppLocation> locations;
    private List<AppRailwayLineSection> railwayLineSections;

    public List<AppLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<AppLocation> locations) {
        this.locations = locations;
    }

    public List<AppRailwayLineSection> getRailwayLineSections() {
        return railwayLineSections;
    }

    public void setRailwayLineSections(List<AppRailwayLineSection> railwayLineSections) {
        this.railwayLineSections = railwayLineSections;
    }
}

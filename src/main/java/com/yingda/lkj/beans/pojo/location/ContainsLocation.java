package com.yingda.lkj.beans.pojo.location;

import com.yingda.lkj.beans.entity.backstage.location.Location;

import java.beans.Transient;
import java.util.List;

/**
 * @author hood  2020/12/15
 */
public interface ContainsLocation {

    String getId();

    void setId(String id);

    List<Location> getLocations();

    void setLocations(List<Location> locations);
}

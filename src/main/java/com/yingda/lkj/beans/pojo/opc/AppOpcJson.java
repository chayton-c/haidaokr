package com.yingda.lkj.beans.pojo.opc;

import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.entity.backstage.opc.Opc;
import com.yingda.lkj.beans.entity.backstage.opc.OpcMark;

import java.util.List;

public class AppOpcJson {
    List<Location> locations;
    List<OpcMark> opc_marks;
    List<Opc> opcs;

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<OpcMark> getOpc_marks() {
        return opc_marks;
    }

    public void setOpc_marks(List<OpcMark> opc_marks) {
        this.opc_marks = opc_marks;
    }

    public List<Opc> getOpcs() {
        return opcs;
    }

    public void setOpcs(List<Opc> opcs) {
        this.opcs = opcs;
    }
}

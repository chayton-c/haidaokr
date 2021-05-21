package com.yingda.lkj.beans.pojo.line;

import com.yingda.lkj.beans.entity.backstage.line.RailwayLine;
import com.yingda.lkj.beans.entity.backstage.line.Station;
import com.yingda.lkj.utils.StreamUtil;

import java.util.List;

/**
 * @author hood  2020/11/22
 */
public class LineSelectTreeNode {
    private String key;
    private String value;
    private String title;
    private String label;
    private boolean disabled;
    private List<LineSelectTreeNode> children;

    public LineSelectTreeNode(RailwayLine railwayLine, List<Station> stations) {
        this.key = railwayLine.getId();
        this.value = this.key;
        this.title = railwayLine.getName();
        this.label = this.title;
        this.children = StreamUtil.getList(stations, LineSelectTreeNode::new);
    }

    public LineSelectTreeNode(Station station) {
        this.key = station.getId();
        this.value = this.key;
        this.title = station.getName();
        this.label = this.title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public List<LineSelectTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<LineSelectTreeNode> children) {
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

package com.yingda.lkj.beans.pojo.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * excel row
 *
 * @author hood  2020/1/6
 */
@Data
public class ExcelRowInfo {
    private Integer lineIndex; // 行下标(从0开始)
    private List<String> cells; // 行信息

    public ExcelRowInfo(Integer lineIndex, List<String> cells) {
        this.lineIndex = lineIndex;
        this.cells = cells;
    }

    public ExcelRowInfo(Integer lineIndex, String... cells) {
        this.lineIndex = lineIndex;
        this.cells = new ArrayList<>(Arrays.asList(cells));
    }
}

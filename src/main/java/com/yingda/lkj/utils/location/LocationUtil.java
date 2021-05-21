package com.yingda.lkj.utils.location;

import com.yingda.lkj.beans.entity.backstage.location.Location;
import com.yingda.lkj.beans.pojo.location.ContainsLocation;
import org.apache.lucene.util.SloppyMath;

import java.util.List;

public class LocationUtil {
    private static double EARTH_RADIUS = 6_371_008.7714D;

    public static double getDistance(Location location1, Location location2) {
        return SloppyMath.haversinMeters(location1.getLatitude(), location1.getLongitude(), location2.getLatitude(), location2.getLongitude());
    }

    private static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        return SloppyMath.haversinMeters(lat1, lng1, lat2, lng2);
    }

    // 计算两向量的法向量
    private static double[] calculateNormalVector(double[] first, double[] second) {
        double[] result = new double[3];
        //根据矩阵公式算出模不为1的法向量
        double x = first[1] * second[2] - first[2] * second[1];
        double y = first[2] * second[0] - first[0] * second[2];
        double z = first[0] * second[1] - first[1] * second[0];
        //将法向量转为模为1的法向量
        result[0] = x;
        result[1] = y;
        result[2] = z;
        changeDisModelTo1(result);
        return result;
    }

    // 将向量转为模为1的向量方便用于夹角的计算
    private static void changeDisModelTo1(double[] target) {
        //模长
        double disModel = Math.sqrt(target[0] * target[0] + target[1] * target[1] + target[2] * target[2]);
        target[0] = target[0] / disModel;
        target[1] = target[1] / disModel;
        target[2] = target[2] / disModel;
    }

    //将经纬度转化为弧度
    public static double torad(double deg) {
        return deg / 180 * Math.PI;
    }

    private static double[] changeLonAndLaToXY(double longitude, double latitude) {
        //转为为弧度
        double lng = torad(longitude);
        double lat = torad(latitude);
        double[] w = new double[3];
        //将弧度转为在映射三维坐标的长度
        double x = Math.cos(lat) * Math.cos(lng);
        double y = Math.cos(lat) * Math.sin(lng);
        double z = Math.sin(lat);
        w[0] = x;
        w[1] = y;
        w[2] = z;
        return w;
    }

    private static double calculateAngle(double[] first, double[] second) {
        return first[0] * second[0] + first[1] * second[1] + first[2] * second[2];
    }

    // 点到线的比较精确距离
    public static double distanceOfPointToLineAcc(double lngLine1, double latLine1, double lngLine2, double latLine2, double lngFrom,
                                                  double latFrom) {
        // 将3个经纬度转为两条在三维坐标系模为1的向量
        double[] vectorone = changeLonAndLaToXY(lngLine1, latLine1);
        double[] vectortwo = changeLonAndLaToXY(lngLine2, latLine2);
        double[] vectorfrom = changeLonAndLaToXY(lngFrom, latFrom);
        // 计算出两向量的法向量
        double[] normalvector = calculateNormalVector(vectorone, vectortwo);
        // 计算法向量和向量的cos值 即为from向量和其他两向量组成平面的夹角的sin值
        double cos = calculateAngle(normalvector, vectorfrom);
        // 将cos转为弧度
        double result = Math.abs(Math.asin(cos));
        result = result * EARTH_RADIUS;

        double line1Distance = getDistance(lngLine1, latLine1, lngFrom, latFrom);
        if (line1Distance == result)
            return result;

        double line2Distance = getDistance(lngLine2, latLine2, lngFrom, latFrom);
        if (line2Distance == result)
            return result;
        double lineDistance = getDistance(lngLine1, latLine1, lngLine2, latLine2);
        if (lineDistance == 0) // 线路首末同一点的情况
            return line1Distance;

        // 余弦定理，这他妈是程序员干的事？
        double cos1 = (line1Distance * line1Distance + lineDistance * lineDistance - line2Distance * line2Distance) / (2 * line1Distance * lineDistance);
        if (cos1 < 0)
            return line1Distance;

        double cos2 = (line2Distance * line2Distance + lineDistance * lineDistance - line1Distance * line1Distance) / (2 * line2Distance * lineDistance);
        if (cos2 < 0)
            return line2Distance;

        return result;
    }

    // 点到线的比较精确距离
    public static double distanceOfPointToLineAcc(Location leftLine, Location rightLine, Location from) {
        return distanceOfPointToLineAcc(leftLine.getLongitude(), leftLine.getLatitude(), rightLine.getLongitude(), rightLine.getLatitude(), from.getLongitude(), from.getLatitude());
    }

    /**
     * 点到多条线的最近距离
     */
    public static double pointToMultiLineShortestDistance(Location point, List<? extends ContainsLocation> multiLine) {
        double shortestDistance = Double.MAX_VALUE;
        // 测定点到opcs包含的所有路径的最近距离(米)
        for (ContainsLocation containsLocation : multiLine) {
            List<Location> locations = containsLocation.getLocations();
            for (int i = 0, locationsSize = locations.size(); i < locationsSize; i++) {
                if (i == locationsSize - 1) continue;
                Location leftLocation = locations.get(i);
                Location rightLocation = locations.get(i + 1);
                double distance = LocationUtil.distanceOfPointToLineAcc(leftLocation, rightLocation, point);
                shortestDistance = Math.min(distance, shortestDistance);
            }
        }
        return shortestDistance;
    }

    public static void main(String[] args) {
        double distance = distanceOfPointToLineAcc(116.402364,39.914133, 116.407188,39.914288, 116.4039,39.914503);
        System.out.printf("距离%f米", distance);
    }
}

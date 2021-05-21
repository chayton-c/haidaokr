package com.yingda.lkj.utils.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yingda.lkj.beans.entity.backstage.location.Location;

import java.util.*;

public class GeoJsonUtil {

    /**
     * 单点
     *
     * @param locations
     * @return
     */
    public static JSON formatFeatureCollectionPoint(List<Location> locations) {
        JSONObject featureCollectionJsonObject = new JSONObject();
        JSONArray featuresJsonArray = new JSONArray();

        locations.forEach(location -> {
            JSONObject featureJson = new JSONObject();
            JSONObject geometryJson = new JSONObject();
            JSONObject properties = new JSONObject();
            double[] coordinateArray = new double[]{location.getLongitude(), location.getLatitude()};

            geometryJson.put("type", "Point");
            geometryJson.put("coordinates", coordinateArray);

            properties.put("iconUrl", location.getIconUrl());
            properties.put("shadowUrl", location.getShadowUrl());
            properties.put("opcMarkTypeName", location.getOpcMarkTypeName());
            properties.put("remark", location.getOpcMarkremark());

            featureJson.put("type", "Feature");
            featureJson.put("geometry", geometryJson);
            featureJson.put("properties", properties);
            featuresJsonArray.add(featureJson);

        });


        featureCollectionJsonObject.put("type", "FeatureCollection");
        featureCollectionJsonObject.put("features", featuresJsonArray);

        return featureCollectionJsonObject;
    }

    /**
     * 多点
     *
     * @param locations
     * @return
     */
    public static JSON formatFeatureCollectionMultiPoint(List<Location> locations) {
        System.out.println("formatFeatureCollectionMultiPoint被调用");
        JSONObject featureCollectionJsonObject = new JSONObject();
        JSONArray featuresJsonArray = new JSONArray();
        JSONObject featureJson = new JSONObject();
        JSONObject geometryJson = new JSONObject();
        JSONObject properties = new JSONObject();

        ArrayList<double[]> coordinatesArray = new ArrayList<>();

        locations.forEach(location -> {
            double[] coordinateArray = new double[]{location.getLongitude(), location.getLatitude()};
            properties.put("color", location.getOpcColor());
            coordinatesArray.add(coordinateArray);
        });

        geometryJson.put("type", "MultiPoint");
        geometryJson.put("coordinates", coordinatesArray);

        featureJson.put("type", "Feature");
        featureJson.put("geometry", geometryJson);
        featureJson.put("properties", properties);

        featuresJsonArray.add(featureJson);

        featureCollectionJsonObject.put("type", "FeatureCollection");
        featureCollectionJsonObject.put("features", featuresJsonArray);

        return featureCollectionJsonObject;
    }

    /**
     * 线
     *
     * @param locations
     * @return
     */
    public static JSON formatFeatureCollectionLineString(List<Location> locations) {
        JSONObject featureCollectionJsonObject = new JSONObject();
        JSONArray featuresJsonArray = new JSONArray();
        JSONObject featureJson = new JSONObject();
        JSONObject geometryJson = new JSONObject();
        JSONObject properties = new JSONObject();

        ArrayList<double[]> coordinatesArray = new ArrayList<>();

        locations.forEach(location -> {
            double[] coordinateArray = new double[]{location.getLongitude(), location.getLatitude()};
            properties.put("color", location.getOpcColor());
            coordinatesArray.add(coordinateArray);
        });

        geometryJson.put("type", "LineString");
        geometryJson.put("coordinates", coordinatesArray);

        featureJson.put("type", "Feature");
        featureJson.put("geometry", geometryJson);
        featureJson.put("properties", properties);

        featuresJsonArray.add(featureJson);

        featureCollectionJsonObject.put("type", "FeatureCollection");
        featureCollectionJsonObject.put("features", featuresJsonArray);

        return featureCollectionJsonObject;
    }

}

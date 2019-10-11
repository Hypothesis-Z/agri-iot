package com.zzq.app.api;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public final class Datapoint {
    private static final Map<String, DatapointParameter> datapointMap = new HashMap<>(4);

    private Datapoint(){
        datapointMap.put("temp",new DatapointParameter("温度","℃", -20, 60));
        datapointMap.put("humi", new DatapointParameter("温度","%", 0,100));
        datapointMap.put("CO2",new DatapointParameter("二氧化碳浓度","ppm", 0, 1000));
        datapointMap.put("light",new DatapointParameter("环境光强度","lx", 0, 10000));
    }

    @NonNull
    public static Map<String, DatapointParameter> getDatapointMap(){
        return new Datapoint().getMap();
    }

    private Map<String, DatapointParameter> getMap(){
        return datapointMap;
    }

    public static class DatapointParameter{
        private String name;
        private String unit;
        private double upperLimitation;
        private double lowerLimitation;

        DatapointParameter(String name, String unit, double lowerLimitation, double upperLimitation){
            this.name = name;
            this.unit = unit;
            this.lowerLimitation = lowerLimitation;
            this.upperLimitation = upperLimitation;
        }

        public double getLowerLimitation() {
            return lowerLimitation;
        }

        public double getUpperLimitation() {
            return upperLimitation;
        }

        @NonNull
        public String getName() {
            return name;
        }

        @NonNull
        public String getUnit() {
            return unit;
        }
    }
}



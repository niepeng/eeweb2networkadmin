package com.chengqianyun.eeweb2networkadmin.biz.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ExportBatchDataBean {

    private long deviceId;

    private Double avgTemp;
    private Double maxTemp;
    private Double minTemp;

    private Double avgHumi;
    private Double maxHumi;
    private Double minHumi;

    private Double avgShine;
    private Double maxShine;
    private Double minShine;

    private Double avgPressure;
    private Double maxPressure;
    private Double minPressure;

}

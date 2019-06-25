package com.chengqianyun.eeweb2networkadmin.biz.enums;

import lombok.Getter;

@Getter
public enum MaxMinEnum {
    avg("平均值"), max("最大值"),min("最小值");

    private String name;

    private MaxMinEnum(String name) {
       this.name = name;
    }
}

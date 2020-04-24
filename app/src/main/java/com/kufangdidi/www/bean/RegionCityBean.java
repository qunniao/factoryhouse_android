package com.kufangdidi.www.bean;

import java.util.List;

public class RegionCityBean {
    private String areacode;
    private String areaname;
    private List<RegionAreaBean> subarea;
    public String getAreacode() {
        return areacode;
    }
    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }
    public String getAreaname() {
        return areaname;
    }
    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }
    public List<RegionAreaBean> getSubarea() {
        return subarea;
    }
    public void setSubarea(List<RegionAreaBean> subarea) {
        this.subarea = subarea;
    }
}

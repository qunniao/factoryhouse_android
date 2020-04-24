package com.kufangdidi.www.bean;

import java.util.List;

public class RegionProvinceBean {
    private String areacode;
    private String areaname;
    private List<RegionCityBean> subarea;
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
    public List<RegionCityBean> getSubarea() {
        return subarea;
    }
    public void setSubarea(List<RegionCityBean> subarea) {
        this.subarea = subarea;
    }
}

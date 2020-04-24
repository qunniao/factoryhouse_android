package com.kufangdidi.www.modal;



import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * FplHousePO对象,厂房,仓库,土地
 */

public class FplHouseModal {

    private int fid;//FplHouse主键

    private String productId;//房源编号

    private short oneType;//类别 (0初始,1厂房 2 厂库 3土地)

    private short towType;//二类(0:初始,1:出租,2:出售, 3:求租,4求购)

    private String region;//区域信息

    private int miniTenancy;//最短租期 月

    private int subleaseType;//是否分租 0:分租 1不分租

    private String nearbyMatching;//周边配套说明

    private String suitableEnterprise;//适合企业说明

    private String ahtName;//建筑名称

    private String ahtType;//建筑类型

    private String ahtStructure;//建筑结构

    private float ahtArea;//建筑面积

    private float ahtavailableArea;//可用面积

    private int allNum;//整栋层数

    private String rentNum;//出租层数 层

    private double downMeter;//地层层高 米

    private double upMeter;//上层层高 米

    private short carType;//行车 0有 1无

    private int carWeight;//行车载重 吨

    private short cargoElevator;//货梯 0有 1无

    private short ceLoad;//货梯载重 吨

    private Timestamp createTime;//创建时间系统生成

    private Timestamp updateTime;//更新时间

    private BigDecimal salePrice;//价格

    private int cid;//city表主键

    private String latitude;//经度

    private String longitude;//纬度

    private String provinceName;//省级名称

    private String cityName;//市级名称

    private String regionName;//区级名称

    private String addressItem;//详细地址

    private List<String> urlList;//图片路径

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public short getOneType() {
        return oneType;
    }

    public void setOneType(short oneType) {
        this.oneType = oneType;
    }

    public short getTowType() {
        return towType;
    }

    public void setTowType(short towType) {
        this.towType = towType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getMiniTenancy() {
        return miniTenancy;
    }

    public void setMiniTenancy(int miniTenancy) {
        this.miniTenancy = miniTenancy;
    }

    public int getSubleaseType() {
        return subleaseType;
    }

    public void setSubleaseType(int subleaseType) {
        this.subleaseType = subleaseType;
    }

    public String getNearbyMatching() {
        return nearbyMatching;
    }

    public void setNearbyMatching(String nearbyMatching) {
        this.nearbyMatching = nearbyMatching;
    }

    public String getSuitableEnterprise() {
        return suitableEnterprise;
    }

    public void setSuitableEnterprise(String suitableEnterprise) {
        this.suitableEnterprise = suitableEnterprise;
    }

    public String getAhtName() {
        return ahtName;
    }

    public void setAhtName(String ahtName) {
        this.ahtName = ahtName;
    }

    public String getAhtType() {
        return ahtType;
    }

    public void setAhtType(String ahtType) {
        this.ahtType = ahtType;
    }

    public String getAhtStructure() {
        return ahtStructure;
    }

    public void setAhtStructure(String ahtStructure) {
        this.ahtStructure = ahtStructure;
    }

    public float getAhtArea() {
        return ahtArea;
    }

    public void setAhtArea(float ahtArea) {
        this.ahtArea = ahtArea;
    }

    public float getAhtavailableArea() {
        return ahtavailableArea;
    }

    public void setAhtavailableArea(float ahtavailableArea) {
        this.ahtavailableArea = ahtavailableArea;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public String getRentNum() {
        return rentNum;
    }

    public void setRentNum(String rentNum) {
        this.rentNum = rentNum;
    }

    public double getDownMeter() {
        return downMeter;
    }

    public void setDownMeter(double downMeter) {
        this.downMeter = downMeter;
    }

    public double getUpMeter() {
        return upMeter;
    }

    public void setUpMeter(double upMeter) {
        this.upMeter = upMeter;
    }

    public short getCarType() {
        return carType;
    }

    public void setCarType(short carType) {
        this.carType = carType;
    }

    public int getCarWeight() {
        return carWeight;
    }

    public void setCarWeight(int carWeight) {
        this.carWeight = carWeight;
    }

    public short getCargoElevator() {
        return cargoElevator;
    }

    public void setCargoElevator(short cargoElevator) {
        this.cargoElevator = cargoElevator;
    }

    public short getCeLoad() {
        return ceLoad;
    }

    public void setCeLoad(short ceLoad) {
        this.ceLoad = ceLoad;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAddressItem() {
        return addressItem;
    }

    public void setAddressItem(String addressItem) {
        this.addressItem = addressItem;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }
}

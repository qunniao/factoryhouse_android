package com.kufangdidi.www.modal;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class AllCityBean implements IPickerViewData {


    /**
     * name : 河北省
     * city : [{"name":"石家庄市","area":["长安区","桥东区","桥西区","新华区","郊  区","井陉矿区","井陉县","正定县","栾城县","行唐县","灵寿县","高邑县","深泽县","赞皇县","无极县","平山县","元氏县","赵  县","辛集市","藁","晋州市","新乐市","鹿泉市"]},{"name":"唐山市","area":["路南区","路北区","古冶区","开平区","新  区","丰润县","滦  县","滦南县","乐亭县","迁西县","玉田县","唐海县","遵化市","丰南市","迁安市"]},{"name":"秦皇岛市","area":["海港区","山海关区","北戴河区","青龙满族自治县","昌黎县","抚宁县","卢龙县"]},{"name":"邯郸市","area":["邯山区","丛台区","复兴区","峰峰矿区","邯郸县","临漳县","成安县","大名县","涉  县","磁  县","肥乡县","永年县","邱  县","鸡泽县","广平县","馆陶县","魏  县","曲周县","武安市"]},{"name":"邢台市","area":["桥东区","桥西区","邢台县","临城县","内丘县","柏乡县","隆尧县","任  县","南和县","宁晋县","巨鹿县","新河县","广宗县","平乡县","威  县","清河县","临西县","南宫市","沙河市"]},{"name":"保定市","area":["新市区","北市区","南市区","满城县","清苑县","涞水县","阜平县","徐水县","定兴县","唐  县","高阳县","容城县","涞源县","望都县","安新县","易  县","曲阳县","蠡  县","顺平县","博野","雄县","涿州市","定州市","安国市","高碑店市"]},{"name":"张家口","area":["桥东区","桥西区","宣化区","下花园区","宣化县","张北县","康保县","沽源县","尚义县","蔚  县","阳原县","怀安县","万全县","怀来县","涿鹿县","赤城县","崇礼县"]},{"name":"承德市","area":["双桥区","双滦区","鹰手营子矿区","承德县","兴隆县","平泉县","滦平县","隆化县","丰宁满族自治县","宽城满族自治县","围场满族蒙古族自治县"]},{"name":"沧州市","area":["新华区","运河区","沧  县","青  县","东光县","海兴县","盐山县","肃宁县","南皮县","吴桥县","献  县","孟村回族自治县","泊头市","任丘市","黄骅市","河间市"]},{"name":"廊坊市","area":["安次区","固安县","永清县","香河县","大城县","文安县","大厂回族自治县","霸州市","三河市"]},{"name":"衡水市","area":["桃城区","枣强县","武邑县","武强县","饶阳县","安平县","故城县","景  县","阜城县","冀州市","深州市"]}]
     */

    private String name;
    private List<CityBean> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }

    public static class CityBean {
        /**
         * name : 石家庄市
         * area : ["长安区","桥东区","桥西区","新华区","郊  区","井陉矿区","井陉县","正定县","栾城县","行唐县","灵寿县","高邑县","深泽县","赞皇县","无极县","平山县","元氏县","赵  县","辛集市","藁","晋州市","新乐市","鹿泉市"]
         */

        private String name;
        private List<String> area;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getArea() {
            return area;
        }

        public void setArea(List<String> area) {
            this.area = area;
        }

        @Override
        public String toString() {
            return "CityBean{" +
                    "name='" + name + '\'' +
                    ", area=" + area +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AllCityBean{" +
                "name='" + name + '\'' +
                ", city=" + city +
                '}';
    }
}

package com.kufangdidi.www.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.kufangdidi.www.R;
import com.kufangdidi.www.utils.LogUtils;

public class ShaiXuanActivity extends AppCompatActivity {
    private Spinner spinner;

    private EditText et_money_start,et_money_end;
    private Button unit1,unit2,unit3,unit4,unit5,unit6,unit7;
    private Button area1,area2,area3,area4,area5,area6,area7,area8,area9;
    private Button structure1,structure2,structure3,structure4,structure5,structure6;


    private String money_start;
    private String money_end;

    private String money_unit;
    private String area;
    private String area_start,area_end;
    private String structure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_shaixuan);
        initView();
    }

    public void tip(View view){
        LogUtils.d("tip click");
        finish();
    }
    private void initView() {
        et_money_start = findViewById(R.id.et_money_start);
        et_money_end = findViewById(R.id.et_money_end);
        unit1 = findViewById(R.id.unit1);
        unit2 = findViewById(R.id.unit2);
        unit3 = findViewById(R.id.unit3);
        unit4 = findViewById(R.id.unit4);
        unit5 = findViewById(R.id.unit5);
        unit6 = findViewById(R.id.unit6);
        unit7 = findViewById(R.id.unit7);

        area1 = findViewById(R.id.area1);
        area2 = findViewById(R.id.area2);
        area3 = findViewById(R.id.area3);
        area4 = findViewById(R.id.area4);
        area5 = findViewById(R.id.area5);
        area6 = findViewById(R.id.area6);
        area7 = findViewById(R.id.area7);
        area8 = findViewById(R.id.area8);
        area9 = findViewById(R.id.area9);

        structure1 = findViewById(R.id.structure1);
        structure2 = findViewById(R.id.structure2);
        structure3 = findViewById(R.id.structure3);
        structure4 = findViewById(R.id.structure4);
        structure5 = findViewById(R.id.structure5);
        structure6 = findViewById(R.id.structure6);
    }

    private void resetUnit(){
        unit1.setSelected(false);
        unit2.setSelected(false);
        unit3.setSelected(false);
        unit4.setSelected(false);
        unit5.setSelected(false);
        unit6.setSelected(false);
        unit7.setSelected(false);
    }

    private void resetArea(){
        area1.setSelected(false);
        area2.setSelected(false);
        area3.setSelected(false);
        area4.setSelected(false);
        area5.setSelected(false);
        area6.setSelected(false);
        area7.setSelected(false);
        area8.setSelected(false);
        area9.setSelected(false);
    }

    private void resetStructure(){
        structure1.setSelected(false);
        structure2.setSelected(false);
        structure3.setSelected(false);
        structure4.setSelected(false);
        structure5.setSelected(false);
        structure6.setSelected(false);
    }

    //重写此方法用来设置当点击activity外部时候，关闭此弹出框
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //finish();
        return true;
    }
    //此方法在布局文件中定义，用来保证点击弹出框内部的时候不会被关闭，如果不设置此方法则单击弹出框内部时候会导致弹出框关闭
    public void unit1(View view){
        setUnitData(view);
    }
    public void unit2(View view){
        setUnitData(view);
    }
    public void unit3(View view){
        setUnitData(view);
    }
    public void unit4(View view){
        setUnitData(view);
    }
    public void unit5(View view){
        setUnitData(view);
    }
    public void unit6(View view){
        setUnitData(view);
    }
    public void unit7(View view){
        setUnitData(view);
    }

    public void area1(View view){
        setAreaData(view);
    }
    public void area2(View view){
        setAreaData(view);
    }
    public void area3(View view){
        setAreaData(view);
    }
    public void area4(View view){
        setAreaData(view);
    }
    public void area5(View view){
        setAreaData(view);
    }
    public void area6(View view){
        setAreaData(view);
    }
    public void area7(View view){
        setAreaData(view);
    }
    public void area8(View view){
        setAreaData(view);
    }
    public void area9(View view){
        setAreaData(view);
    }

    public void structure1(View view){
        setStructureData(view);
    }
    public void structure2(View view){
        setStructureData(view);
    }
    public void structure3(View view){
        setStructureData(view);
    }
    public void structure4(View view){
        setStructureData(view);
    }
    public void structure5(View view){
        setStructureData(view);
    }
    public void structure6(View view){
        setStructureData(view);
    }

    public void submit(View view){
        money_start = et_money_start.getText().toString();
        if("".equals(money_start)){
            money_start = "0";
        }
        money_end = et_money_end.getText().toString();
        if("".equals(money_end)){
            money_end = "1000000";
        }


        LogUtils.d("money:"+money_start+" - "+money_end);
        LogUtils.d("money_unit:"+money_unit);
        LogUtils.d("area:"+area);
        LogUtils.d("area_start:"+area_start+" area_end:"+area_end);
        LogUtils.d("structure:"+structure);

        Intent intent=new Intent();
        intent.putExtra("money_unit", money_unit);
        intent.putExtra("money_start", money_start);
        intent.putExtra("money_end", money_end);
        intent.putExtra("area_start", area_start);
        intent.putExtra("area_end", area_end);
        intent.putExtra("structure", structure);
        setResult(5,intent);
        finish();
    }

    public void setUnitData(View view){
        resetUnit();
        view.setSelected(true);
        Button u = (Button) view;
        money_unit = u.getText().toString();
    }
    public void setAreaData(View view){
        resetArea();
        view.setSelected(true);
        Button u = (Button) view;
        area = u.getText().toString();
        if("不限".equals(area)){
            area_start = "0";
            area_end = "1000000";
        } else if("500以下".equals(area)){
            area_start = "0";
            area_end = "500";
        }else if("10000以上".equals(area)){
            area_start = "10000";
            area_end = "1000000";
        }else {
            String[] str = area.split("-");
            if(str.length==2){
                area_start = str[0];
                area_end = str[1];
            }

        }
    }
    public void setStructureData(View view){
        resetStructure();
        view.setSelected(true);
        Button u = (Button) view;
        structure = u.getText().toString();
    }
}

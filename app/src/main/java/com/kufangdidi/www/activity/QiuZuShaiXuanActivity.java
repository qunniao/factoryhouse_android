package com.kufangdidi.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.kufangdidi.www.R;
import com.kufangdidi.www.utils.LogUtils;

public class QiuZuShaiXuanActivity extends AppCompatActivity {


    private Button unit1,unit2,unit3;

    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiuzu_shaixuan);
        initView();
    }

    public void tip(View view){
        LogUtils.d("tip click");
        finish();
    }
    private void initView() {
        unit1 = findViewById(R.id.unit1);
        unit2 = findViewById(R.id.unit2);
        unit3 = findViewById(R.id.unit3);

    }

    private void resetUnit(){
        unit1.setSelected(false);
        unit2.setSelected(false);
        unit3.setSelected(false);

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


    public void submit(View view){

        LogUtils.d("type:"+type);

        Intent intent=new Intent();
        intent.putExtra("type", type);
        setResult(5,intent);
        finish();
    }

    public void setUnitData(View view){
        resetUnit();
        view.setSelected(true);
        Button u = (Button) view;
        type = u.getText().toString();
    }

}

package com.kufangdidi.www.activity.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.Api;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.utils.LogUtils;
import com.kufangdidi.www.view.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

public class UserIDAuthenticationActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout common_top_back;
    private TextView common_top_title, sfrz_text, yyzzrz_text;
    private ImageView sfrz_picture, yyzzrz_sfz_picture, yyzzrz_zhizhao_picture;
    private LinearLayout sfrz, yyzzrz, sfrz_layout, yyzzrz_layout;
    private View sfrz_view, yyzzrz_view;
    private EditText sfrz_name, sfrz_id, yyzzrz_name, yyzzrz_qiyename, yyzzrz_id;
    private Button sfrz_button, yyzzrz_button;
    private Boolean bool1 = true, bool2 = false;
    private ImagePicker imagePicker;
    private File tupian1, tupian2, tupian3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_id_authentication);
        BaseApplication.addDestoryActivity(this, "UserIDAuthenticationActivity");
        initView();
        initData();
    }

    private void initView() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_title = findViewById(R.id.common_top_title);
        sfrz_text = findViewById(R.id.sfrz_text);
        yyzzrz_text = findViewById(R.id.yyzzrz_text);
        sfrz_picture = findViewById(R.id.sfrz_picture);
        yyzzrz_sfz_picture = findViewById(R.id.yyzzrz_sfz_picture);
        yyzzrz_zhizhao_picture = findViewById(R.id.yyzzrz_zhizhao_picture);
        sfrz = findViewById(R.id.sfrz);
        yyzzrz = findViewById(R.id.yyzzrz);
        sfrz_layout = findViewById(R.id.sfrz_layout);
        yyzzrz_layout = findViewById(R.id.yyzzrz_layout);
        sfrz_view = findViewById(R.id.sfrz_view);
        yyzzrz_view = findViewById(R.id.yyzzrz_view);
        sfrz_name = findViewById(R.id.sfrz_name);
        sfrz_id = findViewById(R.id.sfrz_id);
        yyzzrz_name = findViewById(R.id.yyzzrz_name);
        yyzzrz_qiyename = findViewById(R.id.yyzzrz_qiyename);
        yyzzrz_id = findViewById(R.id.yyzzrz_id);
        sfrz_button = findViewById(R.id.sfrz_button);
        yyzzrz_button = findViewById(R.id.yyzzrz_button);
    }

    private void initData() {
        common_top_back.setOnClickListener(this);
        common_top_title.setText("");
        sfrz.setOnClickListener(this);
        yyzzrz.setOnClickListener(this);
        sfrz_button.setOnClickListener(this);
        yyzzrz_button.setOnClickListener(this);
        sfrz_picture.setOnClickListener(this);
        yyzzrz_sfz_picture.setOnClickListener(this);
        yyzzrz_zhizhao_picture.setOnClickListener(this);
    }

    private void myBool() {
        if (bool1) {
            sfrz_text.setTextColor(getResources().getColor(R.color.colorPink));
            sfrz_view.setVisibility(View.VISIBLE);
            sfrz_layout.setVisibility(View.VISIBLE);
        } else {
            sfrz_text.setTextColor(getResources().getColor(R.color.colorBlack));
            sfrz_view.setVisibility(View.INVISIBLE);
            sfrz_layout.setVisibility(View.INVISIBLE);
        }
        if (bool2) {
            yyzzrz_text.setTextColor(getResources().getColor(R.color.colorPink));
            yyzzrz_view.setVisibility(View.VISIBLE);
            yyzzrz_layout.setVisibility(View.VISIBLE);
        } else {
            yyzzrz_text.setTextColor(getResources().getColor(R.color.colorBlack));
            yyzzrz_view.setVisibility(View.INVISIBLE);
            yyzzrz_layout.setVisibility(View.INVISIBLE);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_top_back:
                finish();
                break;
            case R.id.sfrz:
                bool1 = true;
                bool2 = false;
                myBool();
                break;
            case R.id.yyzzrz:
                bool2 = true;
                bool1 = false;
                myBool();
                break;
            case R.id.sfrz_button:
                if (sfrz_name.length() < 1) {
                    Toast.makeText(UserIDAuthenticationActivity.this, "请输入姓名",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sfrz_id.length() < 1) {
                    Toast.makeText(UserIDAuthenticationActivity.this, "请输入身份证号码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sfrz_id.length() != 18) {
                    Toast.makeText(UserIDAuthenticationActivity.this, "请输入正确的身份证号码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tupian1 == null) {
                    Toast.makeText(UserIDAuthenticationActivity.this, "请上传您的身份证",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                shenFenRenZheng();
                break;
            case R.id.yyzzrz_button:
                LogUtils.d("yyzzrz_name" + yyzzrz_name.getText());
                if (yyzzrz_name.length() < 1) {
                    Toast.makeText(UserIDAuthenticationActivity.this, "请输入姓名",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (yyzzrz_qiyename.length() < 1) {
                    Toast.makeText(UserIDAuthenticationActivity.this, "请输入企业名称",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (yyzzrz_id.length() < 1) {
                    Toast.makeText(UserIDAuthenticationActivity.this, "请输入身份证号码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (yyzzrz_id.length() != 18) {
                    Toast.makeText(UserIDAuthenticationActivity.this, "请输入正确的身份证号码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tupian2 == null) {
                    Toast.makeText(UserIDAuthenticationActivity.this, "请上传您的身份证图片",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tupian3 == null) {
                    Toast.makeText(UserIDAuthenticationActivity.this, "请上传您的营业执照图片",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                yingYeZhiZhaorRenZheng();
                break;
            case R.id.sfrz_picture:
                initImagePicker();
                Intent intent_add1 = new Intent(UserIDAuthenticationActivity.this,
                        ImageGridActivity.class);
                startActivityForResult(intent_add1, 300);
                break;
            case R.id.yyzzrz_sfz_picture:
                initImagePicker();
                Intent intent_add2 = new Intent(UserIDAuthenticationActivity.this,
                        ImageGridActivity.class);
                startActivityForResult(intent_add2, 400);
                break;
            case R.id.yyzzrz_zhizhao_picture:
                initImagePicker();
                Intent intent_add3 = new Intent(UserIDAuthenticationActivity.this,
                        ImageGridActivity.class);
                startActivityForResult(intent_add3, 500);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 300) {
                ArrayList<ImageItem> images =
                        (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ImageItem imageItem = images.get(0);
                final File file = new File(imageItem.path);
                sfrz_picture.setImageURI(Uri.parse(file.getPath()));
                tuPian1(file);
                LogUtils.d("图片1本地路径：" + imageItem.path);
            }
            if (data != null && requestCode == 400) {
                ArrayList<ImageItem> images =
                        (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ImageItem imageItem = images.get(0);
                final File file = new File(imageItem.path);
                yyzzrz_sfz_picture.setImageURI(Uri.parse(file.getPath()));
                tuPian2(file);
                LogUtils.d("图片2本地路径：" + imageItem.path);
            }
            if (data != null && requestCode == 500) {
                ArrayList<ImageItem> images =
                        (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ImageItem imageItem = images.get(0);
                final File file = new File(imageItem.path);
                yyzzrz_zhizhao_picture.setImageURI(Uri.parse(file.getPath()));
                tuPian3(file);
                LogUtils.d("图片3本地路径：" + imageItem.path);
            }
        }
    }

    void tuPian1(File file) {
        tupian1 = file;
    }

    void tuPian2(File file) {
        tupian2 = file;
    }

    void tuPian3(File file) {
        tupian3 = file;
    }

    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setMultiMode(false);//单选多选
//        imagePicker.setSelectLimit(9);    //选中数量限制
//        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(324);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(510);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(324);//保存文件的宽度。单位像素
//        imagePicker.setOutPutY(510);//保存文件的高度。单位像素
//        imagePicker.setImageLoader(new GlideImageLoader());
    }

    private void shenFenRenZheng() {
        OkGo.post(Api.BASE_URL + "/realNameSystem/addRealNameSystem")
                .tag(this)
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("type", "1")
                .params("realName", sfrz_name.getText().toString())
                .params("identityCard", sfrz_id.getText().toString())
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("identityCardImgFile ", tupian1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("身份认证 返回结果s" + s);
                        JSONObject jsonObject = JSON.parseObject(s);
                        if (jsonObject.getInteger("code") == 0) {
                            Toast.makeText(UserIDAuthenticationActivity.this, "提交成功，请等待审核！",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else if (jsonObject.getInteger("code") == 100108) {
                            Toast.makeText(UserIDAuthenticationActivity.this, "您已提交过，请耐心等待！",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(UserIDAuthenticationActivity.this,
                                    jsonObject.getString("msg") + jsonObject.getInteger("code"),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(UserIDAuthenticationActivity.this, "请检查网络或遇到未知错误！",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void yingYeZhiZhaorRenZheng() {
        OkGo.post(Api.BASE_URL + "/realNameSystem/addRealNameSystem")
                .tag(this)
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("type", "2")
                .params("realName", yyzzrz_name.getText().toString())
                .params("identityCard", yyzzrz_id.getText().toString())
                .params("businessName", yyzzrz_qiyename.getText().toString())
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("identityCardImgFile ", tupian2)
                .params("enterpriseCertificationFile", tupian3)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("营业执照认证 返回结果s" + s);
                        JSONObject jsonObject = JSON.parseObject(s);
                        if (jsonObject.getInteger("code") == 0) {
                            Toast.makeText(UserIDAuthenticationActivity.this, "提交成功，请等待审核！",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else if (jsonObject.getInteger("code") == 100108) {
                            Toast.makeText(UserIDAuthenticationActivity.this, "您已提交过，请耐心等待！",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(UserIDAuthenticationActivity.this,
                                    jsonObject.getString(
                                            "msg") + jsonObject.getInteger("code"),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(UserIDAuthenticationActivity.this, "请检查网络或遇到未知错误！"
                                , Toast.LENGTH_LONG).show();
                    }
                });
    }
}

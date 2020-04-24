package com.kufangdidi.www.activity.user.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.service.MainService;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;
import com.kufangdidi.www.utils.common;
import com.kufangdidi.www.view.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout common_top_back;
    private TextView common_top_title;

    private RelativeLayout touxiang, name, gender, phone, create_time, main_area, introduction;
    private TextView tv_name, tv_gender, tv_phone, tv_create_time;
    private ImagePicker imagePicker;
    private ImageView image_touxiang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        BaseApplication.addDestoryActivity(this, "UserInfoActivity");
        initTitle();
        initView();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name:
                startActivity(new Intent(UserInfoActivity.this, UserInfoUpdateNameActivity.class));
                break;
            case R.id.gender:
                startActivity(new Intent(UserInfoActivity.this, UserInfoUpdateGenderActivity.class));
                break;
            case R.id.main_area:
                startActivity(new Intent(UserInfoActivity.this, UserInfoUpdateAreaActivity.class));
                break;
            case R.id.introduction:
                startActivity(new Intent(UserInfoActivity.this, UserInfoUpdateIntroductionActivity.class));
                break;
            case R.id.touxiang:
                initImagePicker();
                Intent intent_add = new Intent(UserInfoActivity.this, ImageGridActivity.class);
                startActivityForResult(intent_add, 200);
                break;
            case R.id.common_top_back:
                finish();
                break;

        }
    }

    private void initView() {
        image_touxiang = findViewById(R.id.image_touxiang);
        if (BaseApplication.getSpUtils().getString("avatarUrl") != null && BaseApplication.getSpUtils().getString("avatarUrl").length() != 0) {
            Picasso.with(this)
                    .load(Constant.SERVER_PIC_HOSTds + BaseApplication.getSpUtils().getString(
                            "avatarUrl"))
                    .into(image_touxiang);
        }
        name = findViewById(R.id.name);
        name.setOnClickListener(this);
        gender = findViewById(R.id.gender);
        gender.setOnClickListener(this);
        phone = findViewById(R.id.phone);
        create_time = findViewById(R.id.create_time);
        tv_name = findViewById(R.id.tv_name);
        tv_gender = findViewById(R.id.tv_gender);
        tv_phone = findViewById(R.id.tv_phone);
        tv_create_time = findViewById(R.id.tv_create_time);
        main_area = findViewById(R.id.main_area);
        main_area.setOnClickListener(this);
        introduction = findViewById(R.id.introduction);
        introduction.setOnClickListener(this);
        if(BaseApplication.getSpUtils().getInt("type")!=2){
            main_area.setVisibility(View.INVISIBLE);
            introduction.setVisibility(View.INVISIBLE);
        }
        touxiang = findViewById(R.id.touxiang);
        touxiang.setOnClickListener(this);
    }

    private void initData() {
        if (BaseApplication.getSpUtils().getString("userName") != null && !"".equals(BaseApplication.getSpUtils().getString("userName"))) {
            tv_name.setText(BaseApplication.getSpUtils().getString("userName"));
        }
        if (1 == BaseApplication.getSpUtils().getInt("userSex")) {
            tv_gender.setText("女");
        } else {
            tv_gender.setText("男");
        }
        tv_phone.setText(BaseApplication.getSpUtils().getString("userPhone"));
        if (BaseApplication.getSpUtils().getString("createTime") != null && !"".equals(BaseApplication.getSpUtils().getString("createTime"))) {
            tv_create_time.setText(common.getTimeDay((long) new Integer(BaseApplication.getSpUtils().getString("createTime")) * 1000));
        }
        /*
        if(null != UserModal.getInstance().get()){
            Picasso.with(this)
                    .load(Constant.SERVER_FILE_HOST+ UserModal.getInstance().getCover())
                    .into(image_touxiang);
        }
        */
    }

    private void initTitle() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_title = findViewById(R.id.common_top_title);
        common_top_back.setOnClickListener(this);
        common_top_title.setText("个人信息");
    }

    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setMultiMode(false);//单选多选
//        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(500);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(500);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(500);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(500);//保存文件的高度。单位像素
        imagePicker.setImageLoader(new GlideImageLoader());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 200) {
                ArrayList<ImageItem> images =
                        (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ImageItem imageItem = images.get(0);
                // final String filename = common.splitName(imageItem.path);
                final File file = new File(imageItem.path);
                // img_uploading.setVisibility(View.VISIBLE);
                image_touxiang.setImageURI(Uri.parse(file.getPath()));
                UpdateUserInfoByTouXiang(file);

                LogUtils.d("图片本地路径：" + imageItem.path);
            }

        }
    }

    private void UpdateUserInfoByTouXiang(File file) {
        LogUtils.d("file" + file);
        OkGo.post(Constant.SERVER_HOST + "/personal/updateProfilePicture")
                .tag(this)
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("file", file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("findBannerListByServer 返回结果s:" + s);
                        JSONObject dataJson = null;
                        try {
                            dataJson = new JSONObject(s);
                            int code = (int) dataJson.get("code");

                            if (code == 0) {
                                //修改成功
                                // UserModal.setInstance(new Gson().fromJson(dataJson.getString
                                // ("data"),UserModal.class));
                                //UserModal.setInstance(new Gson().fromJson(dataJson.getString
                                // ("data"),UserModal.class));
                                //修改成功
                                Intent intent = new Intent(UserInfoActivity.this,
                                        MainService.class);
                                intent.setAction("userAction");
                                intent.putExtra("method", MainService.initService);
                                startService(intent);

                            } else {
                                Toast.makeText(UserInfoActivity.this,
                                        (String) dataJson.get("msg"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LogUtils.d("返回结果出错:" + e.getMessage());
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //耗时操作
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                    }
                }, 1500);
                //该方法将Runnable中更新UI的代码传送到主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {// 运行在UI线程里面的
                        initData();
                    }
                });
            }
        }).start();
        LogUtils.d("改名");
    }
}

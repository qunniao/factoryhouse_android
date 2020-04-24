package com.kufangdidi.www.chat.utils.imagepicker;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kufangdidi.www.R;

import java.io.File;

public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImages(Activity activity, String path, ImageView imageView, int width, int height) {
        RequestOptions requestOptions = new RequestOptions()
                .error(R.mipmap.default_image)       //设置错误图片
                .placeholder(R.mipmap.default_image)//设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(activity)//配置上下文
                .load(Uri.fromFile(new File(path)))
                .apply(requestOptions)//设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .into(imageView);
    }



    @Override
    public void clearMemoryCache() {

    }
}

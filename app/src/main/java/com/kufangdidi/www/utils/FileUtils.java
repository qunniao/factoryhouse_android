package com.kufangdidi.www.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by guojinyu on 2017/7/30.
 */

public class FileUtils {
    private String SDPATH;

    public String getSDPATH() {
        return SDPATH;
    }
    public FileUtils() {
        //得到当前外部存储设备的目录
        // /SDCARD
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }



    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public File creatSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     */
    public File creatSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 删除SD卡上的文件
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     * @param   filePath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String filePath) {
        LogUtils.d("deleteDirectory  "+filePath);
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            LogUtils.d("文件不存在 "+filePath);
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        LogUtils.d("files length:"+files.length);
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    LogUtils.d("删除子文件失败");
                    break;
                }
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    LogUtils.d("删除子目录失败");
                    break;
                }
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }


    public long getFileSize(File f) throws Exception
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++)
        {
            if (flist[i].isDirectory())
            {
                size = size + getFileSize(flist[i]);
            }
            else
            {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public String FormetFileSize(long fileS)
    {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024)
        {
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576)
        {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        }
        else if (fileS < 1073741824)
        {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        }
        else
        {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }


    /**
     * 获取文件名
     */
    public String getUrlName(String name, String url){
        String[] dots = url.split("\\.");
        LogUtils.d("dots:"+dots.length+"  url:"+url);
        name = name.trim()+"."+dots[dots.length-1];
        return name;
    }


    /**
     * 判断SD卡上的文件夹是否存在
     */
    public boolean isFileExist(String fileName){
        File file = new File(SDPATH + fileName);
        boolean flag = file.exists();
        if(!flag){
            LogUtils.d("文件不存在："+SDPATH + fileName);
        }
        return flag;
    }

    public boolean isFileExist2(String fileName){
        File file = new File(fileName);
        boolean flag = file.exists();
        if(!flag){
            LogUtils.d("文件不存在："+ fileName);
        }
        return flag;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public File write2SDFromInput(String path, String fileName, InputStream input){
        File file = null;
        OutputStream output = null;
        try{
            creatSDDir(path);
            file = creatSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte buffer [] = new byte[4 * 1024];
            while((input.read(buffer)) != -1){
                output.write(buffer);
            }
            output.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                output.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return file;
    }

    public static int getWordCount(String s)
    {
        s = s.replaceAll("[^\\x00-\\xff]", "**");
        int length = s.length();
        return length;
    }

    /**
     * 写入配置文件
     * @param
     */
    public void writeConfig(String content, String txt) {
        LogUtils.d("writeConfig写入配置文件:");
        File file = new File(SDPATH+Constant.CONFIG_PATH+txt);
        if (file.exists()) {
            file.delete();
        }
        try {
            creatSDDir(Constant.CONFIG_PATH);
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取配置文件
     * @return
     */
    public String getConfig(String txt) {
        FileInputStream fis = null;
        InputStreamReader is = null;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            File txtFile = new File(SDPATH+Constant.CONFIG_PATH+txt);
            if (!txtFile.exists()) {
                return null;
            }
            fis = new FileInputStream(txtFile);
            is = new InputStreamReader(fis);
            reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (is != null) {
                    is.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /**
     * 生成文件
     * @param
     */
    public void writeTxt(String txt, String pa) {
        LogUtils.d("writeTxt写入sd卡文件:"+SDPATH+pa);
        File file = new File(SDPATH+pa);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(txt);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取 sd 卡 文件内容
     * @return
     */
    public String getFileContent(String path) {
        FileInputStream fis = null;
        InputStreamReader is = null;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            File txtFile = new File(SDPATH+path);
            if (!txtFile.exists()) {
                return "";
            }
            fis = new FileInputStream(txtFile);
            is = new InputStreamReader(fis);
            reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (is != null) {
                    is.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 写入对象-播放列表
     */
    public void writeObject(Object object, String path) {
        try {
            deleteFile(path);
            creatSDFile(path);
            FileOutputStream outStream = new FileOutputStream(SDPATH+path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(object);
            outStream.close();
            LogUtils.d("writeObject successful ");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.d("writeObject 异常"+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.d("writeObject 异常"+e.getMessage());
        }

    }

    /**
     * 读取对象-播放列表
     */
    public Object readObject(String path) {
        Object o = null;
        FileInputStream freader;
        try {
            if(isFileExist(path)){
                freader = new FileInputStream(SDPATH + path);
                ObjectInputStream objectInputStream = new ObjectInputStream(freader);
                o =  objectInputStream.readObject();
                LogUtils.d("readObject successful");
            }else{
                LogUtils.d("readObject 文件不存在 "+path);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return o;
    }


    public void copyFilesFassets(Context context, String fileName, String path) {
        InputStream is;
        byte[] buffer = new byte[1024];
        int byteCount;
        //新的autoui.jar
        //String newpath = Environment.getExternalStorageDirectory().getAbsolutePath()
        //       + File.separator + "qunkong" + File.separator + fileName;


        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            else{
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            /*
            File newfile = new File(newpath);
            if (newfile.exists()) {
                is = new FileInputStream(newfile);
            }else{
                is = context.getResources().getAssets().open(fileName);
            }
            */
            is = context.getResources().getAssets().open(fileName);
            while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
            }
            fos.flush();//刷新缓冲区
            is.close();
            fos.close();
        } catch (Exception e) {
            LogUtils.d("copyFilesFassets error");
            e.printStackTrace();
        }
    }

// Storage Permissions
   private static final int REQUEST_EXTERNAL_STORAGE = 1;
   private static String[] PERMISSIONS_STORAGE = {
                     Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE };

          /**
     * Checks if the app has permission to write to device storage
      *
    * If the app does not has permission then the user will be prompted to
   * grant permissions
   *
     * @param activity
     */

    public static void verifyStoragePermissions(Activity activity) {
       int permission = ActivityCompat.checkSelfPermission(activity,
                       Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                             REQUEST_EXTERNAL_STORAGE);
            }
    }

    public void notifyFileSystemChanged(Context context, String path) {
        if (path == null)
            return;
        final File f = new File(path);
        if (Build.VERSION.SDK_INT >= 19 /*Build.VERSION_CODES.KITKAT*/) { //添加此判断，判断SDK版本是不是4.4或者高于4.4
            String[] paths = new String[]{path};
            MediaScannerConnection.scanFile(context, paths, null, null);
        } else {
            final Intent intent;
            if (f.isDirectory()) {
                intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
                intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
                intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
                LogUtils.d("directory changed, send broadcast:" + intent.toString());
            } else {
                intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(new File(path)));
                LogUtils.d("file changed, send broadcast:" + intent.toString());
            }
            context.sendBroadcast(intent);
        }
    }

    /*
     * 从网络上获取图片，如果图片在本地存在的话就直接拿，如果不存在再去服务器上下载图片
     * 这里的path是图片的地址
     */

    public String getImageURI(String path, File cache) throws Exception {
        // 如果图片存在本地缓存目录，则不去服务器下载
        if (cache.exists()) {
            return cache.getPath();//Uri.fromFile(path)这个方法能得到文件的URI
        } else {
            creatSDDir(Constant.CACHE_DATA);
            creatSDDir(Constant.CACHE_DATA+"images");
            // 从网络上获取图片
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(cache);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
                // 返回一个URI对象
                return cache.getPath();
            }
        }
        return null;
    }

    public static void uploadFile(File file, String oldImage, Context context) {
        LogUtils.d("上传单个文件 key:"+Constant.key);
        if(oldImage==null){
            oldImage="";
        }else{
            oldImage = common.splitName(oldImage);
        }
        if("-1".equals(Constant.key)){
            return;
        }
        OkGo.post(Constant.SERVER_UPLOAD_HOST+"/mobile/upload/uploadFile.html")
                .tag(context)
                .params("key", Constant.key)
                .params("oldImage", oldImage)
                .params("file", file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                    }
                });
    }

    public static void uploadMoreFile(List<File> files, String oldImages, Context context) {
        LogUtils.d("上传多个文件 key:"+Constant.key);
        if(oldImages==null)oldImages="";
        if("-1".equals(Constant.key)){
            return;
        }
        PostRequest request = OkGo.post(Constant.SERVER_UPLOAD_HOST+"/mobile/upload/uploadMoreFiles.html")
                .tag(context);
        request.params("oldImages", oldImages);
        request.params("key", Constant.key);
        for(int i=0;i<files.size();i++){
            request.params("myfiles", files.get(i));
            LogUtils.d("多文件上传 file"+i);
        }

        request.execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                    }
                });
    }

    public static void deleteServerFile(String oldImage, Context context) {
        LogUtils.d("删除服务器图片 "+oldImage);
        if(oldImage==null){
            oldImage="";
        }else{
            oldImage = common.splitName(oldImage);
        }
        if("-1".equals(Constant.key)){
            return;
        }
        OkGo.post(Constant.SERVER_UPLOAD_HOST+"/mobile/upload/deleteFile.html")
                .tag(context)
                .params("key", Constant.key)
                .params("filename", oldImage)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                    }
                });
    }

}


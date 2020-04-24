package com.kufangdidi.www.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.HouseQiuZuBean;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/*
 * Effect:
 * Author: jww
 * Date: 2018/11/17
 */
public class QiuZuAdapter extends RecyclerView.Adapter<QiuZuAdapter.ViewHolder> {

    private Context context;
    private List<HouseQiuZuBean.DataBean> list2;
    private String types, status;

    public interface OnCallBack {
        void onClick(String state);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private OnCallBack onCallBack;

    public QiuZuAdapter(Context context) {
        this.context = context;
    }

    public void setdata(List<HouseQiuZuBean.DataBean> data) {
        if (list2 == null) list2 = new ArrayList<HouseQiuZuBean.DataBean>();
        list2.clear();
        list2.addAll(data);
        LogUtils.d("QiuZuAdapter list size:" + list2.size());
    }

    public List<HouseQiuZuBean.DataBean> getData() {
        return list2;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_user_fabu_qiuzu,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final HouseQiuZuBean.DataBean data = list2.get(position);
        LogUtils.d("name:" + data + " position:" + position);
        holder.productId.setText(data.getProductId());
        holder.title.setText(data.getTitle());
        holder.createTime.setText(data.getCreateTime());

        if (data.getType() == 1) types = "厂房-";
        if (data.getType() == 2) types = "仓库-";
        if (data.getType() == 3) types = "土地-";
        if (data.getStatus() == 1) status = "求租";
        if (data.getStatus() == 2) status = "求购";
        holder.types_status.setText(types + status);
        if (data.getIsEnd().equals("2")) {
            holder.shangxiajia.setText("上架");
        }
        holder.shangxiajia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.shangxiajia.getText().equals("上架")) {
                    OkGo.post(Constant.SERVER_HOST + "/buyingPurchase/updateBuyingPurchaseByIsEnd")
                            .tag(this)
                            .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                            .params("token", BaseApplication.getSpUtils().getString("token"))
                            .params("bpid", data.getBpid())
                            .params("isEnd", 1)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    LogUtils.d("上架 返回结果s" + s);
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        int code = (int) jsonObject.get("code");
                                        if (code == 0) {
                                            holder.shangxiajia.setText("下架");
                                            Toast.makeText(context, "上架成功！",
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context, "上架失败！",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    Toast.makeText(context, "请检查网络或遇到未知错误！",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    OkGo.post(Constant.SERVER_HOST + "/buyingPurchase/updateBuyingPurchaseByIsEnd")
                            .tag(this)
                            .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                            .params("token", BaseApplication.getSpUtils().getString("token"))
                            .params("bpid", data.getBpid())
                            .params("isEnd", 2)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    LogUtils.d("下架 返回结果s" + s);
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        int code = (int) jsonObject.get("code");
                                        if (code == 0) {
                                            holder.shangxiajia.setText("上架");
                                            Toast.makeText(context, "下架成功！",
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context, "下架失败！",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    Toast.makeText(context, "请检查网络或遇到未知错误！",
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                }
            }
        });
        holder.shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.post(Constant.SERVER_HOST + "/buyingPurchase/deleteBuyingPurchase")
                        .tag(this)
                        .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                        .params("token", BaseApplication.getSpUtils().getString("token"))
                        .params("bpid", data.getBpid())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                LogUtils.d("删除 返回结果s" + s);
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    int code = (int) jsonObject.get("code");
                                    if (code == 0) {
                                        list2.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "删除成功！",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(context, "删除失败！",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                Toast.makeText(context, "请检查网络或遇到未知错误！",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView productId, title, types_status, createTime, shangxiajia, shanchu;

        public ViewHolder(View itemView) {
            super(itemView);
            //绑定组件
            productId = itemView.findViewById(R.id.qiuzu_productId);
            title = itemView.findViewById(R.id.qiuzu_title);
            types_status = itemView.findViewById(R.id.qiuzu_types_status);
            createTime = itemView.findViewById(R.id.qiuzu_createTime);
            shangxiajia = itemView.findViewById(R.id.shangxiajia);
            shanchu = itemView.findViewById(R.id.shanchu);
        }
    }
}

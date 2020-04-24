package com.kufangdidi.www.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.chat.activity.ChatActivity;
import com.kufangdidi.www.chat.entity.Event;
import com.kufangdidi.www.chat.utils.SortConvList;
import com.kufangdidi.www.chat.utils.SortTopConvList;
import com.kufangdidi.www.chat.utils.TimeFormat;
import com.kufangdidi.www.chat.view.SwipeLayoutConv;
import com.kufangdidi.www.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.PromptContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.MessageReceiptStatusChangeEvent;
import cn.jpush.im.android.api.event.MessageRetractEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

import static com.kufangdidi.www.chat.entity.EventType.createConversation;

//import com.nearservice.ling.activity.main.PushListActivity;
//import com.zhanchengwlkj.www.model.DbHistory;
//import com.zhanchengwlkj.www.model.Push;


/**
 * Created by admin on 2017/5/16.
 */

public class FragmentChat extends Fragment implements AdapterView.OnItemClickListener{
   // private DBManager db;
    //private Push push;
    private ListView lv_list;
    private Activity mContext;
    private NetworkReceiver mReceiver;
    private ConversationListAdapter adapter;
    private List<Conversation> mDatas = new ArrayList<Conversation>();
    private BackgroundHandler mBackgroundHandler;
    private static final int REFRESH_CONVERSATION_LIST = 0x3000;
    private static final int DISMISS_REFRESH_HEADER = 0x3001;
    private static final int ROAM_COMPLETED = 0x3002;
    private HandlerThread mThread;
    private boolean shouldPlayBeep=true;
    MediaPlayer mediaPlayer;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_CONVERSATION_LIST: {
                    LogUtils.d("Handler REFRESH_CONVERSATION_LIST");
                    if (adapter == null) {
                        adapter = new ConversationListAdapter();
                        lv_list.setAdapter(adapter);
                        updateListHeight();
                    }else{
                        adapter.notifyDataSetChanged();
                        updateListHeight();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //订阅接收消息,子类只要重写onEvent就能收到消息
        JMessageClient.registerEventReceiver(this);
        //db = new DBManager(mContext);
        return inflater.inflate(R.layout.tab3, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d("FragmentChat 启动");
        View view = getView();
        mContext = this.getActivity();

        lv_list = (ListView) view.findViewById(R.id.lv_list);
        lv_list.setOnItemClickListener(this);
        JMessageClient.registerEventReceiver(this);
        initReceiver();
        if(JMessageClient.getMyInfo()!=null){
            initListView();
        }
        //findNewPush();
    }


    private void updateListHeight(){
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, lv_list);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lv_list.getLayoutParams();

        params.height = totalHeight
                + (lv_list.getDividerHeight() * (adapter.getCount() - 1))+50;
        lv_list.setLayoutParams(params);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击会话条目
        Intent intent = new Intent();

        //这里-3是减掉添加的三个headView
        LogUtils.d("position:"+position);
        Conversation conv = mDatas.get(position);
        LogUtils.d("onItemClick title:"+conv.getTitle()+" conv:"+conv.toString());

        UserInfo userInfo = (UserInfo) conv.getTargetInfo();
        if(userInfo==null)return;

        String targetId = userInfo.getUserName();
        intent.putExtra(BaseApplication.TARGET_ID, targetId);
        intent.putExtra(BaseApplication.CONV_TITLE, userInfo.getNickname());
        intent.putExtra(BaseApplication.TARGET_APP_KEY, conv.getTargetAppKey());
        intent.putExtra(BaseApplication.DRAFT, adapter.getDraft(conv.getId()));

        intent.setClass(mContext, ChatActivity.class);
        mContext.startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter==null)return;
        mHandler.sendEmptyMessage(REFRESH_CONVERSATION_LIST);
    }

    private void initReceiver() {
        mReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mContext.registerReceiver(mReceiver, filter);
    }

    public void initListView(){

        mThread = new HandlerThread("MainActivity");
        mThread.start();
        mBackgroundHandler = new BackgroundHandler(mThread.getLooper());

        mDatas = JMessageClient.getConversationList();
        if(mDatas==null)mDatas = new ArrayList<>();
        //如果未登录，消息清空，这里测试，先注销
       // if("-1".equals(Constant.key))mDatas.clear();
        LogUtils.d("initListView mdatas size:"+mDatas.size());
        if (adapter == null) {
            adapter = new ConversationListAdapter();
            lv_list.setAdapter(adapter);
            updateListHeight();
        }else{
            mHandler.sendEmptyMessage(REFRESH_CONVERSATION_LIST);
        }
    }
    //监听网络状态的广播
    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeInfo = manager.getActiveNetworkInfo();
                /*
                if (null == activeInfo) {
                    mConvListView.showHeaderView();
                } else {
                    mConvListView.dismissHeaderView();
                }
                */
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /*
    获取最新通知
    private void findNewPush() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkGo.get(Constant.SERVER_HOST+"/mobile/ad/findNewPush.html?key="+Constant.key)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                if(s!=null){
                                    LogUtils.d("findNewPush s:"+s);
                                    JSONObject jsonObject = null;

                                    try {
                                        jsonObject = new JSONObject(s);
                                        push = new Gson().fromJson(jsonObject.get("model").toString(),Push.class);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(push==null){return;}

                                    DbHistory model = db.queryHistory("push");
                                    if(model==null)model = new DbHistory();
                                    model.setType("push");
                                    model.setHistory_id(push.getId());
                                    db.deleteHistoryByType("push");
                                    db.addHistory(model);
                                    LogUtils.d("push title:"+push.getTitle());

                                    LogUtils.d("push id:"+push.getId()+" history id:"+model.getHistory_id());

                                    final int model_id = model.getHistory_id();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            tv_notification.setText(push.getTitle());
                                            if(push.getId()>model_id){
                                                noti_num.setVisibility(View.VISIBLE);
                                            }else{
                                                noti_num.setVisibility(View.INVISIBLE);
                                            }

                                        }
                                    });






                                }
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                            }
                        });
            }
        }).start();

    }
*/

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        try{
            mContext.unregisterReceiver(mReceiver);
            mBackgroundHandler.removeCallbacksAndMessages(null);
            mThread.getLooper().quit();
        }catch (Exception e){
            e.printStackTrace();
        }

        super.onDestroy();
    }

    public void refreshAdapter(){
        LogUtils.d("刷新列表");
        mHandler.sendEmptyMessage(REFRESH_CONVERSATION_LIST);
    }
    /**
     * 收到消息
     */
    public void onEvent(MessageEvent event) {
        LogUtils.d("收到消息");

        Intent intent = new Intent();
        intent.setAction("setUnReadMsg");
        intent.putExtra("count",JMessageClient.getAllUnReadMsgCount());
        mContext.sendBroadcast(intent);

        Message msg = event.getMessage();

        final UserInfo userInfo = (UserInfo) msg.getTargetInfo();
        String targetId = userInfo.getUserName();
        Conversation conv = JMessageClient.getSingleConversation(targetId, userInfo.getAppKey());
        if(conv==null)return;
        if (adapter != null) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(userInfo.getAvatar())) {
                        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                            @Override
                            public void gotResult(int responseCode, String responseMessage, Bitmap avatarBitmap) {
                                if (responseCode == 0) {
                                    LogUtils.d("什么鬼");
                                    mHandler.sendEmptyMessage(REFRESH_CONVERSATION_LIST);
                                }
                            }
                        });
                    }
                }
            });
            //mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST, conv));
            setToTop(conv);
        }

    }

    /**
     * 接收离线消息
     *
     * @param event 离线消息事件
     */
    public void onEvent(OfflineMessageEvent event) {
        LogUtils.d("接收离线消息");
        Conversation conv = event.getConversation();
        if (!conv.getTargetId().equals("feedback_Android")) {
            mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST, conv));
        }
    }

    /**
     * 消息撤回
     */
    public void onEvent(MessageRetractEvent event) {
        LogUtils.d("消息撤回");
        Conversation conversation = event.getConversation();
        mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST, conversation));
    }

    /**
     * 消息已读事件
     */
    public void onEventMainThread(MessageReceiptStatusChangeEvent event) {
        LogUtils.d("消息已读事件");
        mHandler.sendEmptyMessage(REFRESH_CONVERSATION_LIST);
    }

    /**
     * 消息漫游完成事件
     *
     * @param event 漫游完成后， 刷新会话事件
     */
    public void onEvent(ConversationRefreshEvent event) {
        LogUtils.d("消息漫游完成事件");
        Conversation conv = event.getConversation();
        if (!conv.getTargetId().equals("feedback_Android")) {
            mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST, conv));
            //多端在线未读数改变时刷新
            if (event.getReason().equals(ConversationRefreshEvent.Reason.UNREAD_CNT_UPDATED)) {
                mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST, conv));
            }
        }
    }

    private class BackgroundHandler extends Handler {
        public BackgroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            LogUtils.d("消息handleMessage");
            Intent intent = new Intent();
            intent.setAction("setUnReadMsg");
            intent.putExtra("count",JMessageClient.getAllUnReadMsgCount());
            mContext.sendBroadcast(intent);
            switch (msg.what) {
                case REFRESH_CONVERSATION_LIST:
                    LogUtils.d("1111");
                    Conversation conv = (Conversation) msg.obj;
                    setToTop(conv);
                    LogUtils.d("1111---标题："+conv.getTitle()+" string:"+conv.toString());
                    break;
                case DISMISS_REFRESH_HEADER:
                    LogUtils.d("222");
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // mConvListView.dismissLoadingHeader();
                        }
                    });
                    break;
                case ROAM_COMPLETED:
                    LogUtils.d("333");
                    conv = (Conversation) msg.obj;
                    adapter.addAndSort(conv);
                    break;
            }
        }
    }

    public void onEventMainThread(Event event) {
        LogUtils.d("fragmentTwo收到onEventMainThread "+event.getType()+"  "+createConversation);
        switch (event.getType()) {
            case createConversation:
                LogUtils.d("onEventMainThread 1");
                Conversation conv = event.getConversation();
                if (conv != null) {
                    LogUtils.d("onEventMainThread 2");
                    adapter.addNewConversation(conv);
                }
                break;
            case deleteConversation:
                conv = event.getConversation();
                if (null != conv) {
                    adapter.deleteConversation(conv);
                }
                break;
            //收到保存为草稿事件
            case draft:
                LogUtils.d("fragmentTwo onEventMainThread 草稿");
                conv = event.getConversation();
                String draft = event.getDraft();
                //如果草稿内容不为空，保存，并且置顶该会话
                if (!TextUtils.isEmpty(draft)) {
                    LogUtils.d("fragmentTwo onEventMainThread 置顶草稿");
                    adapter.putDraftToMap(conv, draft);
                    setToTop(conv);
                    //否则删除
                } else {
                    LogUtils.d("fragmentTwo onEventMainThread 删除草稿");
                    adapter.delDraftFromMap(conv);
                }
                break;
            case addFriend:
                break;
        }
        Intent intent = new Intent();
        intent.setAction("setUnReadMsg");
        intent.putExtra("count",JMessageClient.getAllUnReadMsgCount());
        mContext.sendBroadcast(intent);
    }

    /**
     * 收到消息后将会话置顶
     *
     * @param conv 要置顶的会话
     */
    public void setToTop(Conversation conv) {

        //如果是旧的会话

        for (Conversation conversation : mDatas) {
            if (conv.getId().equals(conversation.getId())) {
                LogUtils.d("setToTop 删除旧的会话");
                //因为后面要改变排序位置,这里要删除
                mDatas.remove(conversation);
            }
        }
        if (mDatas.size() == 0) {
            mDatas.add(conv);
        } else {
            //如果是新的会话,直接去掉置顶的消息数之后就插入到list中
            mDatas.add(conv);
            LogUtils.d("setToTop 添加新会话");
        }
        mHandler.sendEmptyMessageDelayed(REFRESH_CONVERSATION_LIST, 1000);

    }

    //置顶会话
    public void setConvTop(Conversation conversation) {
        int count = 0;
        //遍历原有的会话,得到有几个会话是置顶的
        for (Conversation conv : mDatas) {
            if (!TextUtils.isEmpty(conv.getExtra())) {
                count++;
            }
        }
        conversation.updateConversationExtra(count + "");
        mDatas.remove(conversation);
        mDatas.add(count, conversation);
        mHandler.sendEmptyMessageDelayed(REFRESH_CONVERSATION_LIST, 500);

    }


    class ConversationListAdapter extends BaseAdapter {
        private Map<String, String> mDraftMap = new HashMap<>();
        private SparseBooleanArray mArray = new SparseBooleanArray();
        private SparseBooleanArray mAtAll = new SparseBooleanArray();
        private HashMap<Conversation, Integer> mAtConvMap = new HashMap<>();
        private HashMap<Conversation, Integer> mAtAllConv = new HashMap<>();
        private UserInfo mUserInfo;

        public ConversationListAdapter() {
        }


        @Override
        public int getCount() {
            if (mDatas == null) {
                return 0;
            }
            return mDatas.size();
        }

        @Override
        public Conversation getItem(int position) {
            if (mDatas == null) {
                return null;
            }
            LogUtils.d("getItem:"+mDatas.get(position));
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            LogUtils.d("getView");
            final Conversation convItem = mDatas.get(position);
            LogUtils.d("position:"+position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_conv_list, null);
            }
            final ImageView headIcon = (ImageView) convertView.findViewById(R.id.msg_item_head_icon);
            TextView convName = (TextView) convertView.findViewById(R.id.conv_item_name);
            TextView content = (TextView) convertView.findViewById(R.id.msg_item_content);
            TextView datetime = (TextView) convertView.findViewById(R.id.msg_item_date);
            TextView newGroupMsgNumber = (TextView) convertView.findViewById(R.id.new_group_msg_number);
            TextView newMsgNumber = (TextView) convertView.findViewById(R.id.new_msg_number);
            ImageView groupBlocked = (ImageView) convertView.findViewById(R.id.iv_groupBlocked);
            ImageView newMsgDisturb = (ImageView) convertView.findViewById(R.id.new_group_msg_disturb);
            ImageView newGroupMsgDisturb = (ImageView) convertView.findViewById(R.id.new_msg_disturb);

            final SwipeLayoutConv swipeLayout = (SwipeLayoutConv) convertView.findViewById(R.id.swp_layout);
            final TextView delete = (TextView) convertView.findViewById(R.id.tv_delete);

            String draft = mDraftMap.get(convItem.getId());
            if (!TextUtils.isEmpty(convItem.getExtra())) {
                swipeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorGray));
            } else {
                swipeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
            }

            //如果会话草稿为空,显示最后一条消息
            if (TextUtils.isEmpty(draft)) {
                Message lastMsg = convItem.getLatestMessage();
                if (lastMsg != null) {
                    TimeFormat timeFormat = new TimeFormat(mContext, lastMsg.getCreateTime());
//                //会话界面时间
                    datetime.setText(timeFormat.getTime());
                    String contentStr;
                    switch (lastMsg.getContentType()) {
                        case image:
                            contentStr = mContext.getString(R.string.type_picture);
                            break;
                        case voice:
                            contentStr = mContext.getString(R.string.type_voice);
                            break;
                        case location:
                            contentStr = mContext.getString(R.string.type_location);
                            break;
                        case file:
                            String extra = lastMsg.getContent().getStringExtra("video");
                            if (!TextUtils.isEmpty(extra)) {
                                contentStr = mContext.getString(R.string.type_smallvideo);
                            } else {
                                contentStr = mContext.getString(R.string.type_file);
                            }
                            break;
                        case video:
                            contentStr = mContext.getString(R.string.type_video);
                            break;
                        case eventNotification:
                            contentStr = mContext.getString(R.string.group_notification);
                            break;
                        case custom:
                            CustomContent customContent = (CustomContent) lastMsg.getContent();
                            Boolean isBlackListHint = customContent.getBooleanValue("blackList");
                            if (isBlackListHint != null && isBlackListHint) {
                                contentStr = mContext.getString(R.string.jmui_server_803008);
                            } else {
                                contentStr = mContext.getString(R.string.type_custom);
                            }
                            break;
                        case prompt:
                            contentStr = ((PromptContent) lastMsg.getContent()).getPromptText();
                            break;
                        default:
                            contentStr = ((TextContent) lastMsg.getContent()).getText();
                            break;
                    }

                    MessageContent msgContent = lastMsg.getContent();
                    Boolean isRead = msgContent.getBooleanExtra("isRead");
                    Boolean isReadAtAll = msgContent.getBooleanExtra("isReadAtAll");
                    if (lastMsg.isAtMe()) {
                        if (null != isRead && isRead) {
                            mArray.delete(position);
                            mAtConvMap.remove(convItem);
                        } else {
                            mArray.put(position, true);
                        }
                    }
                    if (lastMsg.isAtAll()) {
                        if (null != isReadAtAll && isReadAtAll) {
                            mAtAll.delete(position);
                            mAtAllConv.remove(convItem);
                        } else {
                            mAtAll.put(position, true);
                        }

                    }
                    long gid = 0;
                    if (convItem.getType().equals(ConversationType.group)) {
                        gid = Long.parseLong(convItem.getTargetId());
                    }

                    if (mAtAll.get(position) && BaseApplication.isAtall.get(gid) != null && BaseApplication.isAtall.get(gid)) {
                        contentStr = "[@所有人] " + contentStr;
                        SpannableStringBuilder builder = new SpannableStringBuilder(contentStr);
                        builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        content.setText(builder);
                    } else if (mArray.get(position) && BaseApplication.isAtMe.get(gid) != null && BaseApplication.isAtMe.get(gid)) {
                        //有人@我 文字提示
                        contentStr = mContext.getString(R.string.somebody_at_me) + contentStr;
                        SpannableStringBuilder builder = new SpannableStringBuilder(contentStr);
                        builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        content.setText(builder);
                    } else {
                        if (lastMsg.getTargetType() == ConversationType.group && !contentStr.equals("[群成员变动]")) {
                            UserInfo info = lastMsg.getFromUser();
                            String fromName = info.getDisplayName();
                            if (BaseApplication.isAtall.get(gid) != null && BaseApplication.isAtall.get(gid)) {
                                content.setText("[@所有人] " + fromName + ": " + contentStr);
                            } else if (BaseApplication.isAtMe.get(gid) != null && BaseApplication.isAtMe.get(gid)) {
                                content.setText("[有人@我] " + fromName + ": " + contentStr);
                                //如果content是撤回的那么就不显示最后一条消息的发起人名字了
                            } else if (msgContent.getContentType() == ContentType.prompt) {
                                content.setText(contentStr);
                            } else if (info.getUserName().equals(JMessageClient.getMyInfo().getUserName())) {
                                content.setText(contentStr);
                            } else {
                                content.setText(fromName + ": " + contentStr);
                            }
                        } else {
                            if (BaseApplication.isAtall.get(gid) != null && BaseApplication.isAtall.get(gid)) {
                                content.setText("[@所有人] " + contentStr);
                            } else if (BaseApplication.isAtMe.get(gid) != null && BaseApplication.isAtMe.get(gid)) {
                                content.setText("[有人@我] " + contentStr);
                            } else {
                                if (lastMsg.getUnreceiptCnt() == 0) {
                                    if (lastMsg.getTargetType().equals(ConversationType.single) &&
                                            lastMsg.getDirect().equals(MessageDirect.send) &&
                                            !lastMsg.getContentType().equals(ContentType.prompt) &&
                                            //排除自己给自己发送消息
                                            !((UserInfo) lastMsg.getTargetInfo()).getUserName().equals(JMessageClient.getMyInfo().getUserName())) {
                                        content.setText("[已读]" + contentStr);
                                    } else {
                                        content.setText(contentStr);
                                    }
                                } else {
                                    if (lastMsg.getTargetType().equals(ConversationType.single) &&
                                            lastMsg.getDirect().equals(MessageDirect.send) &&
                                            !lastMsg.getContentType().equals(ContentType.prompt) &&
                                            !((UserInfo) lastMsg.getTargetInfo()).getUserName().equals(JMessageClient.getMyInfo().getUserName())) {
                                        contentStr = "[未读]" + contentStr;
                                        SpannableStringBuilder builder = new SpannableStringBuilder(contentStr);
                                        builder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorGray)),
                                                0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        content.setText(builder);
                                    } else {
                                        content.setText(contentStr);
                                    }

                                }
                            }
                        }
                    }
                } else {
                    if (convItem.getLastMsgDate() == 0) {
                        //会话列表时间展示的是最后一条会话,那么如果最后一条消息是空的话就不显示
                        datetime.setText("");
                        content.setText("");
                    } else {
                        TimeFormat timeFormat = new TimeFormat(mContext, convItem.getLastMsgDate());
                        datetime.setText(timeFormat.getTime());
                        content.setText("");
                    }
                }
            } else {
                draft = mContext.getString(R.string.draft) + draft;
                SpannableStringBuilder builder = new SpannableStringBuilder(draft);
                builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                content.setText(builder);
            }

            if (convItem.getType().equals(ConversationType.single)) {
                groupBlocked.setVisibility(View.GONE);
                mUserInfo = (UserInfo) convItem.getTargetInfo();
                if (mUserInfo != null) {
                    mUserInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                        @Override
                        public void gotResult(int status, String desc, Bitmap bitmap) {
                            if (status == 0) {
                                headIcon.setImageBitmap(bitmap);
                            } else {
                                headIcon.setImageResource(R.mipmap.jmui_head_icon);
                            }
                        }
                    });
                    convName.setText(mUserInfo.getNickname());
                } else {
                    headIcon.setImageResource(R.mipmap.jmui_head_icon);
                }
            }

            if (convItem.getUnReadMsgCnt() > 0) {
                newGroupMsgDisturb.setVisibility(View.GONE);
                newMsgDisturb.setVisibility(View.GONE);
                newGroupMsgNumber.setVisibility(View.GONE);
                newMsgNumber.setVisibility(View.GONE);
                if (convItem.getType().equals(ConversationType.single)) {
                    if (mUserInfo != null && mUserInfo.getNoDisturb() == 1) {
                        newMsgDisturb.setVisibility(View.VISIBLE);
                    } else {
                        newMsgNumber.setVisibility(View.VISIBLE);
                    }
                    if (convItem.getUnReadMsgCnt() < 100) {
                        newMsgNumber.setText(String.valueOf(convItem.getUnReadMsgCnt()));
                    } else {
                        newMsgNumber.setText("99+");
                    }
                }

            } else {
                newGroupMsgDisturb.setVisibility(View.GONE);
                newMsgDisturb.setVisibility(View.GONE);
                newGroupMsgNumber.setVisibility(View.GONE);
                newMsgNumber.setVisibility(View.GONE);
            }

            //禁止使用侧滑功能.如果想要使用就设置为true
            swipeLayout.setSwipeEnabled(true);
            //侧滑删除会话
            swipeLayout.addSwipeListener(new SwipeLayoutConv.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayoutConv layout) {

                }

                @Override
                public void onOpen(SwipeLayoutConv layout) {
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (convItem.getType() == ConversationType.single) {
                                JMessageClient.deleteSingleConversation(((UserInfo) convItem.getTargetInfo()).getUserName());
                            } else {
                                JMessageClient.deleteGroupConversation(((GroupInfo) convItem.getTargetInfo()).getGroupID());
                            }
                            mDatas.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onStartClose(SwipeLayoutConv layout) {

                }

                @Override
                public void onClose(SwipeLayoutConv layout) {

                }

                @Override
                public void onUpdate(SwipeLayoutConv layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onHandRelease(SwipeLayoutConv layout, float xvel, float yvel) {

                }
            });


            return convertView;
        }

        List<Conversation> topConv = new ArrayList<>();
        List<Conversation> forCurrent = new ArrayList<>();

        public void sortConvList() {
            forCurrent.clear();
            topConv.clear();
            int i = 0;
            SortConvList sortConvList = new SortConvList();
            Collections.sort(mDatas, sortConvList);
            for (Conversation con : mDatas) {
                if (!TextUtils.isEmpty(con.getExtra())) {
                    forCurrent.add(con);
                }
            }
            topConv.addAll(forCurrent);
            mDatas.removeAll(forCurrent);
            if (topConv != null && topConv.size() > 0) {
                SortTopConvList top = new SortTopConvList();
                Collections.sort(topConv, top);
                for (Conversation conv : topConv) {
                    mDatas.add(i, conv);
                    i++;
                }
            }
            mHandler.sendEmptyMessage(REFRESH_CONVERSATION_LIST);
        }

        public void addNewConversation(Conversation conv) {
            mDatas.add(0, conv);
            mHandler.sendEmptyMessage(REFRESH_CONVERSATION_LIST);
        }

        public void addAndSort(Conversation conv) {
            mDatas.add(conv);
            SortConvList sortConvList = new SortConvList();
            Collections.sort(mDatas, sortConvList);
            mHandler.sendEmptyMessage(REFRESH_CONVERSATION_LIST);
        }

        public void deleteConversation(Conversation conversation) {
            mDatas.remove(conversation);
            mHandler.sendEmptyMessage(REFRESH_CONVERSATION_LIST);
        }

        public void putDraftToMap(Conversation conv, String draft) {
            mDraftMap.put(conv.getId(), draft);
        }

        public void delDraftFromMap(Conversation conv) {
            mArray.delete(mDatas.indexOf(conv));
            mAtConvMap.remove(conv);
            mAtAllConv.remove(conv);
            mDraftMap.remove(conv.getId());
            mHandler.sendEmptyMessage(REFRESH_CONVERSATION_LIST);
        }

        public String getDraft(String convId) {
            return mDraftMap.get(convId);
        }

        public boolean includeAtMsg(Conversation conv) {
            if (mAtConvMap.size() > 0) {
                Iterator<Map.Entry<Conversation, Integer>> iterator = mAtConvMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Conversation, Integer> entry = iterator.next();
                    if (conv == entry.getKey()) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean includeAtAllMsg(Conversation conv) {
            if (mAtAllConv.size() > 0) {
                Iterator<Map.Entry<Conversation, Integer>> iterator = mAtAllConv.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Conversation, Integer> entry = iterator.next();
                    if (conv == entry.getKey()) {
                        return true;
                    }
                }
            }
            return false;
        }

        public int getAtMsgId(Conversation conv) {
            return mAtConvMap.get(conv);
        }

        public int getatAllMsgId(Conversation conv) {
            return mAtAllConv.get(conv);
        }

        public void putAtConv(Conversation conv, int msgId) {
            mAtConvMap.put(conv, msgId);
        }

        public void putAtAllConv(Conversation conv, int msgId) {
            mAtAllConv.put(conv, msgId);
        }


    }

}

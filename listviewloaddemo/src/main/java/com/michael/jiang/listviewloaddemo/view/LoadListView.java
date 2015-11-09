package com.michael.jiang.listviewloaddemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.michael.jiang.listviewloaddemo.ILoadListener;
import com.michael.jiang.listviewloaddemo.IPullState;
import com.michael.jiang.listviewloaddemo.IRefreshListener;
import com.michael.jiang.listviewloaddemo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义ListView
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener {

    private LinearLayout loadLayout;
    private View footerView;
    private int lastVisibleItem;
    private int totalItemCount;
    private ILoadListener iLoadListener;
    private  boolean isLoading=false;

    private View headView;
    private int firstVisibleItem;
    private int headHeight=0;//下拉刷新这个HeadView的高度
    private int scrollState;//ScrollView当前的滚动状态
    private int pullState;
    private boolean isMark=false;//标记是否是最顶端向下拉的
    private int startY=0;//刚向下拉的时候Y方向的值
    private IRefreshListener iRefreshListener;


    public LoadListView(Context context) {
        super(context);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

//    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        headView = layoutInflater.inflate(R.layout.header_layout, null);
        this.addHeaderView(headView);
        measureView(headView);
        headHeight=headView.getMeasuredHeight();
        setHeaderViewTopPadding(-headHeight);


        footerView = layoutInflater.inflate(R.layout.footer_layout, null);
        loadLayout= (LinearLayout) footerView.findViewById(R.id.load_layout);
        loadLayout.setVisibility(GONE);
        //this.addView(footerView);  //注意此处
        this.addFooterView(footerView);
        this.setOnScrollListener(this);
    }


    /**
     * 通知父布局我占用的宽高。
     * @param view 对象视图
     */
    private  void measureView(View view){
        ViewGroup.LayoutParams params=view.getLayoutParams();
        if(params==null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int height;
        int tmpHeight=params.height;
        if(tmpHeight>0) {
            height = MeasureSpec.makeMeasureSpec(tmpHeight, MeasureSpec.EXACTLY);
        }else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width,height);
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        this.scrollState=scrollState;//滚动状态
           if(totalItemCount==lastVisibleItem && scrollState==SCROLL_STATE_IDLE) //当滚动到最后一个，且当前已经停止滚动了
           {
               if(!isLoading)
               {
                   isLoading=true;
                   loadLayout.setVisibility(VISIBLE);
                   iLoadListener.onLoad();
               }
           }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem=firstVisibleItem;
        this.lastVisibleItem=firstVisibleItem+visibleItemCount;
        this.totalItemCount=totalItemCount;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(firstVisibleItem==0){
                    isMark=true;
                    startY=(int)ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if(pullState== IPullState.PULL)
                {
                    pullState=IPullState.NONE;
                    isMark=false;
                    refreshViewByPullState();
                }else if(pullState==IPullState.RELEASE){ //提示释放刷新
                    pullState=IPullState.REFRESHING;
                    //加载最新数据
                    refreshViewByPullState();
                    iRefreshListener.onRefreshing();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    private void onMove(MotionEvent ev) {
        if(!isMark){ //判断手是否在屏幕上，ACTION_DOWN手开始放到屏幕上，ACTION_UP手从屏幕上抬起来
            return;
        }
        int tempY=(int)ev.getY();
        int distance=tempY-startY;
        int topPadding=distance-headHeight;
        switch (pullState)
        {
            case IPullState.NONE:
                if(distance>0){
                    pullState=IPullState.PULL;
                    refreshViewByPullState();
                }
                break;
            case IPullState.PULL:
                setHeaderViewTopPadding(topPadding);
                if(distance>headHeight+50 && scrollState==SCROLL_STATE_TOUCH_SCROLL){
                    pullState=IPullState.RELEASE;
                    refreshViewByPullState();
                }
                break;
            case IPullState.RELEASE:
                setHeaderViewTopPadding(topPadding);
                if(distance<headHeight+50){
                    pullState=IPullState.PULL;
                    refreshViewByPullState();
                }else if(distance<=0)
                {
                    pullState=IPullState.NONE;
                    isMark=false;
                    refreshViewByPullState();
                }
                break;
        }


    }

    private void refreshViewByPullState()
    {
        ImageView arrow= (ImageView) headView.findViewById(R.id.refresh_arrow);
        TextView tv_tip= (TextView) headView.findViewById(R.id.tip_txtView);
        ProgressBar progressBar= (ProgressBar) headView.findViewById(R.id.progress);
        RotateAnimation rotateAnimation=new RotateAnimation(180,0, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        RotateAnimation rotateAnimation1 = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation1.setDuration(1000);
        rotateAnimation1.setFillAfter(true);

        switch (pullState){
            case IPullState.NONE:
                arrow.clearAnimation();
                 setHeaderViewTopPadding(-headHeight);
                break;
            case IPullState.PULL:
                arrow.setVisibility(VISIBLE);
                arrow.clearAnimation();
                arrow.setAnimation(rotateAnimation);
                tv_tip.setText("下拉可以刷新");
                progressBar.setVisibility(GONE);
                break;
            case IPullState.RELEASE:
                arrow.setVisibility(VISIBLE);
                arrow.clearAnimation();
                arrow.setAnimation(rotateAnimation1);
                progressBar.setVisibility(GONE);
                tv_tip.setText("释放可以刷新");
                break;
            case IPullState.REFRESHING:
                setHeaderViewTopPadding(50);
                arrow.setVisibility(GONE);
                arrow.clearAnimation();
                progressBar.setVisibility(VISIBLE);
                tv_tip.setText("正在刷新");
                break;
        }

    }

    /**
     * 设置HeadView的Padding，只设置top方向，其他方向不变
     * @param topPadding int值
     */
    private void setHeaderViewTopPadding(int topPadding)
    {
        headView.setPadding(headView.getPaddingLeft(),topPadding,headView.getPaddingRight(),headView.getPaddingBottom());
        headView.invalidate();//刷新界面
    }

    /**
     * 通知底部下拉加载完成
     */
    public void loadComplete()
    {
        loadLayout.setVisibility(GONE);
        isLoading=false;
    }
    public  void refreshComplete()
    {
        isMark=false;
        pullState=IPullState.NONE;
        TextView tv_RefreshTime= (TextView) headView.findViewById(R.id.lastRefreshTime_txtView);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = dateFormat.format(date);
        tv_RefreshTime.setText(time);
    }


    public void setILoadListener(ILoadListener iLoadListener)
    {
        this.iLoadListener=iLoadListener;
    }

    public void setIRefreshListener(IRefreshListener iRefreshListener){
        this.iRefreshListener=iRefreshListener;
    }

}

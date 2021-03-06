package com.recyclerview.gridview.test.base.recyclerview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.recyclerview.gridview.test.base.R;
import com.recyclerview.gridview.test.base.recyclerview.adapter.BaseRecyclerAdapter;


/**
 * Created by MaShiZhao on 2016/12/30
 */
public class MyPullSwipeRefresh extends SwipeRefreshLayout
{

    private Context mContext;

    private BaseRecyclerAdapter mBaserAdapter;
    private float startY;
    private float startX;
    // 记录viewPager是否拖拽的标记
    private boolean mIsVpDragger;
    private  int mTouchSlop;
    public MyPullSwipeRefresh(Context context)
    {
        super(context);
        initView(context, null);
    }

    public MyPullSwipeRefresh(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initView(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                mIsVpDragger = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if(mIsVpDragger) {
                    return false;
                }

                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if(distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsVpDragger = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsVpDragger = false;
                break;
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev);
    }

    private void initView(Context context, AttributeSet attrs)
    {
        this.mContext = context;
        setProgressBackgroundColorSchemeColor(ContextCompat.getColor(mContext, R.color.white));
        setColorSchemeResources(R.color.first_theme,
                R.color.first_theme,
                R.color.first_theme,
                R.color.first_theme);
        setSize(SwipeRefreshLayout.DEFAULT);
    }

}

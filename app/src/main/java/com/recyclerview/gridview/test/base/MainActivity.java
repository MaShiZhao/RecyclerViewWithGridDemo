package com.recyclerview.gridview.test.base;

/**
 * Created by MaShiZhao on 2017/3/7
 */

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.http.HttpGet;
import com.app.http.InterfaceHttpResult;
import com.app.utils.LogWriter;
import com.recyclerview.gridview.test.base.recyclerview.MyPullSwipeRefresh;
import com.recyclerview.gridview.test.base.recyclerview.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
{
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyRecyclerView recyclerView;
    private List<InformationResult.DataBean.ListBean> dataList;
    //    private String url = "http://hbsi.gkk.cn/Mobile/Chip/newsListAction?deviceId=861735031544595&mid=0&preNum=40&page=1&oauth_token=&oauth_token_secret=";
    private String url = "http://192.168.3.6:88/Mobile/Chip/newsListAction?deviceId=861735031544595&mid=0&preNum=10&page=1&oauth_token=&oauth_token_secret=";
    private InformationDelegate informationDelegate;
    private MyInformationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_list);
        dataList = new ArrayList<>();
        initViews();
        getInformationData();
    }


    private void initViews()
    {
        recyclerView = (MyRecyclerView) findViewById(R.id.recycler);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        //设置个delegate
        informationDelegate = new InformationDelegate(this, dataList.size());
        adapter = new MyInformationAdapter(this, dataList, informationDelegate);
        recyclerView.setAdapter(adapter);

        //adapter里面设置swiperefresh
        adapter.setSwipeRefresh(myPullSwipeRefresh);

        //刷新事件
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                adapter.resetPageIndex();
                getInformationData();
            }
        });

        //加载事件
//        recyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
//        {
//            @Override
//            public void addMoreListener()
//            {
//                adapter.addPageIndex();
//                getInformationData();
//            }
//        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {

//                LogWriter.d(" child count " + recyclerView.getLayoutManager().getChildCount());
//                //得到当前显示的最后一个item的view
//                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
//                //得到lastChildView的bottom坐标值
//                int lastChildBottom = lastChildView.getBottom();
//                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
//                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
//
//                //通过这个lastChildView得到这个view当前的position值
//                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
//
//                LogWriter.d(" lastChildBottom " + lastChildBottom + " recyclerBottom " + recyclerBottom + " lastPosition " + lastPosition + " itemCount " + recyclerView.getLayoutManager().getItemCount());
//
//                //判断lastChildView的bottom值跟recyclerBottom
//                //判断lastPosition是不是最后一个position
//                //如果两个条件都满足则说明是真正的滑动到了底部
//                if (recyclerBottom - lastChildBottom == 0 && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1)
//                {
//                    adapter.addPageIndex();
//                    getInformationData();
//                }
                GridLayoutManager mGridViewLayoutManger = (GridLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItem = mGridViewLayoutManger.findLastVisibleItemPosition();
                int totalItemCount = mGridViewLayoutManger.getItemCount();
//                lastVisibleItem >= totalItemCount - 4 // 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                LogWriter.d(" lastVisibleItem " + lastVisibleItem + " totalItemCount " + totalItemCount );

                if (lastVisibleItem == totalItemCount - 1 && dy > 0)
                {
//                    if (isLoadingMore) {
//                        Log.d("", "ignore manually update!");
//                    } else {
//                        loadPage();//这里多线程也要手动控制isLoadingMore
//                        Toast.makeText(MainActivity.this, "MainActivity>>loadPage", Toast.LENGTH_SHORT).show();
//                        isLoadingMore = false;

                    adapter.addPageIndex();
                    getInformationData();

//                    }
                }

            }
        });


    }

    private void getInformationData()
    {
        HttpGet httpget = new HttpGet(this, this, new InterfaceHttpResult()
        {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean)
            {
                if (code == 1000)
                {
                    InformationResult resultBean = (InformationResult) baseBean;
                    adapter.setTotalPage(14);

                    List<InformationResult.DataBean.ListBean> dataList = resultBean.getData().getList();
//                    dataList.addAll(resultBean.getData().getList());
                    //刷新后的数据设置
                    adapter.setPullData(dataList);
                } else
                {
                    adapter.setFail();
                }

            }
        }, InformationResult.class, url);
        httpget.excute();
    }

}


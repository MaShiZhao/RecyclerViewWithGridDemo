package com.recyclerview.gridview.test.base;

/**
 * Created by MaShiZhao on 2017/3/7
 */

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.app.http.HttpGet;
import com.app.http.InterfaceHttpResult;
import com.recyclerview.gridview.test.base.recyclerview.MyPullRecyclerView;
import com.recyclerview.gridview.test.base.recyclerview.MyPullSwipeRefresh;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
{
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView recyclerView;
    private List<InformationResult.DataBean.ListBean> dataList;
    private String url = "http://hbsi.gkk.cn/Mobile/Chip/newsListAction?deviceId=861735031544595&mid=0&preNum=40&page=1&oauth_token=&oauth_token_secret=";
//    private String url = "http://192.168.3.6:88/Mobile/Chip/newsListAction?deviceId=861735031544595&mid=0&preNum=40&page=1&oauth_token=&oauth_token_secret=";
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
        recyclerView = (MyPullRecyclerView) findViewById(R.id.recycler);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);

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
        recyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                adapter.addPageIndex();
                getInformationData();
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
                    dataList.addAll(resultBean.getData().getList());
                    //刷新后的数据设置
                    adapter.setPullData(dataList);
                }else{
                    adapter.setFail();
                }

            }
        }, InformationResult.class, url);
        httpget.excute();
    }

}


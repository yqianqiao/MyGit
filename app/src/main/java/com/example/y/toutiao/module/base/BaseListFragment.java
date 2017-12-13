package com.example.y.toutiao.module.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.y.toutiao.R;
import com.example.y.toutiao.util.SettingUtil;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * Created by y on 2017/12/13.
 */

public abstract class BaseListFragment<T extends IBasePresenter> extends LazyLoadFragment<T> implements IBaseListView<T>, SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MultiTypeAdapter adapter;
    //加载更多
    protected boolean canLoadMore = false;

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        //设置转圈颜色为主题颜色
        swipeRefreshLayout.setColorSchemeColors(SettingUtil.getInstance().getColor());
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() throws NullPointerException {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onSetAdapter(List<?> list) {

    }

    @Override
    public void onShowNoMore() {

    }

    @Override
    public void onShowLoading() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onHideLoading() {

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onShowNetError() {
        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setItems(new Items());
                adapter.notifyDataSetChanged();
                canLoadMore = false;
            }
        });
    }

    @Override
    public void setPresenter(T presenter) {

    }

    @Override
    public void fetchData() {

    }
}

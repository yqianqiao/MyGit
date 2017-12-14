package com.example.y.toutiao.module.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.y.toutiao.R;
import com.example.y.toutiao.RxBus;
import com.example.y.toutiao.bean.LoadingEndBean;
import com.example.y.toutiao.util.SettingUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * Created by y on 2017/12/13.
 */

public abstract class BaseListFragment<T extends IBasePresenter> extends LazyLoadFragment<T> implements IBaseListView<T>, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "BaseListFragment";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MultiTypeAdapter adapter;
    //加载更多
    protected boolean canLoadMore = false;
    protected Observable<Integer> observable;

    protected Items oldItems = new Items();

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

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisibleItemPosition == 0) {
            presenter.doRefresh();
            return;
        }
        recyclerView.scrollToPosition(5);
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onSetAdapter(List<?> list) {

    }

    @Override
    public void onShowNoMore() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (oldItems.size() > 0) {
                    Items newItems = new Items(oldItems);
                    newItems.remove(newItems.size() - 1);
                    newItems.add(new LoadingEndBean());
                    adapter.setItems(newItems);
                    adapter.notifyDataSetChanged();
                } else if (oldItems.size() == 0) {
                    oldItems.add(new LoadingEndBean());
                    adapter.setItems(oldItems);
                    adapter.notifyDataSetChanged();
                }
                canLoadMore = false;
            }
        });
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
    public void fetchData() {
        observable = RxBus.getInstance().register(BaseListFragment.TAG);
        observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setColorSchemeColors(SettingUtil.getInstance().getColor());
    }

    @Override
    public void onDestroy() {
        //取消注册
        RxBus.getInstance().unregister(BaseListFragment.TAG, observable);
        super.onDestroy();

    }
}

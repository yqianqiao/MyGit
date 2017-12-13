package com.example.y.toutiao.module.base;

import android.support.v4.widget.SwipeRefreshLayout;

import com.example.y.toutiao.R;


/**
 * Created by y on 2017/12/13.
 */

public abstract class BaseListFragment<T extends IBasePresenter> extends LazyLoadFragment<T> implements IBaseListView<T>, SwipeRefreshLayout.OnRefreshListener {


    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_base_list;
    }
}

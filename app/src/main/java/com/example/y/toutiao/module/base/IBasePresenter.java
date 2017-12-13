package com.example.y.toutiao.module.base;

/**
 * Created by y on 2017/12/11.
 */

public interface IBasePresenter {
    /**
     * 刷新数据
     */
    void doRefresh();

    /**
     * 显示网络错误
     */
    void doShowNetError();
}

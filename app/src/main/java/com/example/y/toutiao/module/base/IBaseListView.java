package com.example.y.toutiao.module.base;

import java.util.List;

/**
 * Created by y on 2017/12/13.
 */

public interface IBaseListView<T> extends IBaseView<T> {
    /**
     * 绑定适配器
     */
    void onSetAdapter(List<?> list);

    /**
     * 加载完成
     */
    void onShowNoMore();
}

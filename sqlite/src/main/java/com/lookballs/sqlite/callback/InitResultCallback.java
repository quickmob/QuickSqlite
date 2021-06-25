package com.lookballs.sqlite.callback;

import com.lookballs.sqlite.core.result.ResultBean;

public interface InitResultCallback<D> {
    void callback(ResultBean<D> resultBean);
}

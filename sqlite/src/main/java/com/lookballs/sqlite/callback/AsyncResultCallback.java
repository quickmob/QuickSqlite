package com.lookballs.sqlite.callback;

import com.lookballs.sqlite.core.result.ResultBean;

public interface AsyncResultCallback<D> {
    void callback(ResultBean<D> resultBean);
}
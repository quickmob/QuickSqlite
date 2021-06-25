package com.lookballs.sqlite.core.async;

import com.lookballs.sqlite.callback.AsyncResultCallback;
import com.lookballs.sqlite.core.async.AsyncExecutor;

public class ResultExecutor extends AsyncExecutor {

    private AsyncResultCallback callback;

    public void listener(AsyncResultCallback asyncResultCallback) {
        callback = asyncResultCallback;
        execute();
    }

    public AsyncResultCallback getListener() {
        return callback;
    }
}
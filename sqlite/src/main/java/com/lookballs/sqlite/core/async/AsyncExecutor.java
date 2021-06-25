package com.lookballs.sqlite.core.async;

public abstract class AsyncExecutor {

    private Runnable pendingTask;

    public void submit(Runnable task) {
        pendingTask = task;
    }

    public void execute() {
        if (pendingTask != null) {
            new Thread(pendingTask).start();
        }
    }
}
package com.lookballs.sqlite.core.result;

public class ResultBean<D> {
    private boolean isSuccess = false;
    private D result;
    private Exception exception;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public D getResult() {
        return result;
    }

    public void setResult(D result) {
        this.result = result;
    }

    public Exception getException() {
        if (exception == null) {
            exception = new Exception();
        }
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}

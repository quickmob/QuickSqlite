package com.lookballs.app.sqlite.table;

import com.lookballs.sqlite.annotation.Column;

public class UserInfoBean extends BaseBean {
    @Column(ignore = true, desc = "测试字段")
    private String test;

    private String username;
    private String password;
    private short shortField;

    private long createTime;
    private int level;
    private boolean isBindMobile;

    private byte[] icon;

    private float money;
    private double payMoney;

    private FollowBean followBean;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public short getShortField() {
        return shortField;
    }

    public void setShortField(short shortField) {
        this.shortField = shortField;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isBindMobile() {
        return isBindMobile;
    }

    public void setBindMobile(boolean bindMobile) {
        isBindMobile = bindMobile;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    public FollowBean getFollowBean() {
        return followBean;
    }

    public void setFollowBean(FollowBean followBean) {
        this.followBean = followBean;
    }
}

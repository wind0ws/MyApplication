package com.michael.jiang.listviewloaddemo;

/**
 * 下拉刷新时的四种状态
 */
public interface IPullState {
    /**
     * 不显示下拉刷新
     */
    int NONE=0;
    /**
     * 提示下拉刷新
     */
    int PULL=1;
    /**
     * 提示释放开始刷新
     */
    int RELEASE=2;
    /**
     * 提示正在刷新中
     */
    int REFRESHING=3;
}

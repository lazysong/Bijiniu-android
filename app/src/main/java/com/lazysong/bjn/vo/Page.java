package com.lazysong.bjn.vo;

import java.util.Collections;
import java.util.List;

/**
 * 分页vo.
 * Created by 丞 on 2016/10/23.
 */
public class Page<E> {
    private int pageShow;
    private int totalCount;
    private int nowPage;
    private List<E> result = Collections.emptyList();

    public int getPageShow() {
        return pageShow;
    }

    public void setPageShow(int pageShow) {
        this.pageShow = pageShow;
    }

    public int getTotalPage() {
        return (int) Math.ceil(totalCount * 1.0 / pageShow);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 获取当前列表起始序号.
     * @return 序号
     */
    public int getStart() {
        int start = (getNowPage() - 1) * getPageShow();
        if (start < 0) {
            start = 0;
        }
        return start;
    }

    /**
     * 获取当前页码.
     * @return 页码从1开始
     */
    public int getNowPage() {
        if (nowPage <= 0) {
            nowPage = 1;
        }
        if (nowPage > getTotalPage()) {
            nowPage = getTotalPage();
        }
        return nowPage;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    public List<E> getResult() {
        return result;
    }

    public void setResult(List<E> result) {
        this.result = result;
    }
}

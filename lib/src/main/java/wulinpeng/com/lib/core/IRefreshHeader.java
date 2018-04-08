package wulinpeng.com.lib.core;

/**
 * @author wulinpeng
 * @datetime: 18/4/3 下午11:18
 * @description:
 */
public interface IRefreshHeader {

    void onStartRefresh();

    void onCancelRefresh();

    void onRefreshing();

    void onRefreshProgress(float progress);

    // 改变下拉状态的阈值
    float getChangeStateRatio();
}

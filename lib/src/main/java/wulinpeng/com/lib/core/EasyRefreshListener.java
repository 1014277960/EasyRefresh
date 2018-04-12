package wulinpeng.com.lib.core;

/**
 * @author wulinpeng
 * @datetime: 18/4/3 下午11:18
 * @description:
 */
public interface EasyRefreshListener {

    void onRefreshing();

    // 根据progress改变内容
    void onRefreshProgress(float progress);

    // 改变下拉状态的阈值
    float getChangeStateRatio();
}

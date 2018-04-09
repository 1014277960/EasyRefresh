package wulinpeng.com.easyrefresh;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import wulinpeng.com.lib.core.IRefreshHeader;

/**
 * @author wulinpeng
 * @datetime: 18/4/4 上午11:33
 * @description:
 */
public class TestHeader extends TextView implements IRefreshHeader {
    public TestHeader(Context context) {
        super(context);
        setGravity(Gravity.CENTER);
        setText("This is a header!");
        setTextSize(30);
    }

    @Override
    public void onRefreshing() {
        setText("refreshing now!");
    }

    @Override
    public void onRefreshProgress(float progress) {
        if (progress > getChangeStateRatio()) {
            setText("release to refresh!");
        } else {
            setText("This is a header!");
        }
    }

    @Override
    public float getChangeStateRatio() {
        return 1.5f;
    }
}

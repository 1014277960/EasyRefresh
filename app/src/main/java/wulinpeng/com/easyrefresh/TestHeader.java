package wulinpeng.com.easyrefresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import wulinpeng.com.lib.core.EasyRefreshListener;

/**
 * @author wulinpeng
 * @datetime: 18/4/4 上午11:33
 * @description:
 */
public class TestHeader extends TextView {

    public TestHeader(Context context) {
        this(context, null);
    }

    public TestHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setGravity(Gravity.CENTER);
        setText("This is a header!");
        setTextSize(30);
    }
}

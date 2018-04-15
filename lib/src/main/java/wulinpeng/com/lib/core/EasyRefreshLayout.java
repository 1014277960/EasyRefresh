package wulinpeng.com.lib.core;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * @author wulinpeng
 * @datetime: 18/4/3 下午11:25
 * @description: ViewParent内部有NestedScrollingParent的实现
 */
public class EasyRefreshLayout extends LinearLayout implements NestedScrollingParent {

    // 初始状态
    public static int STATE_NORMAL = 0;
    // 刷新状态
    public static int STATE_REFRESHING = 1;

    private static String LOG_TAG = "Debug";

    /**
     * 通过持续更新mUnConsumedY来判断子view是否滑动到最顶端
     */
    private int mUnConsumedY = 0;
    private int mHeaderShowHeight = 0;

    // 状态
    private int mCurrentState = STATE_NORMAL;

    private View mContentView;
    private View mHeaderView;
    private View mFooterView;
    private int mHeaderHeight;

    private EasyRefreshListener mListener;

    private boolean mIsLoadOnce = false;

    private Scroller mScroller;
    private boolean isScrolling = false;

    public EasyRefreshLayout(Context context) {
        this(context, null);
    }

    public EasyRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttrs(attrs, defStyleAttr);
        initView();
    }

    private void init() {
        mScroller = new Scroller(getContext());
    }

    private void initAttrs(@Nullable AttributeSet attrs, int defStyleAttr) {
    }

    private void initView() {
        setOrientation(VERTICAL);
    }

    public void addRefreshListener(EasyRefreshListener listener) {
        this.mListener = listener;
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing && mCurrentState == STATE_NORMAL) {
            // 开始刷新
            startRefreshing();
        } else if (!refreshing && mCurrentState == STATE_REFRESHING) {
            // 停止刷新
            stopRefreshing();
        }
    }

    // 滚动到初始位置
    private void stopRefreshing() {
        mCurrentState = STATE_NORMAL;
        startAnimation(mHeaderShowHeight, 0);
    }

    // 滚动到刷新位置，通知刷新
    private void startRefreshing() {

        // 必须在onMeasure之后才有用
        if (mHeaderView == null || mContentView == null) {
            Log.d(LOG_TAG, "must call startRefreshing after onMeasure being called!");
            return;
        }

        mCurrentState = STATE_REFRESHING;
        if (mListener != null) {
            mListener.onRefreshing();
        }
        startAnimation(mHeaderShowHeight, mHeaderHeight);
    }

    private void startAnimation(int startShowHeight, int endShowHeight) {
        if (isScrolling) {
            return;
        }
        isScrolling = true;
        mScroller.startScroll(0, startShowHeight, 0, endShowHeight - startShowHeight);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mHeaderShowHeight = mScroller.getCurrY();
            setHeaderTopMarginWithShowHeight();
            invalidate();
        } else {
            isScrolling = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeaderView = getChildAt(0);
        mContentView = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !mIsLoadOnce) {
            mHeaderHeight = mHeaderView.getHeight();
            setMargin(mHeaderView, 0, -mHeaderHeight, 0, 0);
            mIsLoadOnce = true;
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    /**
     * 在子view滑动前判断自己需要的滑动，然后再给子view
     * todo 当前状态若是refreshing特殊操作
     * @param target
     * @param dx
     * @param dy 向上滑动为正
     * @param consumed
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (isScrolling) {
            return;
        }
        if (dy > 0 && mHeaderShowHeight > 0) {
            // header显示的时候上滑
            if (dy <= mHeaderShowHeight) {
                mHeaderShowHeight -= dy;
                consumed[1] = dy;
            } else {
                consumed[1] = mHeaderShowHeight;
                mHeaderShowHeight = 0;
            }
        }
        if (dy < 0 && mUnConsumedY == 0) {
            consumed[1] = dy;
            mHeaderShowHeight -= dy;
        }
        // 其他情况皆为更新mUnConsumedY，在onNestedScroll方法中更新
        processHeaderShowHeight();
        updateProgress();
    }

    /**
     * 处理mHeaderShowHeight，如果高于mHeaderHeight，加一个滑动的阻力
     */
    private void processHeaderShowHeight() {
        // 计算阻力作用后的mHeaderShowHeight
        if (mHeaderShowHeight > mHeaderHeight) {
            int extra = mHeaderShowHeight - mHeaderHeight;
            // 阻力系数
            float dragRatio = mHeaderHeight * 1.0f / mHeaderShowHeight;
            mHeaderShowHeight = (int) (mHeaderHeight + extra * dragRatio);
        }
        // 通过mHeaderShowHeight来设置topMargin
        setHeaderTopMarginWithShowHeight();
    }

    /**
     * 根据mHeaderShowHeight更新当前进度
     */
    private void updateProgress() {
        if (mListener != null) {
            mListener.onRefreshProgress(mHeaderShowHeight * 1.0f / mHeaderHeight);
        }
    }

    /**
     * 在子view滑动后通过消耗和未消耗的，来计算子view是否滑动到最顶端
     * @param target
     * @param dxConsumed
     * @param dyConsumed
     * @param dxUnconsumed
     * @param dyUnconsumed
     */
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        mUnConsumedY += dyConsumed;
        mUnConsumedY = Math.max(mUnConsumedY, 0);
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        if (mHeaderShowHeight > 0) {
            float changeStateRatio = mListener == null? 1.0f : mListener.getChangeStateRatio();
            if (mHeaderShowHeight * 1.0f / mHeaderHeight > changeStateRatio) {
                // 开始刷新
                startRefreshing();
            } else {
                stopRefreshing();
            }
        }
    }

    /**
     * 禁止子view fling，避免对mUnConsumedY的更新造成影响
     * todo 支持fling
     * @param target
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return true;
    }

    private void setHeaderTopMarginWithShowHeight() {
        setMargin(mHeaderView, 0, mHeaderShowHeight - mHeaderHeight, 0, 0);
    }

    private void setMargin(View target, int left, int top, int right, int bottom) {
        LayoutParams params = (LayoutParams) target.getLayoutParams();
        params.leftMargin = left;
        params.topMargin = top;
        params.rightMargin = right;
        params.bottomMargin = bottom;
        target.setLayoutParams(params);
    }
}

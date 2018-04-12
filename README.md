# EasyRefresh
简易的Android下拉刷新，目前仅支持RecyclerView(实现了NestedScrollingChild的控件)，功能尚不完善，仅用作学习用途.
# 使用方法
Layout如下:
```
<wulinpeng.com.lib.core.EasyRefreshLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/refresh_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="wulinpeng.com.easyrefresh.MainActivity">

    <wulinpeng.com.easyrefresh.TestHeader
        android:id="@+id/header"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</wulinpeng.com.lib.core.EasyRefreshLayout>
```
添加RefreshListener
```
        refreshLayout.addRefreshListener(new EasyRefreshListener() {
            @Override
            public void onRefreshing() {
                header.setText("refreshing now!");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);

            }

            @Override
            public void onRefreshProgress(float progress) {
                if (progress > getChangeStateRatio()) {
                    header.setText("release to refresh!");
                } else {
                    header.setText("pull down to refresh!");
                }
            }

            @Override
            public float getChangeStateRatio() {
                return 1.5f;
            }
        });
```

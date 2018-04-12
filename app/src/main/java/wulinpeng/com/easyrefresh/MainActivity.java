package wulinpeng.com.easyrefresh;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wulinpeng.com.lib.core.EasyRefreshLayout;
import wulinpeng.com.lib.core.EasyRefreshListener;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TestHeader header;
    private EasyRefreshLayout refreshLayout;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TestAdapter(this));

        header = (TestHeader) findViewById(R.id.header);
        refreshLayout = (EasyRefreshLayout) findViewById(R.id.refresh_layout);

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

        // 测试setRefreshing
        header.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }, 500);
    }

    class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;

        public TestAdapter(Context context) {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(context);
            return new TestHolder(textView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((TestHolder) holder).textView.setText("item"+ position);
        }

        @Override
        public int getItemCount() {
            return 40;
        }
    }

    class TestHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public TestHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}

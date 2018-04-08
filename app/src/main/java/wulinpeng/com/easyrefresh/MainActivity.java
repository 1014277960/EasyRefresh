package wulinpeng.com.easyrefresh;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wulinpeng.com.lib.core.EasyRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EasyRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TestAdapter(this));

        refreshLayout = (EasyRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setHeaderView(new TestHeader(this));

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

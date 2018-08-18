package goodchan.fam_map_client.FilterActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import goodchan.fam_map_client.R;


public class FilterActivity extends AppCompatActivity {
    private RecyclerView filterRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filterRecyclerView = (RecyclerView) findViewById(R.id.filter_activity_recycler_view);
        filterRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        filterRecyclerView.setLayoutManager(layoutManager);
        adapter = new FilterAdapter();
        filterRecyclerView.setAdapter(adapter);
    }
}

package goodchan.fam_map_client.SearchActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import goodchan.fam_map_client.R;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView searchResults;
    private EditText search;
    private String currSearch = "";
    private RecyclerView.LayoutManager layoutManager;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchResults = findViewById(R.id.search_recycler_view);
        search = findViewById(R.id.search_edit_text);
        searchResults.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        searchResults.setLayoutManager(layoutManager);
        searchAdapter = new SearchAdapter();
        searchResults.setAdapter(searchAdapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currSearch = search.getText().toString();
                searchAdapter.updateSearch(currSearch);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}

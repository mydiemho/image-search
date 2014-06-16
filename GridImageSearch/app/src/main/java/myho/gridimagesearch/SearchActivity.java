package myho.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {

    private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8";
    private static final String OFFSET_FIELD = "&start=";
    private static final String QUERY_FIELD = "&q=";

    private static AsyncHttpClient client = new AsyncHttpClient();

    //default
    private int resultOffset = 0;

    private EditText etQuery;
    private StaggeredGridView gvResults;
    private Button btnSearch;
    private List<ImageInfo> imageResults = new ArrayList<ImageInfo>();
    private ImageInfoArrayAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setUpViews();

        imageAdapter = new ImageInfoArrayAdapter(this, imageResults);
        gvResults.setAdapter(imageAdapter);

        // spin up intent to display full size image
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DisplayFullImageActivity.class);
                ImageInfo imageInfo = imageResults.get(position);
                intent.putExtra("imageInfo", imageInfo);

                // fire intent
                startActivity(intent);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                customLoadMoreDataFromApi(page);

            }
        });

    }

    private void setUpViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnImageSearch(View view) {
        getNewQueryData();
    }

    private void getData() {
        String query = etQuery.getText().toString();

        String absolute_url = BASE_URL + OFFSET_FIELD + resultOffset + QUERY_FIELD + Uri.encode(query);
        client.get(
                absolute_url,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        JSONArray imageJsonResults = null;

                        try {
                            imageJsonResults = response
                                    .getJSONObject("responseData")
                                    .getJSONArray("results");

                            imageAdapter.addAll(
                                    ImageInfo.fromJSONArray(imageJsonResults));

                            Log.d("DEBUG", imageResults.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void getNewQueryData() {
        String query = etQuery.getText().toString();

        String absolute_url = BASE_URL + OFFSET_FIELD + resultOffset + QUERY_FIELD + Uri.encode(query);
        client.get(
                absolute_url,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        JSONArray imageJsonResults = null;

                        try {
                            imageJsonResults = response
                                    .getJSONObject("responseData")
                                    .getJSONArray("results");

                            imageResults.clear();
                            imageAdapter.addAll(
                                    ImageInfo.fromJSONArray(imageJsonResults));

                            Log.d("DEBUG", imageResults.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    // Append more data into the adapter
    private void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        resultOffset = resultOffset + 8;
        getData();
    }
}

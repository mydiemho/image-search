package myho.gridimagesearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import org.apache.commons.io.FileUtils;

public class SearchActivity extends SherlockFragmentActivity implements SearchFiltersDialog.SearchFiltersDialogListener {

    private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8";
    private static final String OFFSET_FIELD = "&start=";
    private static final String QUERY_FIELD = "&q=";
    private static final String IMAGE_SIZE_FIELD = "&imgsz=";
    private static final String IMAGE_COLOR_FIELD = "&imgcolor=";
    private static final String IMAGE_TYPE_FIELD = "&imgtype=";
    private static final String SITE_FIELD = "&as_sitesearch=";

    private static AsyncHttpClient client = new AsyncHttpClient();

    //default
    private int resultOffset = 0;

    private StaggeredGridView gvResults;
    private List<ImageInfo> imageResults = new ArrayList<ImageInfo>();
    private ImageInfoArrayAdapter imageAdapter;

    private SearchView searchView;

    private String queryString = "";
    private FilterInfo filterInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setUpViews();

        imageAdapter = new ImageInfoArrayAdapter(this, imageResults);
        gvResults.setAdapter(imageAdapter);

        setUpFullSizeImageDisplay();
        setUpInfiniteScrolling();
    }

    private void setUpFullSizeImageDisplay() {
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
    }

    private void setUpInfiniteScrolling() {
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                customLoadMoreDataFromApi(page);

            }
        });
    }

    private void setUpViews() {
        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML; this adds items to the action bar if it is present.
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.search, (com.actionbarsherlock.view.Menu) menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    Crouton.showText(getParent(), "Please enter a query", Style.INFO);
                    return true;
                }

                // do nothing if query didn't change
                if (queryString.equals(query)) {
                    return true;
                }

                queryString = query;
                resultOffset = 0;
                getData();

                // hide keyboard and search box
                searchMenuItem.collapseActionView();

                // hide only keyboard
                searchView.onActionViewCollapsed();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void displaySearchFilter(MenuItem item) {
        SearchFiltersDialog searchFiltersDialog = SearchFiltersDialog.newInstance("Advanced Search Options");
        searchFiltersDialog.show(getFragmentManager(), "fragment_search_filters");
    }

    @Override
    public void onFinishSelectFilters(FilterInfo filterInfo) {
        // if no new filters are added, do nothing
        if (this.filterInfo != null && this.filterInfo.equals(filterInfo)) {
            return;
        }

        // restart search whenever user apply new filter and query string is not null
        this.filterInfo = filterInfo;

        if (queryString != null && !queryString.isEmpty()) {
            this.resultOffset = 0;
            getData();
        }
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File filtersFile = new File(filesDir, "todo.txt");
        try {
            filterInfo = new FilterInfo(FileUtils.readLines(filtersFile));
        } catch (IOException e) {
            filterInfo = new FilterInfo();
            e.printStackTrace();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File filtersFile = new File(filesDir, "filters.txt");
        try {
            FileUtils.write(filtersFile, filterInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Append more data into the adapter
    private void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        resultOffset = resultOffset + 8;
        getData();
    }

    private void getData() {
        client.get(
                getAbsoluteUrl(),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        JSONArray imageJsonResults = null;

                        try {
                            imageJsonResults = response
                                    .getJSONObject("responseData")
                                    .getJSONArray("results");

                            if (resultOffset == 0) {
                                imageAdapter.clear();
                            }

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

    private String getAbsoluteUrl() {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(BASE_URL);
        urlBuilder.append(OFFSET_FIELD);
        urlBuilder.append(resultOffset);
        urlBuilder.append(QUERY_FIELD);
        urlBuilder.append(Uri.encode(queryString));

        if (filterInfo != null) {
            String imageSize = filterInfo.getImageSize();
            if (!imageSize.equals(getString(R.string.image_size_default))) {
                urlBuilder.append(IMAGE_SIZE_FIELD);
                urlBuilder.append(imageSize);
            }

            String imageColor = filterInfo.getImageColor();
            if (!imageColor.equals(getString(R.string.image_color_default))) {
                urlBuilder.append(IMAGE_COLOR_FIELD);
                urlBuilder.append(imageColor);
            }

            String imageType = filterInfo.getImageType();
            if (!imageType.equals(getString(R.string.image_type_default))) {
                urlBuilder.append(IMAGE_TYPE_FIELD);
                urlBuilder.append(imageType);
            }

            String site = filterInfo.getSite();
            if (site != null && !site.isEmpty()) {
                urlBuilder.append(SITE_FIELD);
                urlBuilder.append(site);
            }
        }

        Log.i("SEARCH", "url: " + urlBuilder.toString());
        return urlBuilder.toString();
    }
}



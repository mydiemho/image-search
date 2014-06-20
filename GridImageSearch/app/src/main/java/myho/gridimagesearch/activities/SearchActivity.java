package myho.gridimagesearch.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import myho.gridimagesearch.R;
import myho.gridimagesearch.adapters.ImageInfoArrayAdapter;
import myho.gridimagesearch.fragments.SearchFiltersDialog;
import myho.gridimagesearch.helpers.EndlessScrollListener;
import myho.gridimagesearch.models.FilterInfo;
import myho.gridimagesearch.models.ImageInfo;

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

    private GridView gvResults;
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

                Log.i("FULL_SIZE", imageInfo.getThumbUrl());
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
        gvResults = (GridView) findViewById(R.id.gvResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML; this adds items to the action bar if it is present.
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.search, (com.actionbarsherlock.view.Menu) menu);

        final MenuItem menuItemSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItemSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if( queryString.equals(query)) {
                    return true;
                }

                queryString = query;
                resultOffset = 0;
                getData();

                // hide soft keyboard when user is done with input
                searchView.clearFocus();

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
        Bundle bundle = new Bundle();
        bundle.putParcelable("filterInfo", filterInfo);
        SearchFiltersDialog searchFiltersDialog = SearchFiltersDialog.newInstance("Advanced Search Options");
        // pass filterInfo to setting dialog
        searchFiltersDialog.setArguments(bundle);
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

        if (!queryString.isEmpty()) {
            this.resultOffset = 0;
            getData();
        }
    }

    // Append more data into the adapter
    private void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        resultOffset = resultOffset + 8;

        if (resultOffset == 64) {
            return;
        }
        // only load more if not at the end
        getData();
    }


    private void getData() {
        showProgressBar();
        client.get(
                getAbsoluteUrl(),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        JSONArray imageJsonResults = null;

                        try {
//                            hideProgressBar();

                            imageJsonResults = response
                                    .getJSONObject("responseData")
                                    .getJSONArray("results");

                            if (resultOffset == 0) {
                                imageAdapter.clear();
                            }

                            if(imageJsonResults.length() == 0){
                                Log.i("SEARCH_ACTIVITY", "Call Warn user");
                                warnUser();
                            }

                            imageAdapter.addAll(
                                    ImageInfo.fromJSONArray(imageJsonResults));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void warnUser() {
        Log.i("SEARCH_ACTIVITY", "Warn user");
        String msg = "No result found for query=" + queryString + "filters: " + filterInfo.toString();

        Configuration croutonConfiguration = new Configuration.Builder().setDuration(4000).build();
        Style style = new Style.Builder()
                .setBackgroundColorValue(Color.parseColor("#daffc0"))
                .setGravity(Gravity.CENTER_HORIZONTAL)
                .setConfiguration(croutonConfiguration)
                .setHeight(150)
                .setTextColorValue(Color.parseColor("#323a2c")).build();
        Crouton.makeText(this, msg, style).show();
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

    // Should be called manually when an async task has started
    public void showProgressBar() {
        setProgressBarIndeterminateVisibility(true);
    }

    // Should be called when an async task has finished
    public void hideProgressBar() {
        setProgressBarIndeterminateVisibility(false);
    }
}



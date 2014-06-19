package myho.gridimagesearch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import myho.gridimagesearch.R;
import myho.gridimagesearch.models.ImageInfo;

public class DisplayFullImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_full_image);

        ImageInfo imageInfo = getIntent().getParcelableExtra("imageInfo");
        ImageView ivImage = (ImageView) findViewById(R.id.ivResult);

        Log.i("FULL_IMAGE", "thumbUrl: " + imageInfo.getThumbUrl() + "; fullUrl: " + imageInfo.getFullUrl());

        Picasso
                .with(this)
                .load(imageInfo.getFullUrl())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_not_found)
//                // wait until the ImageView has been measured and resize the image to exactly match its size.
                .fit()
                // scale the image honoring the aspect ratio until it fills the size. Crop either
                // the top and bottom or left and right so it matches the size exactly.
                .centerCrop()
                .into(ivImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_full_image, menu);
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
}

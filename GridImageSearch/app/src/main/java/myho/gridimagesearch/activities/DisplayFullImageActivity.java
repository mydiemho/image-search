package myho.gridimagesearch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.image.SmartImageView;

import myho.gridimagesearch.R;
import myho.gridimagesearch.models.ImageInfo;

public class DisplayFullImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_full_image);

        ImageInfo imageInfo = (ImageInfo) getIntent().getParcelableExtra("imageInfo");
        SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
        ivImage.setImageUrl(imageInfo.getFullUrl());
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

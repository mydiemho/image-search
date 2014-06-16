package myho.gridimagesearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

import java.util.List;

public class ImageInfoArrayAdapter extends ArrayAdapter<ImageInfo> {
    public ImageInfoArrayAdapter(Context context, List<ImageInfo> images) {
        super(context, R.layout.item_image_result, images);
    }

    // default version return object's toString()
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageInfo imageInfo = this.getItem(position);
        SmartImageView ivImage;
        if(convertView == null) {
            // instantiate the file into an in-memory java object
            LayoutInflater inflator = LayoutInflater.from(getContext());
            ivImage = (SmartImageView) inflator.inflate(R.layout.item_image_result, parent, false);
        }else {
            ivImage = (SmartImageView) convertView;
            // clear out any existing data by setting image to blank background
            //noinspection ResourceType
            ivImage.setImageResource(android.R.color.transparent);
        }

        ivImage.setImageUrl(imageInfo.getThumbUrl());
        return ivImage;
    }
}

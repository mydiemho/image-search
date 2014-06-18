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
        // get the data item for this position
        ImageInfo imageInfo = this.getItem(position);
        SmartImageView ivImage;

        ViewHolder viewHolder; // view lookup cache stored in tag

        // check if an existing view is being reused, otherwise inflate the view
        if(convertView == null) {
            viewHolder = new ViewHolder();

            // instantiate the file into an in-memory java object
            LayoutInflater inflator = LayoutInflater.from(getContext());
            ivImage = (SmartImageView) inflator.inflate(R.layout.item_image_result, parent, false);

            viewHolder.thumbUrl = imageInfo.getThumbUrl();
            convertView.setTag(viewHolder);
        }else {
            ivImage = (SmartImageView) convertView;
            // clear out any existing data by setting image to blank background
            //noinspection ResourceType
            ivImage.setImageResource(android.R.color.transparent);
        }

        ivImage.setImageUrl(imageInfo.getThumbUrl());
        return ivImage;
    }

    // View lookup cache
    private static class ViewHolder {
        String thumbUrl;
    }
}

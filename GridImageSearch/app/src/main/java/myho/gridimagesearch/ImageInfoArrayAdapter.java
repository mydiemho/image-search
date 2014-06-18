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

        ViewHolder viewHolder;

        // check if an existing view is being reused, otherwise inflate the view
        if(convertView == null) {
            viewHolder = new ViewHolder();

            // instantiate the file into an in-memory java object
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = inflator.inflate(R.layout.item_image_result, parent, false);

            viewHolder.ivImage = (SmartImageView) convertView;
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            // clear out any existing data by setting image to blank background
            //noinspection ResourceType
            viewHolder.ivImage.setImageResource(android.R.color.transparent);
        }

        viewHolder.ivImage.setImageUrl(imageInfo.getThumbUrl());
        return convertView;
    }

    static class ViewHolder {
        SmartImageView ivImage;
    }
}

package myho.gridimagesearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import myho.gridimagesearch.R;
import myho.gridimagesearch.helpers.CircleTransform;
import myho.gridimagesearch.models.ImageInfo;

public class ImageInfoArrayAdapter extends ArrayAdapter<ImageInfo> {
    public ImageInfoArrayAdapter(Context context, List<ImageInfo> images) {
        super(context, R.layout.item_image_result, images);
    }

    // default version return object's toString()
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for this position
        ImageInfo imageInfo = this.getItem(position);
        ImageView ivImage;

        // check if an existing view is being reused, otherwise inflate the view
        if(convertView == null) {
            // instantiate the file into an in-memory java object
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = inflator.inflate(R.layout.item_image_result, parent, false);
        }

        ivImage = (ImageView) convertView;

        Picasso
                .with(getContext())
                .load(imageInfo.getThumbUrl())
                .placeholder(null)
                .resize(100, 100)
                .centerCrop()
                .transform(new CircleTransform())
                .into(ivImage);

        return ivImage;
    }
}

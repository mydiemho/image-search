package myho.gridimagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ImageInfo implements Parcelable {
    private String fullUrl;
    private String thumbUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullUrl);
        dest.writeString(this.thumbUrl);
    }

    public static Parcelable.Creator<ImageInfo> CREATOR = new Parcelable.Creator<ImageInfo>() {
        public ImageInfo createFromParcel(Parcel source) {
            return new ImageInfo(source);
        }

        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };

    public String getFullUrl() {
        return fullUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    @Override
    public String toString() {
        return this.thumbUrl;
    }

    public static List<ImageInfo> fromJSONArray(JSONArray imageJsonResults) {
        List<ImageInfo> results = new ArrayList<ImageInfo>();
        for (int i = 0; i < imageJsonResults.length(); i++) {
            try {
                results.add(new ImageInfo(imageJsonResults.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    private ImageInfo(JSONObject jsonObject) {
        try {
            this.fullUrl = jsonObject.getString("url");
            this.thumbUrl = jsonObject.getString("tbUrl");

        } catch (JSONException e) {
            this.fullUrl = null;
            this.thumbUrl = null;
        }
    }

    private ImageInfo(Parcel in) {
        this.fullUrl = in.readString();
        this.thumbUrl = in.readString();
    }
}

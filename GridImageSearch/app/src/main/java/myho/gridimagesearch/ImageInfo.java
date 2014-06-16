package myho.gridimagesearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageInfo implements Serializable{
    private String fullUrl;
    private String thumbUrl;

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
}


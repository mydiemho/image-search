package myho.gridimagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;

public class FilterInfo implements Parcelable {

    private String imageSize;
    private String imageColor;
    private String imageType;
    private String site;

    public FilterInfo(String imageSize, String imageColor, String imageType, String site) {
        this.imageSize = imageSize;
        this.imageColor = imageColor;
        this.imageType = imageType;
        this.site = site.trim();
    }

    public String getImageSize() {
        return imageSize;
    }

    public String getImageColor() {
        return imageColor;
    }

    public String getImageType() {
        return imageType;
    }

    public String getSite() {
        return site;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("imageSize", imageSize)
                .add("imageColor", imageColor)
                .add("imageType", imageType)
                .add("site", site)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterInfo)) return false;

        FilterInfo that = (FilterInfo) o;

        if (!imageColor.equals(that.imageColor)) return false;
        if (!imageSize.equals(that.imageSize)) return false;
        if (!imageType.equals(that.imageType)) return false;
        if (!site.equals(that.site)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = imageSize.hashCode();
        result = 31 * result + imageColor.hashCode();
        result = 31 * result + imageType.hashCode();
        result = 31 * result + site.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageSize);
        dest.writeString(this.imageColor);
        dest.writeString(this.imageType);
        dest.writeString(this.site);
    }

    private FilterInfo(Parcel in) {
        this.imageSize = in.readString();
        this.imageColor = in.readString();
        this.imageType = in.readString();
        this.site = in.readString();
    }

    public static Parcelable.Creator<FilterInfo> CREATOR = new Parcelable.Creator<FilterInfo>() {
        public FilterInfo createFromParcel(Parcel source) {
            return new FilterInfo(source);
        }

        public FilterInfo[] newArray(int size) {
            return new FilterInfo[size];
        }
    };
}

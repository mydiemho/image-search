package myho.gridimagesearch;

import com.google.common.base.Objects;

import java.io.Serializable;

public class FilterInfo implements Serializable {

    private String imageSize;
    private String imageColor;
    private String imageType;
    private String site;

    public FilterInfo(String imageSize, String imageColor, String imageType, String site) {
        this.imageSize = imageSize;
        this.imageColor = imageColor;
        this.imageType = imageType;
        this.site = site;
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
}

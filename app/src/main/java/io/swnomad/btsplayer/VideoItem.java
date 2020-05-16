package io.swnomad.btsplayer;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoItem implements Parcelable {

    String title;
    String date;
    String video_id;
    String thumbnailUrl;

    public VideoItem(String title, String date, String video_id, String thumbnailUrl) {
        this.title = title;
        this.date = date;
        this.video_id = video_id;
        this.thumbnailUrl = thumbnailUrl;
    }

    public VideoItem(Parcel in){
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in){
        title = in.readString();
        date = in.readString();
        video_id = in.readString();
        thumbnailUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(video_id);
        dest.writeString(thumbnailUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getVideoId() {
        return video_id;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public VideoItem createFromParcel(Parcel in) {
            return new VideoItem(in);
        }

        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };
}
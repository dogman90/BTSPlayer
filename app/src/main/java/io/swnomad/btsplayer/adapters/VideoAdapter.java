package io.swnomad.btsplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import io.swnomad.btsplayer.PipActivity;
import io.swnomad.btsplayer.R;
import io.swnomad.btsplayer.VideoItem;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<VideoItem> mVideoList;

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView videoTitle;

        MyViewHolder(View view) {

            super(view);

            thumbnail =  view.findViewById(R.id.thumbnail);
            videoTitle =  view.findViewById(R.id.videoTitle);
            Button addFavoriteBtn = view.findViewById(R.id.addFavoriteBtn);

            addFavoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    VideoItem video = mVideoList.get(MyViewHolder.this.getAdapterPosition());
                    String title = video.getTitle();
                    String videoId = video.getVideoId();
                    String pubDate = video.getDate();
                    String thumbnailUrl = video.getThumbnailUrl();

                    SQLiteDatabase db = mContext.openOrCreateDatabase("favorites",
                            Context.MODE_ENABLE_WRITE_AHEAD_LOGGING, null);

                    String sql1 = "select * from videos where videoId=" + "'" + videoId + "'";
                    Cursor cursor = db.rawQuery(sql1, null);

                    if (cursor.getCount() <= 0) {
                        String sql = "insert into videos(title, videoId, pubDate, thumbnailUrl) " +
                                "values(" + "\"" + title + "\"," + "'" + videoId + "'," + "'" + pubDate + "'," + "'" + thumbnailUrl + "');";
                        db.execSQL(sql);
                        Snackbar.make(v, R.string.favAddMsg, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(v, R.string.favAlreadyMsg, Snackbar.LENGTH_SHORT).show();
                    }

                    db.close();
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = MyViewHolder.this.getAdapterPosition();
                    Intent intent = new Intent(mContext, PipActivity.class);
                    intent.putExtra("pos", pos);
                    intent.putExtra("videoList", mVideoList);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public VideoAdapter(Context context, ArrayList<VideoItem> videoList) {
        mContext = context;
        mVideoList = videoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.video_item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        VideoItem singleVideo = mVideoList.get(position);
        holder.videoTitle.setText(singleVideo.getTitle());
        Glide.with(mContext).load(singleVideo.getThumbnailUrl()).override(400,270).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }
}

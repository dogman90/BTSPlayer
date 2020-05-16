package io.swnomad.btsplayer.adapters;

import android.content.Context;
import android.content.Intent;
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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<VideoItem> mFavoriteList;

    private VideoItem singleVideo;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView videoTitle;
        //TextView videoDate;

        public MyViewHolder(View view) {

            super(view);

            thumbnail =  view.findViewById(R.id.thumbnail);
            videoTitle =  view.findViewById(R.id.videoTitle);
            //videoDate = view.findViewById(R.id.videoDate);
            Button deleteFavoriteBtn = view.findViewById(R.id.delFavoriteBtn);

            deleteFavoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoItem video = mFavoriteList.get(getAdapterPosition());
                    String videoId = video.getVideoId();

                    mFavoriteList.remove(getAdapterPosition());

                    SQLiteDatabase db = mContext.openOrCreateDatabase("favorites",
                            Context.MODE_ENABLE_WRITE_AHEAD_LOGGING,null);
                    String[] whereArgs = {videoId};
                    db.delete("videos", "videoId = ?", whereArgs);
                    db.close();

                    notifyDataSetChanged();
                    Snackbar.make(v, R.string.favDelMsg, Snackbar.LENGTH_SHORT).show();
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = MyViewHolder.this.getAdapterPosition();
                    Intent intent = new Intent(mContext, PipActivity.class);
                    intent.putExtra("pos", pos);
                    intent.putExtra("videoList", mFavoriteList);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public FavoriteAdapter(Context context, ArrayList<VideoItem> videoList) {
        mContext = context;
        mFavoriteList = videoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.favorite_item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        singleVideo = mFavoriteList.get(position);
        holder.videoTitle.setText(singleVideo.getTitle());
        //holder.videoDate.setText(singleVideo.getDate());
        Glide.with(mContext).load(singleVideo.getThumbnailUrl()).override(400,270).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mFavoriteList.size();
    }
}

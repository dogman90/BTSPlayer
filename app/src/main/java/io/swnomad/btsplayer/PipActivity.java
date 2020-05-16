package io.swnomad.btsplayer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class PipActivity extends AppCompatActivity {
    private YouTubePlayerView youTubePlayerView;
    int playPos;
    ArrayList<VideoItem> mPlayList;

    int prevPos;
    int nextPos;

    Handler handler = new Handler();

    TextView videoTitle;

    ImageButton playPauseBtn;
    ImageButton prevBtn;
    ImageButton nextBtn;
    ImageButton repeatBtn;

    public static final int REPEAT_ALL = 0;
    public static final int REPEAT_ONE = 1;

    int repeatMode = REPEAT_ALL;

    CardView prevView;
    CardView nextView;
    ImageView prevThumbnail;
    ImageView nextThumbnail;
    TextView prevTitle;
    TextView nextTitle;

    private YouTubePlayerListener listener = new YouTubePlayerListener() {
        @Override
        public void onReady(final YouTubePlayer youTubePlayer) {
            videoTitle.setText(mPlayList.get(playPos).getTitle());
            YouTubePlayerUtils.loadOrCueVideo(youTubePlayer, getLifecycle(), mPlayList.get(playPos).getVideoId(),0f);

            prevBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playPos = (playPos <= 0) ? mPlayList.size() - 1 : playPos - 1;
                    prevPos = (playPos <= 0) ? mPlayList.size() - 1 : playPos - 1;
                    nextPos = (playPos >= mPlayList.size() - 1) ? 0 : playPos + 1;
                    youTubePlayer.cueVideo(mPlayList.get(playPos).getVideoId(), 0);
                }
            });

            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playPos = (playPos >= mPlayList.size() - 1) ? 0 : playPos + 1;
                    prevPos = (playPos <= 0) ? mPlayList.size() - 1 : playPos - 1;
                    nextPos = (playPos >= mPlayList.size() - 1) ? 0 : playPos + 1;
                    youTubePlayer.cueVideo(mPlayList.get(playPos).getVideoId(), 0);
                }
            });

            prevView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playPos = (playPos <= 0) ? mPlayList.size() - 1 : playPos - 1;
                    prevPos = (playPos <= 0) ? mPlayList.size() - 1 : playPos - 1;
                    nextPos = (playPos >= mPlayList.size() - 1) ? 0 : playPos + 1;
                    youTubePlayer.cueVideo(mPlayList.get(playPos).getVideoId(), 0);
                }
            });

            nextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playPos = (playPos >= mPlayList.size() - 1) ? 0 : playPos + 1;
                    prevPos = (playPos <= 0) ? mPlayList.size() - 1 : playPos - 1;
                    nextPos = (playPos >= mPlayList.size() - 1) ? 0 : playPos + 1;
                    youTubePlayer.cueVideo(mPlayList.get(playPos).getVideoId(), 0);
                }
            });
        }

        @Override
        public void onStateChange(@NonNull final YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {

            switch(playerState){
                case ENDED:
                    if(repeatMode == REPEAT_ALL){
                        playPos = (playPos >= mPlayList.size()-1) ? 0 : playPos+1;
                        prevPos = (playPos <= 0) ? mPlayList.size()-1 : playPos-1;
                        nextPos = (playPos >= mPlayList.size()-1) ? 0 : playPos+1;
                    }else if(repeatMode == REPEAT_ONE){
                        //exception
                    }

                    youTubePlayer.cueVideo(mPlayList.get(playPos).getVideoId(), 0);

                    break;

                case VIDEO_CUED:
                    videoTitle.setText(mPlayList.get(playPos).getTitle());
                    youTubePlayer.play();

                    playPauseBtn.setImageResource(R.drawable.pause_icon);

                    Glide.with(PipActivity.this)
                            .load(mPlayList.get(prevPos).getThumbnailUrl())
                            .override(280,180).into(prevThumbnail);
                    prevTitle.setText(mPlayList.get(prevPos).getTitle());

                    Glide.with(PipActivity.this)
                            .load(mPlayList.get(nextPos).getThumbnailUrl())
                            .override(280,180).into(nextThumbnail);
                    nextTitle.setText(mPlayList.get(nextPos).getTitle());

                    break;

                case PLAYING:
                    playPauseBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            youTubePlayer.pause();
                            playPauseBtn.setImageResource(R.drawable.play_icon);
                        }
                    });
                    break;

                case PAUSED:
                    playPauseBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            youTubePlayer.play();
                            playPauseBtn.setImageResource(R.drawable.pause_icon);
                        }
                    });
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {

        }

        @Override
        public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {

        }

        @Override
        public void onError(final YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {

            switch(playerError){
                case VIDEO_NOT_FOUND: case UNKNOWN: case VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER:
                    if(playPos < mPlayList.size()-1){
                        playPos++;
                    }else{
                        playPos = 0;
                    }

                    Runnable runnable = new Runnable(){
                        @Override
                        public void run() {
                            youTubePlayer.cueVideo(mPlayList.get(playPos).getVideoId(), 0);
                        }
                    };

                    handler.post(runnable);
            }
        }

        @Override
        public void onCurrentSecond(YouTubePlayer youTubePlayer, float v) {

        }

        @Override
        public void onVideoDuration(YouTubePlayer youTubePlayer, float v) {

        }

        @Override
        public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {

        }

        @Override
        public void onVideoId(YouTubePlayer youTubePlayer, String s) {

        }

        @Override
        public void onApiChange(YouTubePlayer youTubePlayer) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pip);

        Intent intent = getIntent();
        mPlayList = intent.getParcelableArrayListExtra("videoList");
        playPos = intent.getIntExtra("pos", 0);
        prevPos = (playPos <= 0) ? mPlayList.size()-1 : playPos -1;
        nextPos = (playPos >= mPlayList.size()-1) ? 0 : playPos+1;

        playPauseBtn = findViewById(R.id.playPauseBtn);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);

        initYouTubePlayerView();

        videoTitle = findViewById(R.id.videoTitle);

        repeatBtn = findViewById(R.id.repeatBtn);
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatMode == REPEAT_ALL) {
                    repeatMode = REPEAT_ONE;
                    repeatBtn.setImageResource(R.drawable.repeat_one_icon);
                } else if (repeatMode == REPEAT_ONE) {
                    repeatMode = REPEAT_ALL;
                    repeatBtn.setImageResource(R.drawable.repeat_all_icon);
                }
            }
        });

        ImageButton pipBtn = findViewById(R.id.pipBtn);
        pipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    boolean supportsPIP = PipActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
                    if (supportsPIP) {
                        PipActivity.this.enterPictureInPictureMode();
                    }
                } else {
                    new AlertDialog.Builder(PipActivity.this)
                            .setTitle("Can't enter picture in picture mode")
                            .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
                            .show();
                }
            }
        });

       prevThumbnail = findViewById(R.id.prevThumbnail);
       prevTitle = findViewById(R.id.prevTitle);
       nextThumbnail = findViewById(R.id.nextThumbnail);
       nextTitle = findViewById(R.id.nextTitle);

       Glide.with(PipActivity.this)
               .load(mPlayList.get(prevPos).getThumbnailUrl())
               .override(280,190).into(prevThumbnail);
       prevTitle.setText(mPlayList.get(prevPos).getTitle());

        Glide.with(PipActivity.this)
                .load(mPlayList.get(nextPos).getThumbnailUrl())
                .override(280,190).into(nextThumbnail);
        nextTitle.setText(mPlayList.get(nextPos).getTitle());

        prevView = findViewById(R.id.prevView);
        nextView = findViewById(R.id.nextView);
    }

    private void initYouTubePlayerView() {
        youTubePlayerView = findViewById(R.id.youtubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(listener);
        initPictureInPicture(youTubePlayerView);
    }

    private void initPictureInPicture(final YouTubePlayerView youTubePlayerView) {
        ImageView pictureInPictureIcon = new ImageView(this);
        pictureInPictureIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_picture_in_picture_24dp));

        pictureInPictureIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    boolean supportsPIP = PipActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
                    if (supportsPIP) {
                        PipActivity.this.enterPictureInPictureMode();
                    }
                } else {
                    new AlertDialog.Builder(PipActivity.this)
                            .setTitle("Can't enter picture in picture mode")
                            .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
                            .show();
                }
            }
        });

        youTubePlayerView.getPlayerUiController().addView(pictureInPictureIcon);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        if(isInPictureInPictureMode) {
            youTubePlayerView.enterFullScreen();
            youTubePlayerView.getPlayerUiController().showUi(false);
            youTubePlayerView.addYouTubePlayerListener(listener);
        } else {
            youTubePlayerView.exitFullScreen();
            youTubePlayerView.getPlayerUiController().showUi(true);
        }
    }

}
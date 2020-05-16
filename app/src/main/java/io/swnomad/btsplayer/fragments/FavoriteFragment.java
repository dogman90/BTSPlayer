package io.swnomad.btsplayer.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import io.swnomad.btsplayer.adapters.FavoriteAdapter;
import io.swnomad.btsplayer.R;
import io.swnomad.btsplayer.VideoItem;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;

    private FavoriteAdapter adapter;

    private InterstitialAd mInterstitialAd;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_favorite, container, false);

        /* 광고 초기화 */
        MobileAds.initialize(getContext(), getString(R.string.app_id));

        /*전면광고*/
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.front_ad_id));
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        /*배너광고*/
        AdView bannerAdView = rootView.findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FavoriteAdapter(getContext(), getFavoriteVideoList());
        recyclerView.setAdapter(adapter);
        int number = adapter.getItemCount();
        //getActivity().setTitle("즐겨찾기("+number+"개 영상)");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public ArrayList<VideoItem> getFavoriteVideoList() {
        ArrayList<VideoItem> videoItems = new ArrayList<>();

        SQLiteDatabase db = getContext().openOrCreateDatabase("favorites",
                getContext().MODE_ENABLE_WRITE_AHEAD_LOGGING, null);
        Cursor cursor = db.rawQuery("SELECT * FROM videos", null);
        //int count = cursor.getCount(); //데이터의 갯수
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String title = cursor.getString(0); // 커서가 가르키는 column0(title) 받기
            String videoId = cursor.getString(1);
            String pubDate = cursor.getString(2);
            String thumbnailUrl = cursor.getString(3);

            videoItems.add(new VideoItem(title, pubDate, videoId, thumbnailUrl));

            cursor.moveToNext(); // 다음 데이터로 이동
        }

        return videoItems;
    }
}
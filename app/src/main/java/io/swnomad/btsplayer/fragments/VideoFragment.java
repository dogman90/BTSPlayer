package io.swnomad.btsplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.swnomad.btsplayer.R;
import io.swnomad.btsplayer.VideoItem;


public abstract class VideoFragment extends Fragment {

    RecyclerView.Adapter adapter;

    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = getAdapter();
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = inflate(inflater, container);

        /* 광고 초기화 *//*
        MobileAds.initialize(getContext(), getString(R.string.app_id));

        *//*전면광고*//*
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.front_ad_id));
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        *//*배너광고*//*
        AdView bannerAdView = rootView.findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);*/

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    public abstract ViewGroup inflate(LayoutInflater inflater, ViewGroup container);

    public abstract ArrayList<VideoItem> getVideoItems();

    public abstract RecyclerView.Adapter getAdapter();
}

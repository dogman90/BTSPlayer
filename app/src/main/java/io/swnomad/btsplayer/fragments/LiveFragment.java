package io.swnomad.btsplayer.fragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.swnomad.btsplayer.R;
import io.swnomad.btsplayer.VideoInfoReader;
import io.swnomad.btsplayer.VideoItem;

public class LiveFragment extends VideoFragment {

    VideoInfoReader reader = new VideoInfoReader();

    @Override
    public ViewGroup inflate(LayoutInflater inflater, ViewGroup container) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_live, container, false);
    }

    @Override
    public ArrayList<VideoItem> getVideoItems() {
        return VideoInfoReader.getLiveItems();
    }
}

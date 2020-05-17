package io.swnomad.btsplayer.fragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.swnomad.btsplayer.R;
import io.swnomad.btsplayer.VideoItem;
import io.swnomad.btsplayer.adapters.FavoriteAdapter;
import io.swnomad.btsplayer.datahandlers.SQLiteHandler;

public class FavoriteFragment extends VideoFragment {

    @Override
    public ViewGroup inflate(LayoutInflater inflater, ViewGroup container) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public ArrayList<VideoItem> getVideoItems() {
        return SQLiteHandler.getInstance(getContext()).getFavoriteItems();
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return new FavoriteAdapter(getContext(), getVideoItems());
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
    }
}
package com.glebdrozdov.vkhackathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;

public class ThirdFloorFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_floor, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        PhotoView photoView = (PhotoView) view.findViewById(R.id.third_floor_photo_view);
        photoView.setImageResource(R.drawable.map_hermitage_3);
        photoView.setScale(2.5f, true);
    }

}

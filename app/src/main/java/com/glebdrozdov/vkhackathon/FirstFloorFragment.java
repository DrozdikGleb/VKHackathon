package com.glebdrozdov.vkhackathon;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.zip.Inflater;


public class FirstFloorFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_floor,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        PhotoView photoView =(PhotoView) view.findViewById(R.id.first_floor_photo_view);
        /*Matrix inverse = new Matrix();
        photoView.getImageMatrix().invert(inverse);
        float[] touchPoint = new float[1000];
        photoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float[] touchPoint = new float[] {event.getX(), event.getY()};
                return true;
            }
        });
        inverse.mapPoints(touchPoint);*/
        photoView.setImageResource(R.drawable.map_hermitage_1);
    }
}

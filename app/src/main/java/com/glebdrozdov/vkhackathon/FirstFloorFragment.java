package com.glebdrozdov.vkhackathon;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;


public class FirstFloorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_floor, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        PhotoView photoView = (PhotoView) view.findViewById(R.id.first_floor_photo_view);
        final TextView textView = (TextView) view.findViewById(R.id.our_data);
        photoView.setOnScaleChangeListener(new OnScaleChangedListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                textView.setText(String.valueOf(scaleFactor));
            }
        });
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

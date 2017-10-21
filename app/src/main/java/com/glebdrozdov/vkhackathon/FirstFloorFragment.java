package com.glebdrozdov.vkhackathon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.OnSingleFlingListener;
import com.github.chrisbanes.photoview.PhotoView;

public class FirstFloorFragment extends Fragment {

    private float xLocation;
    private float yLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_floor, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        PhotoView photoView = (PhotoView) view.findViewById(R.id.first_floor_photo_view);
        photoView.setImageResource(R.drawable.map_hermitage_1);
        photoView.buildDrawingCache();
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map_hermitage_1)
                .copy(Bitmap.Config.ARGB_8888, true);
        Bitmap newBitmap = getOurRoute(myBitmap);
        photoView.setImageBitmap(newBitmap);
        final TextView textView = (TextView) view.findViewById(R.id.our_data);
        photoView.setOnScaleChangeListener(new OnScaleChangedListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                textView.setText(String.valueOf(scaleFactor) + " " + String.valueOf(focusX) + " " + String.valueOf(focusY));
            }
        });
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                textView.setText(String.valueOf(x) + " " + String.valueOf(y));
            }
        });

    }

    public Bitmap getOurRoute(Bitmap previousBitmap) {
        Canvas canvas = new Canvas(previousBitmap);
        float heightBitmap = previousBitmap.getHeight();
        float widthBitmap = previousBitmap.getWidth();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(6);
        canvas.drawLine(widthBitmap * 0.435f, heightBitmap * 0.52f, widthBitmap * 0.5f, heightBitmap * 0.52f, paint);
        return previousBitmap;
    }
}

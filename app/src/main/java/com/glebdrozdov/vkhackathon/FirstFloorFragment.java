package com.glebdrozdov.vkhackathon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

public class FirstFloorFragment extends Fragment {

    private float xLocation;
    private float yLocation;
    private int roomNumber;

    public int getNumber() {
        return roomNumber;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_floor, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RoomCoordinatesFiller roomFilter = new RoomCoordinatesFiller();
        roomFilter.fillRooms();
        PhotoView photoView = (PhotoView) view.findViewById(R.id.first_floor_photo_view);
        photoView.setImageResource(R.drawable.map_hermitage_1);
        photoView.buildDrawingCache();
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map_hermitage_1)
                .copy(Bitmap.Config.ARGB_8888, true);
        Bitmap newBitmap = getOurRoute(myBitmap);
        photoView.setImageBitmap(newBitmap);
        photoView.setScale(2.5f, true);
        final TextView textView = (TextView) view.findViewById(R.id.our_data);
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                xLocation = x * 800;
                yLocation = y * 376;
                Log.i("location", String.valueOf(xLocation) + " " + String.valueOf(yLocation));
                textView.setText(String.valueOf(xLocation) + " " + String.valueOf(yLocation));
                int k = 0;
                for (int i = 0; i < 107; i++) {
                    if (RoomCoordinatesFiller.rooms[i].top != 0) {
                        Room room = RoomCoordinatesFiller.rooms[i];
                        if ((room.left < xLocation) && (room.right > xLocation) && (room.top < yLocation) && (room.bottom > yLocation)) {
                            roomNumber = i;
                            Toast.makeText(getActivity(), "Вы выбрали комнату " + String.valueOf(roomNumber), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    } else {
                        k++;
                    }
                }

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

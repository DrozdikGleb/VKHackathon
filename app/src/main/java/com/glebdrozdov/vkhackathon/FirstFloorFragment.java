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
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

public class FirstFloorFragment extends Fragment {

    private float xLocation;
    private float yLocation;
    private int roomNumber;
    private PhotoView photoView;
    private float BitmapWidth;
    private float BitmapHeight;
    private static float coefficientX;
    private static float coefficientY;
    float previousCenterX = 0;
    float previousCenterY = 0;

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
        photoView = (PhotoView) view.findViewById(R.id.first_floor_photo_view);
        photoView.setImageResource(R.drawable.map_hermitage_1);
        photoView.buildDrawingCache();
        Bitmap previousBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map_hermitage_1)
                .copy(Bitmap.Config.ARGB_8888, true);
        Bitmap newBitmap = getOurRoute(previousBitmap);
        BitmapHeight = newBitmap.getHeight();
        BitmapWidth = newBitmap.getWidth();
        coefficientX = newBitmap.getWidth() / 800;
        coefficientY = newBitmap.getHeight() / 376;
        photoView.setImageBitmap(newBitmap);
        photoView.setScale(2.5f, true);
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                xLocation = x * 800;
                yLocation = y * 376;
                for (int i = 0; i < 131; i++) {
                    if (RoomCoordinatesFiller.rooms[i].top != 0) {
                        Room room = RoomCoordinatesFiller.rooms[i];
                        if ((room.left < xLocation) && (room.right > xLocation) && (room.top < yLocation) && (room.bottom > yLocation)) {
                            roomNumber = i;
                            if (i == 0) {
                                Toast.makeText(getActivity(), "Вы выбрали выход", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            Toast.makeText(getActivity(), "Вы выбрали комнату " + String.valueOf(roomNumber), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    } else {
                    }
                }

            }
        });

    }

    public Bitmap getOurRoute(Bitmap previousBitmap) {
        String route = RoomsActivity.routeToDraw;
        if (route != null) {
            String[] strRoute = route.split("\\s+");
            int routeSize = strRoute.length;
            int[] intRoute = new int[routeSize];
            for (int i = 0; i < routeSize; i++) {
                intRoute[i] = Integer.parseInt(strRoute[i]);
            }
            Canvas canvas = new Canvas(previousBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(6);
            for (int i = 0; i < routeSize; i++) {
                if (i == 0) {
                    Room room = RoomCoordinatesFiller.rooms[intRoute[0]];
                    previousCenterX = room.centerX * coefficientX;
                    previousCenterY = room.centerY * coefficientY;
                } else {
                    Room room = RoomCoordinatesFiller.rooms[intRoute[i]];
                    if (room.left != 0) {
                        canvas.drawLine(previousCenterX, previousCenterY, room.centerX * coefficientX, room.centerY * coefficientY, paint);
                        previousCenterX = room.centerX * coefficientX;
                        previousCenterY = room.centerY * coefficientY;
                    }
                }
            }
        }
        return previousBitmap;
    }
}

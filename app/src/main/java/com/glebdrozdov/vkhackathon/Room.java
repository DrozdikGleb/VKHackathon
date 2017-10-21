package com.glebdrozdov.vkhackathon;

/**
 * Created by apple on 21.10.17.
 */

public class Room {

    public int top;
    public int bottom;
    public int left;
    public int right;
    public float centerX;
    public float centerY;

    Room(int left, int right, int top, int bottom) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        centerX = left + (right - left) / 2;
        centerY = top + (bottom - top) / 2;
    }


}

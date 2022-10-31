package com.hangoverstudios.romantic.photo.frames.love.photo.editor.views;

import android.view.MotionEvent;

public class TouchManager {
    private final int maxNumberOfTouchPoints;
    private final Vector2D[] points;
    private final Vector2D[] previousPoints;

    public TouchManager(int maxNumberOfTouchPoints2) {
        this.maxNumberOfTouchPoints = maxNumberOfTouchPoints2;
        this.points = new Vector2D[maxNumberOfTouchPoints2];
        this.previousPoints = new Vector2D[maxNumberOfTouchPoints2];
    }

    public boolean isPressed(int index) {
        return this.points[index] != null;
    }

    public int getPressCount() {
        int count = 0;
        for (Vector2D point : this.points) {
            if (point != null) {
                count++;
            }
        }
        return count;
    }

    public Vector2D moveDelta(int index) {
        Vector2D previous;
        if (!isPressed(index)) {
            return new Vector2D();
        }
        if (this.previousPoints[index] != null) {
            previous = this.previousPoints[index];
        } else {
            previous = this.points[index];
        }
        return Vector2D.subtract(this.points[index], previous);
    }

    private static Vector2D getVector(Vector2D a, Vector2D b) {
        if (a != null && b != null) {
            return Vector2D.subtract(b, a);
        }
        throw new RuntimeException("can't do this on nulls");
    }

    public Vector2D getPoint(int index) {
        return this.points[index] != null ? this.points[index] : new Vector2D();
    }

    public Vector2D getPreviousPoint(int index) {
        if (this.previousPoints[index] != null) {
            return this.previousPoints[index];
        }
        return new Vector2D();
    }

    public Vector2D getVector(int indexA, int indexB) {
        return getVector(this.points[indexA], this.points[indexB]);
    }

    public Vector2D getPreviousVector(int indexA, int indexB) {
        if (this.previousPoints[indexA] == null || this.previousPoints[indexB] == null) {
            return getVector(this.points[indexA], this.points[indexB]);
        }
        return getVector(this.previousPoints[indexA], this.previousPoints[indexB]);
    }

    public void update(MotionEvent event) {
        if ((event.getAction() & 255) == 1) {
            int index = event.getAction() >> 8;
            Vector2D[] vector2DArr = this.previousPoints;
            this.points[index] = null;
            vector2DArr[index] = null;
            return;
        }
        for (int i = 0; i < this.maxNumberOfTouchPoints; i++) {
            if (i < event.getPointerCount()) {
                int index2 = event.getPointerId(i);
                Vector2D newPoint = new Vector2D(event.getX(i), event.getY(i));
                if (this.points[index2] == null) {
                    this.points[index2] = newPoint;
                } else {
                    if (this.previousPoints[index2] != null) {
                        this.previousPoints[index2].set(this.points[index2]);
                    } else {
                        this.previousPoints[index2] = new Vector2D(newPoint);
                    }
                    if (Vector2D.subtract(this.points[index2], newPoint).getLength() < 1000.0f) {
                        this.points[index2].set(newPoint);
                    }
                }
            } else {
                Vector2D[] vector2DArr2 = this.previousPoints;
                this.points[i] = null;
                vector2DArr2[i] = null;
            }
        }
    }
}

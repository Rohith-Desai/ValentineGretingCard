package com.hangoverstudios.romantic.photo.frames.love.photo.editor.views;

public class Vector2D {

    /* renamed from: x */
    private float f4527x;

    /* renamed from: y */
    private float f4528y;

    public Vector2D() {
    }

    public Vector2D(Vector2D v) {
        this.f4527x = v.f4527x;
        this.f4528y = v.f4528y;
    }

    public Vector2D(float x, float y) {
        this.f4527x = x;
        this.f4528y = y;
    }

    public float getX() {
        return this.f4527x;
    }

    public float getY() {
        return this.f4528y;
    }

    public float getLength() {
        return (float) Math.sqrt((double) ((this.f4527x * this.f4527x) + (this.f4528y * this.f4528y)));
    }

    public Vector2D set(Vector2D other) {
        this.f4527x = other.getX();
        this.f4528y = other.getY();
        return this;
    }

    public Vector2D set(float x, float y) {
        this.f4527x = x;
        this.f4528y = y;
        return this;
    }

    public Vector2D add(Vector2D value) {
        this.f4527x += value.getX();
        this.f4528y += value.getY();
        return this;
    }

    public static Vector2D subtract(Vector2D lhs, Vector2D rhs) {
        return new Vector2D(lhs.f4527x - rhs.f4527x, lhs.f4528y - rhs.f4528y);
    }

    public static float getDistance(Vector2D lhs, Vector2D rhs) {
        return subtract(lhs, rhs).getLength();
    }

    public static float getSignedAngleBetween(Vector2D a, Vector2D b) {
        Vector2D na = getNormalized(a);
        Vector2D nb = getNormalized(b);
        return (float) (Math.atan2((double) nb.f4528y, (double) nb.f4527x) - Math.atan2((double) na.f4528y, (double) na.f4527x));
    }

    public static Vector2D getNormalized(Vector2D v) {
        float l = v.getLength();
        if (l == 0.0f) {
            return new Vector2D();
        }
        return new Vector2D(v.f4527x / l, v.f4528y / l);
    }

    public String toString() {
        return String.format("(%.4f, %.4f)", new Object[]{Float.valueOf(this.f4527x), Float.valueOf(this.f4528y)});
    }
}

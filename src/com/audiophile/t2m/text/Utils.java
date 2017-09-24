package com.audiophile.t2m.text;

/**
 * @author Simon
 * Created on 24.09.2017.
 */
public class Utils {

    // quadratic smoothing
    public static float[] smoothData(float[] data, int radius) {
        radius++;
        int length = data.length;
        if (length < 1)
            return new float[0];
        if (length < 2)
            return new float[]{data[0]};

        if (radius > length / 2)
            radius = length / 2;
        float[] result = new float[length];
        float s = radius * radius;
        for (int i = 0; i < length; i++) {
            float sum = 0;
            for (int r = -radius; r <= radius; r++)
                sum += data[clamp(i + r, 0, length - 1)] * (radius - Math.abs(r));
            result[i] = sum / s;
        }
        return result;
    }

    private static int clamp(int value, int min, int max) {
        return value < min ? min : (value > max ? max : value);
    }
}

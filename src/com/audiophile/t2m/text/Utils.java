package com.audiophile.t2m.text;

/**
 * @author Simon
 */
public class Utils {

    /**
     * Applies a one-dimensional gaussian blur to the array, to remove jumps between values.
     * The original array is not changed.
     * @param data The source data, which is blurred.
     * @param radius The radius of the blur. Maximum is the half of the data array length
     * @return A new array with the blur applied to the source data.
     */
    public static float[] BlurData(float[] data, int radius) {
        radius++;
        int length = data.length;
        if (length < 1)
            return new float[0];
        if (length < 2)
            return new float[]{data[0]};
        // Assertion for bug finding
        assert radius>length/2;

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

    /**
     * Clamps a value to a certain range.
     * <p>
     *     Examples:
     *     <ul>
     *         <li>clamp(4,0,5) => 4</li>
     *         <li>clamp(-2,-1,3) => -1</li>
     *         <li>clamp(100,10,50) => 50</li>
     *     </ul>
     *
     * </p>
     * @param value The value to clamp
     * @param min The minimum value of the result.
     * @param max The maximum value of the result.
     * @return The value if it is within the given range. Else the bounds are returned.
     */
    public static int clamp(int value, int min, int max) {
        return value < min ? min : (value > max ? max : value);
    }
}

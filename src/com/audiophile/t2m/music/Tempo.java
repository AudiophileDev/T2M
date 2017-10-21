package com.audiophile.t2m.music;

import java.util.HashMap;


public class Tempo {
    private HashMap<Float, Integer> tempo = new HashMap<>();
    private Integer bpm;


    private Integer averageBpm = 0;
    private float minLength, maxLength;


    /**
     * Sets span of BPMs
     * Generates different tempos out of the average word length
     * creates HashMap with word length to BPM mapping
     *
     * @param avgWordLengths
     */
    public Tempo(float[] avgWordLengths) {
        getBounds(avgWordLengths);
        float ratio = (76 / this.getMinLength() + 200 /  this.maxLength) / 2; //75 = lower bpm bound, 200=higher bpm bound
        for (Float f : avgWordLengths) {
            bpm = Math.round(f * ratio);
            tempo.put(f, bpm);
            averageBpm += bpm;
            //System.out.println(String.format("%.2f -> %d", f, tempo.get(f)));
        }
        System.out.println("Average Tempo:" + (averageBpm /= avgWordLengths.length));
    }

    /**
     * returns minmal and maximal average word length in order to calculate a ratio
     *
     * @param avgWordLengths sets minimal and maximal average word length
     */
    public void getBounds(float[] avgWordLengths) {
        this.maxLength = avgWordLengths[0];
        this.minLength = avgWordLengths[0];
        for (float f : avgWordLengths) {
            if (f < minLength) this.minLength = f;
            if (f > maxLength) this.maxLength = f;
        }
    }

    public Float getMinLength() {
        return minLength;
    }

    public void setMinLength(Float minLength) {
        this.minLength = minLength;
    }

    public Float getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Float maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getAverageBpm() {
        return averageBpm;
    }

}

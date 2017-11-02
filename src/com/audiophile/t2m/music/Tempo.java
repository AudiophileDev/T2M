package com.audiophile.t2m.music;

import java.util.HashMap;


public class Tempo {
    private Integer averageBpm = 0;
    private float minLength, maxLength;

    /**
     * Sets span of BPMs
     * Generates different tempos out of the average word length
     * creates HashMap with word length to BPM mapping
     *
     * @param avgWordLengths The average word length in the article for every sentence
     */
    Tempo(float[] avgWordLengths) {
        getBounds(avgWordLengths);
        float ratio = (76 / this.getMinLength() + 200 / this.maxLength) / 2; //75 = lower bpm bound, 200=higher bpm bound
        for (Float f : avgWordLengths) {
            Integer bpm = Math.round(f * ratio);
            HashMap<Float, Integer> tempo = new HashMap<>();
            tempo.put(f, bpm);
            averageBpm += bpm;
            //System.out.println(String.format("%.2f -> %d", f, tempo.get(f)));
        }
        this.averageBpm /= avgWordLengths.length;
    }

    /**
     * returns minmal and maximal average word length in order to calculate a ratio
     *
     * @param avgWordLengths sets minimal and maximal average word length
     */
    private void getBounds(float[] avgWordLengths) {
        this.maxLength = avgWordLengths[0];
        this.minLength = avgWordLengths[0];
        for (float f : avgWordLengths) {
            if (f < minLength) this.minLength = f;
            if (f > maxLength) this.maxLength = f;
        }
    }

    // TODO documentation
    private Float getMinLength() {
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

    Integer getAverageBpm() {
        return averageBpm;
    }

    void setAverageBpm(Integer averageBpm) {
        this.averageBpm = averageBpm;
    }


}

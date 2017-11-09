package com.audiophile.t2m.music;

public class Tempo {
    int averageBpm = 0, resolution;
    float minLength, maxLength;

    /**
     * Sets span of BPMs
     * Generates different tempos out of the average word length
     * creates HashMap with word length to BPM mapping
     *
     * @param avgWordLengths The average word length in the article for every sentence
     */
    Tempo(float[] avgWordLengths) {
        double m, t, diff;
        int maxTempo = 180, minTempo = 60;
        getBounds(avgWordLengths);
        if (this.maxLength == this.minLength)
            diff = this.maxLength;
        else diff = this.maxLength - this.minLength;
        m = (minTempo - maxTempo) / (diff);
        t = minTempo - m * this.maxLength;
        for (float x : avgWordLengths) {
            averageBpm += m * x + t;
        }
        this.averageBpm /= avgWordLengths.length;
        if (this.averageBpm > maxTempo) this.averageBpm = maxTempo;

        this.resolution = MidiUtils.BpmToRes(averageBpm);
    }

    /**
     * returns minimal and maximal average word length in order to calculate a ratio
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
}

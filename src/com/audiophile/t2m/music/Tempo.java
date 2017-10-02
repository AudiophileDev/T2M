package com.audiophile.t2m.musicGenerator;

import java.util.HashMap;


public class Tempo {
    HashMap<Float, Integer> tempo = new HashMap<>();
    Integer bpm, averageBpm = 0;

    /**
     * Sets span of BPMs
     * Generates different tempos out of the average word length
     * creates HashMap with word length to BPM mapping
     *
     * @param avgWordLengths
     */
    public Tempo(float[] avgWordLengths) {
        float min = this.getBounds(avgWordLengths).getMin(), max = this.getBounds(avgWordLengths).getMax();
        float ratio = (76 / min + 200 / max) / 2; //75 = lower bpm bound, 200=higher bpm bound
        for (Float f : avgWordLengths) {
            bpm = Math.round(f * ratio);
            tempo.put(f, bpm);
            averageBpm += bpm;
            //System.out.println(String.format("%.2f -> %d", f, tempo.get(f)));
        }
       //System.out.println("Average Tempo:" + (averageBpm /= avgWordLengths.length));

    }

    /**
     * returns minmal and maximal average word length in order to calculate a ratio
     *
     * @param avgWordLengths
     * @return minimal and maximal average word length
     */
    public MyResult getBounds(float[] avgWordLengths) {
        MyResult bounds = new MyResult(0.0F, 0.0F);
        float max = avgWordLengths[0], min = avgWordLengths[0];
        for (float f : avgWordLengths) {
            if (f < min) bounds.setMin(f);
            if (f > max) bounds.setMax(f);
        }
        return bounds;
    }

    private class MyResult {
        private Float min;
        private Float max;

        public MyResult(Float min, Float max) {
            this.min = min;
            this.max = max;
        }

        public Float getMin() {
            return min;
        }

        public void setMin(Float min) {
            this.min = min;
        }

        public Float getMax() {
            return max;
        }

        public void setMax(Float max) {
            this.max = max;
        }
    }
}

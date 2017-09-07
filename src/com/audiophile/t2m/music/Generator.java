package com.audiophile.t2m.music;

import java.util.HashMap;

public class Generator {
    private HashMap<Float, Integer> tempo; //andante 76-108; Moderato 108-120; Allegro 120-168; Presto 168-200

    public Generator(float[] avgWordLengths) {
        tempo = new HashMap<>();
        float min = this.getBounds(avgWordLengths).getMin(), max = this.getBounds(avgWordLengths).getMax();
        float ratio = (76 / min + 200 / max) / 2; //75 = lower bpm bound, 200=higher bpm bound
        for (Float f : avgWordLengths) {
            this.tempo.put(f, Math.round(f * ratio)); //temporary solution => real calculation will follow
            System.out.println(String.format("%.2f", f) + "->" + tempo.get(f));
        }
    }

    public V2 getBounds(float[] avgWordLengths) {
        V2 bounds = new V2(0.0F, 0.0F);
        float max = avgWordLengths[0], min = avgWordLengths[0];
        for (float f : avgWordLengths) {
            if (f < min) bounds.setMin(f);
            if (f > max) bounds.setMax(f);
        }
        return bounds;
    }
}

package com.audiophile.t2m.music;

//TODO documentation

/**
 * This class contains all of the important musical paramters
 */
public class MusicData {
    /**
     * Constant value for the tempo of the track
     */
    Tempo tempo;
    /**
     * Value for the dynamicGradient between 0-127
     */
    Dynamic dynamic;
    /**
     * The basic baseKey of the whole track
     */
    Harmony baseKey;

    public MusicData(Tempo tempo, Dynamic dynamic, Harmony baseKey) {
        this.tempo = tempo;
        this.dynamic = dynamic;
        this.baseKey = baseKey;
    }

    public Tempo getTempo() {
        return tempo;
    }

    public void setTempo(Tempo tempo) {
        this.tempo = tempo;
    }

    public Dynamic getDynamic() {
        return dynamic;
    }

    public Harmony getBaseKey() {
        return baseKey;
    }

    public void setBaseKey(Harmony baseKey) {
        this.baseKey = baseKey;
    }
}

package com.audiophile.t2m.music;

//TODO documentation

/**
 * This class contains all of the important musical paramters
 */
public class MusicData {
    /**
     * Constant value for the tempo of the track
     */
    private Tempo tempo;
    /**
     * Value for the dynamic between 0-127
     */
    private int dynamic;
    /**
     * The basic baseKey of the whole track
     */
    private Harmony baseKey;

    public MusicData(Tempo tempo, int dynamic, Harmony baseKey) {
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

    public int getDynamic() {
        return dynamic;
    }

    public void setDynamic(int dynamic) {
        this.dynamic = dynamic;
    }

    public Harmony getBaseKey() {
        return baseKey;
    }

    public void setBaseKey(Harmony baseKey) {
        this.baseKey = baseKey;
    }
}

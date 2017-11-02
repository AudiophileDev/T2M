package com.audiophile.t2m.music;

//TODO documentation
public class MusicData {
    private Tempo tempo;
    private int dynamic;
    private Harmony key;

    public MusicData(Tempo tempo, int dynamic, Harmony key) {
        this.tempo = tempo;
        this.dynamic = dynamic;
        this.key = key;
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

    public Harmony getKey() {
        return key;
    }

    public void setKey(Harmony key) {
        this.key = key;
    }
}

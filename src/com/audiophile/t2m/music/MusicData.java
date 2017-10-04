package com.audiophile.t2m.music;

public class MusicData {
    private Tempo tempo;
    private String dynamic;
    private Harmony key;

    public MusicData(Tempo tempo, String dynamic, Harmony key) {
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

    public String getDynamic() {
        return dynamic;
    }

    public void setDynamic(String dynamic) {
        this.dynamic = dynamic;
    }

    public Harmony getKey() {
        return key;
    }

    public void setKey(Harmony key) {
        this.key = key;
    }
}

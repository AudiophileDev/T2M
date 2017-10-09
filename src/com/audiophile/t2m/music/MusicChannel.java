package com.audiophile.t2m.music;

import com.audiophile.t2m.text.TextAnalyser;

public class MusicChannel {
    private MusicData musicData;
    private TextAnalyser textBase;

    public MusicChannel(MusicData musicData, TextAnalyser textBase) {
        this.musicData = musicData;
        this.textBase = textBase;
    }

    public MusicData getMusicData() {
        return musicData;
    }

    public void setMusicData(MusicData musicData) {
        this.musicData = musicData;
    }

    public TextAnalyser getTextBase() {
        return textBase;
    }

    public void setTextBase(TextAnalyser textBase) {
        this.textBase = textBase;
    }
}

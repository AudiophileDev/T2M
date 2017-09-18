package com.audiophile.t2m.MusicGenerator;

import javax.sound.midi.Sequence;

public class Merger {
    private RhythmChannel rhythmChannel;
    private MelodyChannel melodyChannel;
    private Tempo tempo;
    private String dynamic;
    private Harmony key;

    /**
     * This class merges the different MIDI channels of rhythm, melody and sound effects
     * it also calculates the meta data of the music (dynamic, tempo, key)
     *
     * @param rhythmChannel MIDI channel of the rhythm section
     * @param melodyChannel MIDI channel of the melody section
     * @param dynamic       dynamic of the created music
     * @param tempo         dynamic of the created music
     */
    public Merger(RhythmChannel rhythmChannel, MelodyChannel melodyChannel, String dynamic, Tempo tempo, Harmony key) {
        this.rhythmChannel = rhythmChannel;
        this.melodyChannel = melodyChannel;
        this.dynamic = dynamic;
        this.tempo = tempo;
        this.key = key;
    }

    public Merger(Tempo tempo) {
        this.tempo = tempo;
    }

    public Sequence[] mergeAudioTracks() {
        return new Sequence[]{melodyChannel.getSequence(), rhythmChannel.getSequence()};
    }
}

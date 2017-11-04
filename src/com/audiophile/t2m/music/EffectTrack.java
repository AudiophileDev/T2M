package com.audiophile.t2m.music;

import com.audiophile.t2m.io.FileUtils;
import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.text.Word;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import java.util.HashMap;

import static com.audiophile.t2m.music.MidiUtils.*;

/**
 * @author Simon
 * Created on 03.11.2017.
 */
public class EffectTrack implements TrackGenerator {

    private HashMap<String, Float> effects;
    private Tempo tempo;
//TODO stop effecting from overlaying each other
    EffectTrack(Sentence[] sentences, Tempo tempo) {
        this.tempo = tempo;
        effects = new HashMap<>();
        int index = 0;
        for (Sentence s : sentences)
            for (Word w : s.getWords()) {
                if (w.getEntry() != null && w.getEntry().getEffect() != null) {
                    effects.put(w.getEntry().getEffect(), (float) index);
                }
                index++;
            }
        for (String s : effects.keySet()) {
            effects.put(s, effects.get(s) / index);
            System.out.println(s + ": " + effects.get(s));
        }
    }

    @Override
    public void writeToTrack(Track track, int channel) {
        for (String name : effects.keySet()) {
            Sequence sequence = FileUtils.LoadMidiFile("effects\\" + name.trim() + ".mid");
            if (sequence != null) {
                int start =
                        // Position effect in track
                        (int) (QUARTER * tempo.averageBpm / 60.0 *  //beats per second
                                15 * //because 15 seconds
                                effects.get(name)  //i-th word in text
                        );
                if (sequence.getMicrosecondLength() / 1000000.0 + TicksInSecs(start, tempo.resolution) > 15.0) {
                    start -= SecsInTicks(TicksInSecs(start, tempo.resolution) + sequence.getMicrosecondLength() / 1000000.0 - 15, tempo.resolution);
                }

                float scale = tempo.resolution / (float) sequence.getResolution(); // Make the tempo fit
                for (Track t : sequence.getTracks()) {
                    for (int i = 0; i < t.size(); i++) {
                        MidiEvent event = t.get(i);
                        byte[] data = event.getMessage().getMessage();//(command & 0xF0) | (channel & 0x0F)
                        data[0] += 1; // Keep channel 1 free
                        MidiEvent ev = new MidiEvent(new MidiMessage(data) {
                            @Override
                            public Object clone() {
                                return null;
                            }
                        }, (long) (event.getTick() * scale) + start);
                        track.add(ev);
                    }
                }
            }
        }
    }
}

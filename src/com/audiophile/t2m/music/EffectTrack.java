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
        final int i = index;
        effects.forEach((k, v) -> effects.put(k, v / (float) i));
    }

    @Override
    public void writeToTrack(Track track, int channel) {
        long lastEnd = 0;
        for (String name : effects.keySet()) {
            Sequence sequence = FileUtils.LoadMidiFile("effects\\" + name.trim() + ".mid");
            if (sequence != null) {
                int start =
                        // Position effect in track
                        (int) (QUARTER * tempo.averageBpm / 60.0 *  //beats per second
                                15 * //because 15 seconds
                                effects.get(name)  //i-th word in text
                        );
                if (start < lastEnd)
                    start = (int) lastEnd;
                if (sequence.getMicrosecondLength() / 1000000.0 + TicksInSecs(start, tempo.resolution) > 15.0) {
                    start -= SecsInTicks(TicksInSecs(start, tempo.resolution) + sequence.getMicrosecondLength() / 1000000.0 - 15, tempo.resolution);
                }

                float scale = tempo.resolution / (float) sequence.getResolution(); // Make the tempo fit
                for (Track t : sequence.getTracks()) {
                    for (int i = 0; i < t.size(); i++) {
                        MidiEvent event = t.get(i);
                        byte[] data = event.getMessage().getMessage();//(command & 0xF0) | (channel & 0x0F)
                        data[0] += 2; // Keep channel 1 and 2 free
                        long tick = (long) (event.getTick() * scale) + start;
                        MidiEvent ev = new MidiEvent(new MidiMessage(data) {
                            @Override
                            public Object clone() {
                                return null;
                            }
                        }, tick);
                        track.add(ev);
                        if (tick > lastEnd)
                            lastEnd = tick;
                    }
                }
            }
        }
    }
}

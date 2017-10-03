package com.audiophile.t2m.writer;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;

/**
 * Interface for all classes that convert midi data to music format
 */
public class MusicWriter {

    public static final String MP3 = "mp3", WAV = "wav", PLAY = "play", MIDI = "midi";

    public static void writeMidi(Sequence sequence, String fileName) throws IOException {
        int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
        if (allowedTypes.length == 0) {
            System.err.println("No supported MIDI file types.");
        } else {
            MidiSystem.write(sequence, allowedTypes[0], new File(fileName));
        }
    }

    public static void writeMP3(Sequence sequence, String fileName) throws IOException {
        //TODO wav to java
    }

    public static void writeWav(Sequence sequence, String fileName) throws IOException {
        try {
            MidiToWavRenderer renderer = new MidiToWavRenderer();
            renderer.createWavFile(sequence, new File(fileName));
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private static final int END_OF_TRACK = 47;

    public synchronized static void play(Sequence sequence) {
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());

            // Specify the sequence to play, and the tempo to play it at
            sequencer.setSequence(sequence);
            //sequencer.setTempoInBPM(120);

            // Use Semaphore to block thread while playing
            Semaphore s = new Semaphore(0);

            // Let us know when it is done playing
            sequencer.addMetaEventListener(m -> {
                // A message of this type is automatically sent
                // when we reach the end of the track
                if (m.getType() == END_OF_TRACK) {
                    //avoid cut off ending
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }

                    sequencer.stop();
                    sequencer.close();
                    synthesizer.close();
                    s.release();
                }
            });
            // And start playing now.
            sequencer.start();

            // Block thread while playing
            s.acquire();
        } catch (MidiUnavailableException | InvalidMidiDataException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

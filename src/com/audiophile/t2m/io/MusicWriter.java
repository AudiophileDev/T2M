package com.audiophile.t2m.io;

import it.sauronsoftware.jave.*;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;

/**
 * Interface for all classes that convert midi data to music format
 */
public class MusicWriter {

    /**
     * All available output methods
     */
    public static final String MP3 = "mp3", WAV = "wav", PLAY = "play", MIDI = "midi";

    /**
     * Writes a given sequence to a midi file.
     *
     * @param sequence The sequence to write to the file
     * @param fileName The file to write to
     * @throws IOException Thrown if writing to file was not possible
     */
    public static void writeMidi(Sequence sequence, String fileName) throws IOException {
        int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
        if (allowedTypes.length == 0) {
            System.err.println("No supported MIDI file types.");
        } else {
            MidiSystem.write(sequence, allowedTypes[0], new File(fileName));
        }
    }

    /**
     * Converts the given <code>sequence</code> to a wav file and then to a mp3 file.
     * The temporary wav file is deleted afterwards.
     *
     * @param sequence The sequence to write to the file
     * @param fileName The file to write to
     * @throws IOException Thrown if writing to file was not possible
     * @see MusicWriter#writeWav(Sequence, String)
     */
    public static void writeMP3(Sequence sequence, String fileName) throws IOException {
        String file = fileNameWithoutEnding(fileName);
        File wav = new File(file + System.currentTimeMillis() + ".wav");
        File mp3 = new File(file + ".mp3");
        if (!mp3.createNewFile())
            return;
        writeWav(sequence, wav.getPath());

        //Convert wav to mp3
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(64000);
        audio.setChannels(1);
        audio.setSamplingRate(22050);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(wav, mp3, attrs);
            if (!fileName.equals(mp3.getName()))
                System.out.println("Wrote mp3 to \"" + mp3.getPath() + "\"");
        } catch (EncoderException e) {
            e.printStackTrace();
        } finally {
            if (!wav.delete())
                System.err.println("Could not delete temporary wav file");
        }
    }

    /**
     * Removes the extension from a filename
     *
     * @param fileName The filename with extension
     * @return The filename without extension
     */
    private static String fileNameWithoutEnding(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index > 0 && index + 1 < fileName.length())
            return fileName.substring(0, index);
        else return fileName;
    }


    /**
     * Saves the given <code>sequence</code> to a wav file.
     *
     * @param sequence The sequence to write to the file
     * @param fileName The file to write to
     * @throws IOException Thrown if writing to file was not possible
     * @see MidiToWavRenderer
     */
    public static void writeWav(Sequence sequence, String fileName) throws IOException {
        try {
            MidiToWavRenderer renderer = new MidiToWavRenderer();
            File wavFile = new File(fileName);
            renderer.createWavFile(sequence, wavFile);
            String run = "open";
            if (System.getProperty("os.name").startsWith("Windows"))
                run = "cmd /c start";
            Runtime.getRuntime().exec(run +" "+ wavFile); //TODO remove
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private static final int END_OF_TRACK = 47;

    /**
     * Plays given sequence direct from the console. For testing only.
     * Blocks thread while playing.
     *
     * @param sequence The sequence to play
     */
    public synchronized static void play(Sequence sequence) {
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());

            // Specify the sequence to play, and the tempo to play it at
            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(128);

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

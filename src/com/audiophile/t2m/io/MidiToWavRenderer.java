package com.audiophile.t2m.io;

import com.sun.media.sound.AudioSynthesizer;

import javax.sound.midi.*;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Uses a modified version of a file of the <a
 * href="http://www.jfugue.org/download.html">JFugue</a> API for Music
 * Programming. More precisely the <a
 * href="http://www.jfugue.org/code/Midi2WavRenderer.java"
 * >Midi2WavRenderer.java</a> file.
 * </p>
 *
 * @author Karl Helgason
 * @author David Koelle
 * @author Joren Six
 * @author Simon Niedermayr
 */
class MidiToWavRenderer {
    /**
     * The synthesizer used to render the audio.
     */
    private final transient AudioSynthesizer synth;

    MidiToWavRenderer() throws MidiUnavailableException, InvalidMidiDataException, IOException {
        try {
            synth = (AudioSynthesizer) MidiSystem.getSynthesizer();
        } catch (ClassCastException e) {
            throw new Error("Please make sure Gervill is included in the classpath: "
                    + "it should be de default synth. These are the currently installed synths: "
                    + Arrays.toString(MidiSystem.getMidiDeviceInfo()), e);
        }
    }

    /**
     * Creates a WAV file based on the Sequence, using the default soundbank.
     *
     * @param sequence   The sequence to write to the file
     * @param outputFile The file to write to
     * @throws MidiUnavailableException No Midi system available
     * @throws InvalidMidiDataException Invalid data
     * @throws IOException              Could not write to file
     */
    void createWavFile(final Sequence sequence, final File outputFile)
            throws MidiUnavailableException, InvalidMidiDataException, IOException {
        /*
         * Open synthesizer in pull mode in the format 96000hz 24 bit stereo
		 * using Sinc interpolation for highest quality. With 1024 as max
		 * polyphony.
		 */
        long start = System.currentTimeMillis();
        final AudioFormat format = new AudioFormat(44000, 24, 2, true, false);
        final Map<String, Object> map = new HashMap<>();
        map.put("interpolation", "sinc");
        map.put("max polyphony", "1024");
        AudioInputStream stream = synth.openStream(format, map);

        // Play Sequence into AudioSynthesizer Receiver.
        final double total = send(sequence, synth.getReceiver());

        // Calculate how long the WAVE file needs to be.
        final long len = (long) (stream.getFormat().getFrameRate() * (total + 1)); // Add extra second for smooth ending
        stream = new AudioInputStream(stream, stream.getFormat(), len);

        // Write WAVE file to disk.
        AudioSystem.write(stream, AudioFileFormat.Type.WAVE, outputFile);
        this.synth.close();
        long end = System.currentTimeMillis();
        System.out.println("Writing wav file took: " + (end - start) + "ms");
    }

    /**
     * Send entry MIDI Sequence into Receiver using time stamps.
     *
     * @return The total length of the sequence.
     */
    private double send(final Sequence seq, final Receiver recv) {
        assert seq.getDivisionType() == Sequence.PPQ;

        final float divtype = seq.getDivisionType();
        final Track[] tracks = seq.getTracks();

        final int[] trackspos = new int[tracks.length];
        int mpq = 500000;
        final int seqres = seq.getResolution();
        long lasttick = 0;
        long curtime = 0;
        while (true) {
            MidiEvent selevent = null;
            int seltrack = -1;
            for (int i = 0; i < tracks.length; i++) {
                final int trackpos = trackspos[i];
                final Track track = tracks[i];
                if (trackpos < track.size()) {
                    final MidiEvent event = track.get(trackpos);
                    if (selevent == null || event.getTick() < selevent.getTick()) {
                        selevent = event;
                        seltrack = i;
                    }
                }
            }
            if (seltrack == -1) {
                break;
            }
            trackspos[seltrack]++;
            final long tick = selevent.getTick();
            if (divtype == Sequence.PPQ) {
                curtime += (tick - lasttick) * mpq / seqres;
            } else {
                curtime = (long) (tick * 1000000.0 * divtype / seqres);
            }
            lasttick = tick;
            final MidiMessage msg = selevent.getMessage();
            if (msg instanceof MetaMessage) {
                if (divtype == Sequence.PPQ && ((MetaMessage) msg).getType() == 0x51) {
                    final byte[] data = ((MetaMessage) msg).getData();
                    mpq = (data[0] & 0xff) << 16 | (data[1] & 0xff) << 8 | data[2] & 0xff;
                }
            } else if (recv != null) {
                recv.send(msg, curtime);
            }
        }
        return curtime / 1000000.0;
    }
}
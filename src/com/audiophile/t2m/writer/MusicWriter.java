package com.audiophile.t2m.writer;

import javax.sound.midi.Sequence;
import java.io.IOException;

/**
 * Interface for all classes that convert midi data to music format
 */
public interface MusicWriter{
    void writeFile(Sequence channel, String filename) throws IOException;
}

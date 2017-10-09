package com.audiophile.t2m.music;

import javax.sound.midi.Track;

/**
 * @author Simon
 */
public interface TrackGenerator {
    void writeToTrack(Track track);
}

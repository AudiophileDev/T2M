package com.audiophile.t2m.music;

import javax.sound.midi.Track;

/**
 * @author Simon
 * Created on 02.10.2017.
 */
public interface TrackGenerator {
    void writeToTrack(Track track);
}

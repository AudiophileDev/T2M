package com.audiophile.t2m.music;

import javax.sound.midi.Track;

/**
 * Interface for all music generation classes
 * @author Simon
 */
public interface TrackGenerator {
    /**
     * The music should be written to the given track on the given channel
     * @param track The track to write to
     * @param channel The channel to write to
     */
    void writeToTrack(Track track, int channel);
}

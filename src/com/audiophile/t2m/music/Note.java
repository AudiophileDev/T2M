package com.audiophile.t2m.music;

import javax.sound.midi.Track;

public class Note {

    private Track track;
    private int startTick;
    private int length;
    private int key;
    private int vel;
    private int channel;

    public Note(Track track, int startTick, int length, int key, int vel, int channel) {
        this.track = track;
        this.startTick = startTick;
        this.length = length;
        this.key = key;
        this.vel = vel;
        this.channel = channel;
    }

    public Track getTrack() {
        return track;
    }

    public int getStartTick() {
        return startTick;
    }

    public int getLength() {
        return length;
    }

    public int getKey() {
        return key;
    }

    public int getVel() {
        return vel;
    }

    public int getChannel() {
        return channel;
    }
}

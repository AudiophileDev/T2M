package com.audiophile.t2m.music;

public enum MyInstrument {
    Piano("p"), Trumpet("trp"), AltSax("aSax"), TenorSax("tSax"), SopranSax("sSax"), BaritoneSax("bSax"), Trombone("trb"), Drums("dr");

    final int MidiValue;

    MyInstrument(String instrumentClass) {
        switch (instrumentClass) {
            case "p":
                MidiValue = 1;
                break;
            case "trp":
                MidiValue = 57;
                break;
            case "trb":
                MidiValue = 58;
                break;
            case "sSax":
                MidiValue = 65;
                break;
            case "aSax":
                MidiValue = 66;
                break;
            case "tSax":
                MidiValue = 67;
                break;
            case "bSax":
                MidiValue = 68;
                break;
            case "dr":
                MidiValue = 1;
                break;
            default:
                MidiValue = 1;
        }
    }
}

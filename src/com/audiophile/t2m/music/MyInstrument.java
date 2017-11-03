package com.audiophile.t2m.music;

//TODO documentation
public enum MyInstrument {
    Piano(1, "p"),
    Trumpet(57, "trp"),
    AltSax(66, "aSax"),
    TenorSax(67, "tSax"),
    SopranSax(65, "sSax"),
    BaritoneSax(68, "bSax"),
    Trombone(58, "trb"),
    Drums(1, "dr");

    final int midiValue;
    final String instrumentClass;

    MyInstrument(int midiValue, String instrumentClass) {
        this.midiValue = midiValue;
        this.instrumentClass = instrumentClass;
    }

    public static String[] stringValues() {
        MyInstrument[] instruments = MyInstrument.values();
        String[] values = new String[instruments.length];
        for(int i=0;i<values.length;i++)
            values[i] = instruments[i].getInstrumentClass();
        return values;
    }

    public int getMidiValue() {
        return midiValue;
    }

    public String getInstrumentClass() {
        return instrumentClass;
    }
}

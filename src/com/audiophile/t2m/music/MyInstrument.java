package com.audiophile.t2m.music;

//TODO documentation
public enum MyInstrument {
   // add instrument here first..
    Piano(1, "p"),

    AltoSax(66, "aSax"),
    TenorSax(67, "tSax"),
    SopranoSax(65, "sSax"),
    BaritoneSax(68, "bSax"),

    Trumpet(57, "trp"),
    Trombone(58, "trb"),
    Horn(61,"c"),
    Tuba(59,"tuba"),

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

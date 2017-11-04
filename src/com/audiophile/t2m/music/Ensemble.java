package com.audiophile.t2m.music;

public enum Ensemble {
    //..then add ensemble of instruments here
    Brass("brass", MyInstrument.Trumpet, MyInstrument.Trombone, MyInstrument.Horn, MyInstrument.Tuba),
    Saxs("saxs", MyInstrument.SopranoSax, MyInstrument.AltoSax, MyInstrument.TenorSax, MyInstrument.BaritoneSax),
    Piano("piano", MyInstrument.Piano,MyInstrument.Piano,MyInstrument.Piano,MyInstrument.Piano); //mehrstimmiges klavier
    //Strings("strings")
    //and so on...

    String instrumentClass;
    MyInstrument[] instruments;

    Ensemble(String instrumentClass, MyInstrument... instruments) {
        this.instrumentClass = instrumentClass;
        this.instruments = instruments;
    }
    }

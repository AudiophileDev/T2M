package com.audiophile.t2m.music;

public enum Ensemble {
    //..then add ensemble of instruments here
    Brass("brass", MyInstrument.Trumpet, MyInstrument.Trombone, MyInstrument.Horn, MyInstrument.Tuba),
    Saxs("saxs", MyInstrument.SopranoSax, MyInstrument.AltoSax, MyInstrument.TenorSax, MyInstrument.BaritoneSax),
    Piano("piano", MyInstrument.Piano,MyInstrument.Piano,MyInstrument.Piano,MyInstrument.Piano), //mehrstimmiges klavier

    Strings("strings", MyInstrument.Violin, MyInstrument.Violin, MyInstrument.Cello, MyInstrument.Contrabass, MyInstrument.Harp),
    AcousticStrings("accStrings", MyInstrument.AcousticGuitar, MyInstrument.Violin, MyInstrument.Cello),
    Woodwinds("woodwinds", MyInstrument.Flute, MyInstrument.EngHorn, MyInstrument.Clarinet, MyInstrument.Basson ),
    ModernWoodwinds("mwoodwinds", MyInstrument.Oboe, MyInstrument.Flute, MyInstrument.Clarinet, MyInstrument.Basson),
    Percussion("percussion", MyInstrument.Timpani, MyInstrument.Timpani, MyInstrument.Cymbals),
    Keyboards ("keyboards", MyInstrument.AcousticPiano, MyInstrument.AcousticPiano, MyInstrument.Celesta, MyInstrument.DrawbarOrgan);
    //and so on...

    String instrumentClass;
    MyInstrument[] instruments;

    Ensemble(String instrumentClass, MyInstrument... instruments) {
        this.instrumentClass = instrumentClass;
        this.instruments = instruments;
    }
    }

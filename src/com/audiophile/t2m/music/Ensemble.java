package com.audiophile.t2m.music;

public enum Ensemble {
    //..then add ensemble of instruments here
    Brass("brass", MyInstrument.Trumpet, MyInstrument.Trombone, MyInstrument.Horn, MyInstrument.Tuba),
    Saxs("saxs", MyInstrument.SopranoSax, MyInstrument.AltoSax, MyInstrument.TenorSax, MyInstrument.BaritoneSax),
    Piano("piano", MyInstrument.Piano,MyInstrument.Piano,MyInstrument.Piano,MyInstrument.Piano), //mehrstimmiges klavier

    Strings("strings", MyInstrument.Violin, MyInstrument.Violin, MyInstrument.Cello, MyInstrument.Contrabass, MyInstrument.Harp),
    AcousticStrings("accStrings",  MyInstrument.Cello, MyInstrument.AcousticGuitar,  MyInstrument.Violin),
    Woodwinds("woodwinds", MyInstrument.Flute, MyInstrument.EngHorn, MyInstrument.Clarinet, MyInstrument.Basson),
    ModernWoodwinds("mwoodwinds", MyInstrument.Flute,  MyInstrument.Basson ,MyInstrument.Oboe, MyInstrument.Clarinet),
    Percussion("percussion", MyInstrument.Timpani, MyInstrument.Timpani, MyInstrument.Cymbals),
    Instrumentals("Instrumental",MyInstrument.AcousticGuitar, MyInstrument.Celesta, MyInstrument.AcousticPiano, MyInstrument.AcousticPiano),
    Keyboards("keyboards", MyInstrument.ReedOrgan, MyInstrument.ReedOrgan, MyInstrument.DrawbarOrgan),
    BirdTweet("birdtweet", MyInstrument.BirdTweet, MyInstrument.BirdTweet, MyInstrument.BirdTweet);
   //and so on...

    String instrumentClass;
    MyInstrument[] instruments;

    Ensemble(String instrumentClass, MyInstrument... instruments) {
        this.instrumentClass = instrumentClass;
        this.instruments = instruments;
    }
    }

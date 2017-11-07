package com.audiophile.t2m.music;

public enum Ensemble {
    //..then add ensemble of instruments here
    Brass("brass", MyInstrument.Trumpet, MyInstrument.Trombone, MyInstrument.Horn, MyInstrument.Tuba),
    Saxs("saxs", MyInstrument.SopranoSax, MyInstrument.AltoSax, MyInstrument.TenorSax, MyInstrument.BaritoneSax),
    Piano("piano", MyInstrument.Piano, MyInstrument.Piano, MyInstrument.Piano, MyInstrument.Piano), //mehrstimmiges klavier

    Strings("strings", MyInstrument.Violin, MyInstrument.Violin, MyInstrument.Cello, MyInstrument.Contrabass, MyInstrument.Harp),
    AcousticStrings("accStrings", MyInstrument.Cello, MyInstrument.AcousticGuitar, MyInstrument.Violin),
    Woodwinds("woodwinds", MyInstrument.Flute, MyInstrument.EngHorn, MyInstrument.Clarinet, MyInstrument.Basson),
    ModernWoodwinds("mwoodwinds", MyInstrument.Flute, MyInstrument.Basson, MyInstrument.Oboe, MyInstrument.Clarinet),
    Percussion("percussion", MyInstrument.Timpani, MyInstrument.Timpani, MyInstrument.Cymbals),
    Instrumentals("Instrumental", MyInstrument.AcousticGuitar, MyInstrument.Celesta, MyInstrument.AcousticPiano, MyInstrument.AcousticPiano),
    Keyboards("keyboards", MyInstrument.ReedOrgan, MyInstrument.ReedOrgan, MyInstrument.DrawbarOrgan),
    BirdTweet("birdtweet", MyInstrument.BirdTweet, MyInstrument.BirdTweet, MyInstrument.BirdTweet);
    //and so on...

    String instrumentClass;
    MyInstrument[] instruments;

    Ensemble(String instrumentClass, MyInstrument... instruments) {
        this.instrumentClass = instrumentClass;
        this.instruments = instruments;
    }

    /**
     * Maps the short names to the ensembles
     *
     * @param key      The short namegithub
     * @param fallback the fallback if no ensemble with the given short name exists
     * @return An corresponding ensemble
     */
    public static Ensemble map(String key, Ensemble fallback) {
        for (Ensemble e : Ensemble.values())
            if (e.instrumentClass.equals(key))
                return e;
        return fallback;
    }

    /**
     * Returns all available ensemble as array
     *
     * @return The short name for every ensemble
     */
    public static String[] stringValues() {
        Ensemble[] ensembles = Ensemble.values();
        String[] values = new String[ensembles.length];
        for (int i = 0; i < values.length; i++)
            values[i] = ensembles[i].instrumentClass;
        return values;
    }

}

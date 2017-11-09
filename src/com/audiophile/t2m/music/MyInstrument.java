package com.audiophile.t2m.music;

/**
 * The Midi instruments mapped to enums
 */
public enum MyInstrument {
    // add instrument here first..
    Piano(1, "p"),
    AcousticPiano(2, "acpn"),
    Celesta(9, "clt"),
    DrawbarOrgan(17, "drborg"),
    ReedOrgan(20, "rorgn"),

    Accordion(22, "acc"),
    AcousticGuitar(25, "accgtr"),

    AltoSax(66, "aSax"),
    TenorSax(67, "tSax"),
    SopranoSax(65, "sSax"),
    BaritoneSax(68, "bSax"),

    Trumpet(57, "trp"),
    Trombone(58, "trb"),
    Horn(61, "c"),
    Tuba(59, "tuba"),

    Violin(41, "vln"),
    Viola(42, "vla"),
    Cello(43, "cl"),
    Contrabass(44, "ctbs"),
    Harp(47, "hrp"),

    Flute(74, "flt"),
    EngHorn(70, "ehrn"),
    Clarinet(72, "clrt"),
    Basson(71, "bsn"),

    Oboe(69, "ob"),

    Timpani(48, "tmpn"),
    Cymbals(120, "cmbl"),

    Drums(117, "dr"),
    BirdTweet(123, "brt");

    /**
     * The id of the instrument in midi
     */
    final int midiValue;
    /**
     * The short name of the instrument
     */
    final String instrumentClass;

    /**
     * Creates a new instrument with a short name the the corresponding midi id
     * @param midiValue The midi id of the instrument
     * @param instrumentClass The short name if the instrument
     */
    MyInstrument(int midiValue, String instrumentClass) {
        this.midiValue = midiValue;
        this.instrumentClass = instrumentClass;
    }
}

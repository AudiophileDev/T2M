package com.audiophile.t2m.music;

public enum Mode {
    Major(4), Minor(3);
    final int third;

    Mode(int third) {
        this.third = third;
    }

}

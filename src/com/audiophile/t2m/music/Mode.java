package com.audiophile.t2m.music;

public enum Mode {
    /**
     * Major key with a big third represented in the {@link Mode#third}
     */
    Major(4),
    /**
     * Major key with a small third represented in the {@link Mode#third}
     */
    Minor(3);

    /**
     * The halftone step value of the third.
     */
    final int third;

    Mode(int third) {
        this.third = third;
    }
}

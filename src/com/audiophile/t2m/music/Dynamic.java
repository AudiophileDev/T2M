package com.audiophile.t2m.music;

class Dynamic {

    int[] dynamicGradient;
    int initDynamic;

    public Dynamic(int initDynamic, float[] avgWordLength) {
        this.initDynamic = initDynamic;
        this.dynamicGradient = makeMusicallyExpression(getExtrema(avgWordLength));
    }

    /**
     * The extrema of the average word length are calculated. They will be later used for getting the dynamic to change.
     *
     * @param avgWordLength The average word length of the input text
     * @return Returns at maximum 5 extrema in the word length averages
     */
    private float[] getExtrema(float[] avgWordLength) {
        float[] extrema = new float[5];
        int j = 0;
        float pref = 0, next = 0;
        for (int i = 0; i < avgWordLength.length; i++) {
            avgWordLength[i] = Math.round(avgWordLength[i] * 100.0) / 100.0F;
        }
        for (int i = 0; i < avgWordLength.length; i++) {
            if (i > 0) pref = avgWordLength[i - 1];
            if (i < avgWordLength.length - 1) next = avgWordLength[i + 1];
            if (((avgWordLength[i] <= pref && avgWordLength[i] < next) || (avgWordLength[i] >= pref && avgWordLength[i] > next))) {
                extrema[j++] = avgWordLength[i];
            }
            if (j > extrema.length - 1) return extrema;
        }
        // System.out.println(Arrays.toString(extrema));
        return extrema;
    }

    /**
     * indicates how much the dynamic will in- or decrease
     *
     * @param extrema calculated extremae from above
     * @return an array of values, which determine, if the music gets louder or more silent
     */
    private int[] makeMusicallyExpression(float[] extrema) {
        int dif, j = 0;
        int[] musicalGradient = new int[extrema.length - 1];
        for (int i = 0; i < musicalGradient.length; i++) {
            dif = Math.round((extrema[i + 1] - extrema[i]) * 10);
            if (extrema[i + 1] == 0)
                dif = 0;
            musicalGradient[j] = dif;
            j++;
        }
        //System.out.println(Arrays.toString(extrema));
        //System.out.println(Arrays.toString(musicalGradient));
        return musicalGradient;
    }


    /**
     * checks if input dynamic is valid and properly heard (>20)
     *
     * @param dynamic dynamic to check
     * @return boolean if dynamic is proper to use
     */
    static boolean isValidDynamic(int dynamic) {
        return dynamic < 127 && dynamic > 20;
    }
}

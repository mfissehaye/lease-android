package ahadoo.com.collect.util;

import android.text.TextUtils;

public class SurveyNumberRange {

    public float from;

    public float to;

    public SurveyNumberRange(String range) {

        if(range == null) range = "";

        String[] components = TextUtils.split(range, "-");

        try {

            from = Float.parseFloat(components[0]);

            to = Float.parseFloat(components[1]);

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {

            from = 0;

            to = 0;
        }
    }

    @Override
    public String toString() {

        return from + "-" + to;
    }
}

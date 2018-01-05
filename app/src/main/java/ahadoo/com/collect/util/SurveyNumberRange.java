package ahadoo.com.collect.util;

import android.text.TextUtils;

public class SurveyNumberRange {

    public double from;

    public double to;

    public SurveyNumberRange(String range) {

        if(range == null) range = "";

        String[] components = TextUtils.split(range, "-");

        try {

            from = Double.parseDouble(components[0]);

            to = Double.parseDouble(components[1]);

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

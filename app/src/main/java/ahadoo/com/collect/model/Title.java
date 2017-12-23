package ahadoo.com.collect.model;

import com.google.gson.annotations.SerializedName;

import ahadoo.com.collect.util.SurveyLanguage;

public class Title {

    public Long id;

    public static SurveyLanguage currentLanguage = SurveyLanguage.ENGLISH;

    @SerializedName("en")
    public
    String en;

    @SerializedName("am")
    String am;

    @SerializedName("tig")
    String tig;

    @SerializedName("or")
    String or;

    @Override
    public String toString() {

        switch (currentLanguage) {

            case ENGLISH:

                return en;

            case AMHARIC:

                return am;

            case TIGRIGNA:

                return tig;

            case OROMIFFA:

                return or;
        }

        return en;
    }
}

package ahadoo.com.collect.model;

import com.google.gson.annotations.SerializedName;

import ahadoo.com.collect.util.SurveyLanguage;

public class Title {

    public Long id;

    public static SurveyLanguage currentLanguage = SurveyLanguage.ENGLISH;

    public Title(String title) {
        this.en = title;
        this.am = "";
        this.tig = "";
        this.or = "";
    }

    @SerializedName("en")
    public
    String en;

    @SerializedName("am")
    String am;

    @SerializedName("tg")
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

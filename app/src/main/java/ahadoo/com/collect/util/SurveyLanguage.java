package ahadoo.com.collect.util;

import com.google.gson.annotations.SerializedName;

public enum SurveyLanguage {

    @SerializedName("en")
    ENGLISH("en"),

    @SerializedName("am")
    AMHARIC("am"),

    @SerializedName("tg")
    TIGRIGNA("tg"),

    @SerializedName("or")
    OROMIFFA("or");

    private String code;

    private SurveyLanguage(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static SurveyLanguage fromCode(String code) {
        switch (code) {
            case "en":
                return ENGLISH;
            case "am":
                return AMHARIC;
            case "tg":
                return TIGRIGNA;
            case "or":
                return OROMIFFA;
        }

        return ENGLISH;
    }
}

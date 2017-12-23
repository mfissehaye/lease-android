package ahadoo.com.collect.util;

import com.google.gson.annotations.SerializedName;

public enum QuestionType {
    @SerializedName("number")
    NUMBER,

    @SerializedName("choose-any")
    CHOOSE_ANY ,

    @SerializedName("choose-one")
    CHOOSE_ONE,

    @SerializedName("text")
    TEXT,

    @SerializedName("location")
    LOCATION,

    @SerializedName("currency")
    CURRENCY,

    @SerializedName("image")
    IMAGE,

    @SerializedName("date")
    DATE,

    @SerializedName("number-range")
    NUMBER_RANGE;

    public boolean eq(String value1, String value2) {

        if(value1 == null || value2 == null) return false;

        switch(this) {

            case NUMBER:

                return Double.parseDouble(value1) == Double.parseDouble(value2);

            default:

                return value1.equals(value2);
        }
    }

    public boolean gt(String value1, String value2) {
        return Double.parseDouble(value1) > Double.parseDouble(value2);
    }

    public boolean ge(String value1, String value2) {
        return Double.parseDouble(value1) >= Double.parseDouble(value2);
    }

    public boolean lt(String value1, String value2) {
        return Double.parseDouble(value1) < Double.parseDouble(value2);
    }

    public boolean le(String value1, String value2) {
        return Double.parseDouble(value1) < Double.parseDouble(value2);
    }
}

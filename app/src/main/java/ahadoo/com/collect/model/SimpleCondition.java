package ahadoo.com.collect.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import ahadoo.com.collect.util.CollectDatabaseHelpers;

class SimpleCondition {

    @SerializedName("operator")
    String operator;

    @SerializedName("question")
    String questionUUID;

    @SerializedName("value")
    String value;

    public boolean resolve(Context context) {

        SurveyQuestion question = CollectDatabaseHelpers.getQuestionByUUID(context, questionUUID, null, null);

        switch(operator) {

            case "==":

                return question.questionType.eq(question.response, value);

            case ">":

                return question.questionType.gt(question.response, value);
        }

        return true;
    }
}

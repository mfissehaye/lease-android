package ahadoo.com.collect.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ahadoo.com.collect.util.QuestionType;
import ahadoo.com.collect.util.SurveyNumberRange;

public class SurveyQuestion {

    @SerializedName("uuid")
    public
    String uuid;

    @SerializedName("end")
    int end;

    @SerializedName("required")
    public
    boolean required;

    @SerializedName("start")
    int start;

    @SerializedName("index")
    public int index;

    @SerializedName("title")
    public Title text;

    @SerializedName("survey")
    public String surveyUUID;

    @SerializedName("type")
    public QuestionType questionType;

    @SerializedName("group")
    public String groupUUID;

    @SerializedName("choices")
    public List<SurveyChoice> choices;

    @SerializedName("condition")
    public AggregateCondition aggregateCondition;

    public boolean visible;

    // this is UUID for question types of CHOOSE_ANY and CHOOSE_ONE
    // it's plain text for other question types
    public String response;

    private boolean isChoice() {

        return questionType == QuestionType.CHOOSE_ANY ||

                questionType == QuestionType.CHOOSE_ONE;
    }

    public boolean validate() {

        if (isChoice()) {

            for (SurveyChoice choice : choices) {

                if(choice.uuid.equals(response)) return true;
            }

        } else if(questionType == QuestionType.NUMBER_RANGE) {

            SurveyNumberRange range = new SurveyNumberRange(response);

            return range.to != 0 || range.from != 0;
        }

        return !this.required || (this.response != null && !this.response.isEmpty());

        /*return ((isChoice() && !choices.contains(response))this.response.isEmpty() || this.attemptedReplyValue.isEmpty()) &&

                this.required;*/
    }

    public void resetResponse() {

        this.response = null;
    }
}

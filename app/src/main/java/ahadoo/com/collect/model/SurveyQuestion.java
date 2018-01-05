package ahadoo.com.collect.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ahadoo.com.collect.util.QuestionType;
import ahadoo.com.collect.util.SurveyNumberRange;

public class SurveyQuestion {

    private static final Double MAX_END_OF_RANGE = 999999999.99;

    @SerializedName("uuid")
    public
    String uuid;

    @SerializedName("end")
    Double end;

    @SerializedName("required")
    public boolean required;

    @SerializedName("start")
    Double start;

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

    private String errorMessage;

    // this is UUID for question types of CHOOSE_ANY and CHOOSE_ONE
    // it's plain text for other question types
    public String response;

    private boolean isChoice() {

        return questionType == QuestionType.CHOOSE_ANY ||

                questionType == QuestionType.CHOOSE_ONE;
    }

    public boolean validate() {

        boolean valid = false;

        if (required && (response == null || response.isEmpty())) {

            errorMessage = "This question is required";
        }

        switch (questionType) {

            case CHOOSE_ONE:

                for (SurveyChoice choice : choices) {

                    if (choice.uuid.equals(response)) return true;
                }

                break;

            case NUMBER_RANGE:

                SurveyNumberRange range = new SurveyNumberRange(response);

                start = start != null ? start : 0;

                end = end != null ? end : MAX_END_OF_RANGE;

                valid = range.from >= start && range.to <= end && range.to > range.from;

                if (!valid) {

                    errorMessage = "The range has to be between " + start + " and " + end;
                }

                return valid;

            case NUMBER:

                start = start != null ? start : 0;

                end = end != null ? end : MAX_END_OF_RANGE;

                Double number;

                try {

                    number = Double.parseDouble(response);

                } catch (Exception e) {

                    e.printStackTrace();

                    return false;
                }

                valid = number >= start && number <= end;

                if (!valid) {

                    errorMessage = "The number has to be between " + start + " and " + end;
                }

                return valid;

            case TEXT:

                break;

            case CHOOSE_ANY:

                break;

            case CURRENCY:

                break;

            case DATE:

                break;

            case IMAGE:

                break;

            case LOCATION:

                break;
        }

        return !this.required || (this.response != null && !this.response.isEmpty());

        /*return ((isChoice() && !choices.contains(response))this.response.isEmpty() || this.attemptedReplyValue.isEmpty()) &&

                this.required;*/
    }

    public void resetResponse() {

        this.response = null;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}

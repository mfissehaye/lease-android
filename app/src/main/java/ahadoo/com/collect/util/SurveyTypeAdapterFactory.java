package ahadoo.com.collect.util;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ahadoo.com.collect.model.Survey;
import ahadoo.com.collect.model.SurveyQuestion;
import ahadoo.com.collect.model.Title;

public class SurveyTypeAdapterFactory extends CustomizedTypeAdapterFactory<Survey> {

    private static final String SURVEY_UUID_IDENTIFIER = "survey";
    private static final String SURVEY_LANGUAGE_IDENTIFIER = "language";
    private static final String QUESTION_UUID_IDENTIFIER = "question";
    private static final String ANSWER_IDENTIFIER = "answer";
    private static final String ANSWERS_IDENTIFIER = "answers";

    public SurveyTypeAdapterFactory() {
        super(Survey.class);
    }

    @Override
    protected void beforeWrite(Survey survey, JsonElement toSerialize) {

        JsonObject custom = toSerialize.getAsJsonObject();

        List<String> keys = new ArrayList<>();

        for(Map.Entry<String, JsonElement> entry : custom.entrySet()) {

            keys.add(entry.getKey());
        }

        for(String key : keys) {

            custom.remove(key);
        }

        custom.add(SURVEY_UUID_IDENTIFIER, new JsonPrimitive(survey.getUuid()));
        // todo: check this Title.currentLanguage may always be English

        custom.add(SURVEY_LANGUAGE_IDENTIFIER, new JsonPrimitive(Title.currentLanguage.getCode()));

        JsonArray arrayOfAnswers = new JsonArray();

        for(SurveyQuestion surveyQuestion : survey.getSurveyQuestionList()) {

            JsonObject questionResponse = new JsonObject();

            questionResponse.add(QUESTION_UUID_IDENTIFIER, new JsonPrimitive(surveyQuestion.uuid));

            switch (surveyQuestion.questionType) {

                case NUMBER:

                case TEXT:

                case CHOOSE_ONE:

                    questionResponse.add(ANSWER_IDENTIFIER, new JsonPrimitive(surveyQuestion.response));

                    break;

                case CHOOSE_ANY:

                    JsonArray choicesArray = new JsonArray();

                    for(String uuid : TextUtils.split(surveyQuestion.response, ",")) {

                        choicesArray.add(uuid);
                    }

                    questionResponse.add(ANSWER_IDENTIFIER, choicesArray);

                    break;

                case LOCATION:

                    String[] location = TextUtils.split(surveyQuestion.response, ",");

                    JsonArray locationArray = new JsonArray();

                    try {

                        locationArray.add(Double.parseDouble(location[0])); // putting the latitude

                        locationArray.add(Double.parseDouble(location[1])); // putting the longitude

                    } catch (Exception e) {

                        e.printStackTrace();

                        locationArray = new JsonArray();

                        locationArray.add(0);

                        locationArray.add(0);
                    }
            }

            arrayOfAnswers.add(questionResponse);
        }

        custom.add(ANSWERS_IDENTIFIER, arrayOfAnswers);
    }

    @Override
    protected void afterRead(JsonElement deserialized) {
        super.afterRead(deserialized);
    }
}

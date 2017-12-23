package ahadoo.com.collect.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Comparator;

import ahadoo.com.collect.model.Survey;
import ahadoo.com.collect.model.SurveyGroup;
import ahadoo.com.collect.model.SurveyQuestion;

public class SurveyDeserializer implements JsonDeserializer<Survey> {

    @Override
    public Survey deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        /*try {

            JsonObject jsonObject = json.getAsJsonObject();

            Survey survey = new Gson().fromJson(jsonObject, Survey.class);

            for (SurveyGroup group : survey.surveyGroupList) {

                for (SurveyQuestion question : survey.surveyQuestionList) {

                    if (question.groupUUID.equals(group.uuid)) {

                        group.question.add(question);
                    }
                }

                group.questions.sort(new Comparator<SurveyQuestion>() {
                    @Override
                    public int compare(SurveyQuestion o1, SurveyQuestion o2) {
                        return o1.index - o2.index;
                    }
                });
            }

            survey.surveyGroupList.sort(new Comparator<SurveyGroup>() {
                @Override
                public int compare(SurveyGroup o1, SurveyGroup o2) {
                    return o1.index - o2.index;
                }
            });

            return survey;

        } catch (IllegalStateException e) {

            return null;
        }*/

        return null;
    }
}

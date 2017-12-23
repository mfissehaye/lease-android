package ahadoo.com.collect.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;

import ahadoo.com.collect.model.SurveyQuestion;

public class SurveyQuestionListConverter implements PropertyConverter<List<SurveyQuestion>, String> {

    private Gson gson;

    public SurveyQuestionListConverter() {
        gson = new Gson();
    }

    @Override
    public List<SurveyQuestion> convertToEntityProperty(String databaseValue) {

        if(databaseValue == null) return null;

        JsonParser parser = new JsonParser();

        JsonArray surveyQuestionsJsonArray = (JsonArray) parser.parse(databaseValue);

        List<SurveyQuestion> surveyQuestions = new ArrayList<>();

        for(JsonElement element : surveyQuestionsJsonArray) {

            surveyQuestions.add(gson.fromJson(element, SurveyQuestion.class));
        }

        return surveyQuestions;
    }

    @Override
    public String convertToDatabaseValue(List<SurveyQuestion> entityProperty) {

        if(entityProperty == null) return null;

        return gson.toJson(entityProperty);
    }
}

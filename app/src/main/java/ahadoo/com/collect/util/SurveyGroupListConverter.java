package ahadoo.com.collect.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;

import ahadoo.com.collect.model.SurveyGroup;

public class SurveyGroupListConverter implements PropertyConverter<List<SurveyGroup>, String> {

    private Gson gson;

    public SurveyGroupListConverter() {
        this.gson = new Gson();
    }

    @Override
    public List<SurveyGroup> convertToEntityProperty(String databaseValue) {

        if(databaseValue == null) return null;

        JsonParser parser = new JsonParser();

        JsonArray surveyGroupJsonArray = (JsonArray) parser.parse(databaseValue);

        List<SurveyGroup> surveyGroups = new ArrayList<>();

        for(JsonElement element : surveyGroupJsonArray) {

            surveyGroups.add(gson.fromJson(element, SurveyGroup.class));
        }

        return surveyGroups;
    }

    @Override
    public String convertToDatabaseValue(List<SurveyGroup> entityProperty) {

        if(entityProperty == null) return null;

        return gson.toJson(entityProperty);
    }
}

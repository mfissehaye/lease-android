package ahadoo.com.collect.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.greenrobot.greendao.converter.PropertyConverter;

import ahadoo.com.collect.model.Title;

public class TitleConverter implements PropertyConverter<Title, String> {

    private Gson gson;

    public TitleConverter() {

        gson = new Gson();
    }

    @Override
    public Title convertToEntityProperty(String databaseValue) {

        if(databaseValue == null) return null;

        return gson.fromJson(databaseValue, Title.class);
    }

    @Override
    public String convertToDatabaseValue(Title entityProperty) {

        if(entityProperty == null) return null;

        return gson.toJson(entityProperty);
    }
}

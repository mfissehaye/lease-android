package zewd.com.learnamharic.utils;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.List;

public class GreenListConverter implements PropertyConverter<List<String>, String> {
    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        if(databaseValue == null) {
            return null;
        }
        return Arrays.asList(databaseValue.split(","));
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        if(entityProperty == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for(String link: entityProperty) {
            sb.append(link);
            sb.append(",");
        }
        return sb.toString();
    }
}

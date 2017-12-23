package ahadoo.com.collect.util;

import org.greenrobot.greendao.converter.PropertyConverter;

public class QuestionTypeConverter implements PropertyConverter<QuestionType, String> {
    @Override
    public QuestionType convertToEntityProperty(String databaseValue) {
        if(databaseValue == null) {
            return null;
        }

        return QuestionType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(QuestionType entityProperty) {
        return entityProperty == null ? null : entityProperty.name();
    }
}

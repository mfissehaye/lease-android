package ahadoo.com.collect.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AggregateCondition {

    private long id;

    // todo: change String to SurveyConditionOperator
    @SerializedName("operator")
    public String operator;

    @SerializedName("conditions")
    public List<SimpleCondition> simpleConditionList;

    public boolean resolve(Context context) {

        switch(operator) {

            case "&&":

                return simpleConditionList.size() == 0 || and(context, simpleConditionList.size() - 1);

            case "||":

                return simpleConditionList.size() == 0 || or(context, simpleConditionList.size() - 1);
        }

        return true;
    }

    private boolean and(Context context, int index) {
        if(!simpleConditionList.get(index).resolve(context) /* short circuiting */) return false;
        if(index == 0) return simpleConditionList.get(0).resolve(context);
        return and(context, --index);
    }

    private boolean or(Context context, int index) {
        if(simpleConditionList.get(index).resolve(context) /* short circuiting */) return true;
        if(index == 0) return simpleConditionList.get(0).resolve(context);
        return or(context, --index);
    }
}

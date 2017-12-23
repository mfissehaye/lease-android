package ahadoo.com.collect.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SurveyResults {

    @SerializedName("results")
    public List<Survey> surveys;
}

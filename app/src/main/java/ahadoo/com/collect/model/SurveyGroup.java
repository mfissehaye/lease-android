package ahadoo.com.collect.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SurveyGroup {

    private static int currentQuestionIndex = 0;

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("root")
    boolean root;

    @SerializedName("parent")
    String parent;

    @SerializedName("index")
    public int index;

    @SerializedName("condition")
    public AggregateCondition aggregateCondition;

    @SerializedName("questions")
    public List<String> questionUUIDs;
}

package ahadoo.com.collect.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

public class SurveyChoice {

    private String surveyQuestionId;

    private long titleId;

    @SerializedName("text")
    public Title text;

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("question")
    public String questionUUID;
}

package ahadoo.com.collect.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import ahadoo.com.collect.util.CollectDatabaseHelpers;
import ahadoo.com.collect.util.LanguagesConverter;
import ahadoo.com.collect.util.SurveyGroupListConverter;
import ahadoo.com.collect.util.SurveyLanguage;
import ahadoo.com.collect.util.SurveyQuestionListConverter;
import ahadoo.com.collect.util.TitleConverter;

@Entity
public class Survey {

    private static int currentGroupIndex = 0;

    @SerializedName("title")
    @Convert(converter = TitleConverter.class, columnType = String.class)
    public Title title;

    @SerializedName("description")
    @Convert(converter = TitleConverter.class, columnType = String.class)
    public Title description;

    @SerializedName("languages")
    @Convert(converter = LanguagesConverter.class, columnType = String.class)
    List<String> languages;

    @Id
    @SerializedName("uuid")
    String uuid;

    @SerializedName("groups")
    @Convert(converter = SurveyGroupListConverter.class, columnType = String.class)
    public List<SurveyGroup> surveyGroupList;

    @SerializedName("questions")
    @Convert(converter = SurveyQuestionListConverter.class, columnType = String.class)
    public List<SurveyQuestion> surveyQuestionList;

    public boolean submitted;

    @Transient
    public SurveyLanguage language;

    @Generated(hash = 401177111)
    public Survey(Title title, Title description, List<String> languages, String uuid,
            List<SurveyGroup> surveyGroupList, List<SurveyQuestion> surveyQuestionList,
            boolean submitted) {
        this.title = title;
        this.description = description;
        this.languages = languages;
        this.uuid = uuid;
        this.surveyGroupList = surveyGroupList;
        this.surveyQuestionList = surveyQuestionList;
        this.submitted = submitted;
    }

    @Generated(hash = 1742867551)
    public Survey() {
    }

    public Title getTitle() {
        return this.title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Title getDescription() {
        return this.description;
    }

    public void setDescription(Title description) {
        this.description = description;
    }

    public List<String> getLanguages() {
        return this.languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<SurveyGroup> getSurveyGroupList() {
        return this.surveyGroupList;
    }

    public void setSurveyGroupList(List<SurveyGroup> surveyGroupList) {
        this.surveyGroupList = surveyGroupList;
    }

    public List<SurveyQuestion> getSurveyQuestionList() {
        return this.surveyQuestionList;
    }

    public void setSurveyQuestionList(List<SurveyQuestion> surveyQuestionList) {
        this.surveyQuestionList = surveyQuestionList;
    }

    public boolean getSubmitted() {
        return this.submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public List<String> getVisibleQuestionUUIDs(Context context) {

        List<String> visibleQuestionUUIDs = new ArrayList<>();

        for(SurveyGroup group : getSurveyGroupList()) {

            // check if group should show
            if(group.aggregateCondition.resolve(context)) {

                for(String uuid : group.questionUUIDs) {

                    SurveyQuestion q = CollectDatabaseHelpers.getQuestionByUUID(context, uuid, group.uuid, uuid);

                    if(q.aggregateCondition.resolve(context)) {

                        visibleQuestionUUIDs.add(uuid);
                    }
                }
            }
        }

        return visibleQuestionUUIDs;
    }
}

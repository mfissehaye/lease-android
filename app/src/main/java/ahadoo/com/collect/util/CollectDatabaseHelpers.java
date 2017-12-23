package ahadoo.com.collect.util;

import android.content.Context;

import ahadoo.com.collect.CollectApplication;
import ahadoo.com.collect.model.Survey;
import ahadoo.com.collect.model.SurveyDao;
import ahadoo.com.collect.model.SurveyGroup;
import ahadoo.com.collect.model.SurveyQuestion;

public class CollectDatabaseHelpers {

    public static void clearDatabase(Context context) {

        CollectApplication app = (CollectApplication) context.getApplicationContext();

        if (app != null) {

            SurveyDao surveyDao = app.getSurveyDao();

            surveyDao.deleteAll();
        }
    }

    public static SurveyGroup getGroupByUUID(Context context, String groupUUID, String surveyUUID) {

        SurveyDao surveyDao = getSurveyDao(context);

        if (surveyUUID != null) {

            Survey survey = surveyDao.load(surveyUUID);

            if (survey != null) {

                for (SurveyGroup group : survey.getSurveyGroupList()) {

                    if (groupUUID.equals(group.uuid)) return group;
                }
            }
        }

        for (Survey survey : surveyDao.loadAll()) {

            for (SurveyGroup group : survey.getSurveyGroupList()) {

                if (groupUUID.equals(group.uuid)) return group;
            }
        }

        return null;
    }

    public static SurveyQuestion getQuestionByUUID(Context context, String questionUUID, String groupUUID, String surveyUUID) {

        SurveyDao surveyDao = getSurveyDao(context);

        if (surveyUUID != null) {

            Survey survey = surveyDao.load(surveyUUID);

            if (survey != null) {

                for (SurveyQuestion question : survey.getSurveyQuestionList()) {

                    if (question.uuid.equals(questionUUID)) return question;
                }
            }
        }

        for (Survey survey : surveyDao.loadAll()) {

            for (SurveyQuestion question : survey.getSurveyQuestionList()) {

                if (question.uuid.equals(questionUUID)) return question;
            }
        }

        return null;
    }

    public boolean questionInGroup(Context context, String questionUUID, String groupUUID) {

        SurveyGroup group = getGroupByUUID(context, groupUUID, null);

        return group != null && group.questionUUIDs.contains(questionUUID);
    }

    private static SurveyDao getSurveyDao(Context context) {
        return ((CollectApplication) context.getApplicationContext()).getSurveyDao();
    }
}

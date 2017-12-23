package ahadoo.com.collect;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;

import ahadoo.com.collect.model.DaoMaster;
import ahadoo.com.collect.model.DaoSession;
import ahadoo.com.collect.model.SurveyDao;

public class CollectApplication extends Application {

    public DaoSession daoSession;

    @Override
    public void onCreate() {

        super.onCreate();

        // initialize Stetho
        Stetho.initializeWithDefaults(this);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "surveys", null);

        SQLiteDatabase db = helper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(db);

        daoSession = daoMaster.newSession();
    }

    public SurveyDao getSurveyDao() {
        return daoSession.getSurveyDao();
    }
}

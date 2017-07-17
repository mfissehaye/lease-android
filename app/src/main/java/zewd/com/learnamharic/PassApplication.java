package zewd.com.learnamharic;

import android.app.Application;

import com.facebook.stetho.Stetho;

import org.greenrobot.greendao.database.Database;

import zewd.com.learnamharic.model.DaoMaster;
import zewd.com.learnamharic.model.DaoSession;

public class PassApplication extends Application {

    DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "exams-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        // initialize Stetho
        Stetho.initializeWithDefaults(this);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}

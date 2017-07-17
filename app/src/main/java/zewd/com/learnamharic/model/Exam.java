package zewd.com.learnamharic.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.DaoException;

@Entity(indexes = {@Index(value = "title")})
public class Exam {

    @Id
    @SerializedName("id")
    private long id;

    @NotNull
    @SerializedName("title")
    private String title;

    @NotNull
    @SerializedName("question_count")
    private int questionCount;

    @SerializedName("instructions")
    private String instructions;

    @SerializedName("description")
    private String description;

    @SerializedName("date_created")
    private String dateCreated;

    @SerializedName("icon_url")
    private String iconFileURL;

    @NotNull
    @SerializedName("duration")
    private long duration = 60 * 60; // 1 hour in seconds

    @SerializedName("year")
    private int yearTag;

    @ToMany(referencedJoinProperty = "examId")
    private List<Question> questions;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 973692038)
    private transient ExamDao myDao;

    @Generated(hash = 1325950310)
    public Exam(long id, @NotNull String title, int questionCount, String instructions,
            String description, String dateCreated, String iconFileURL, long duration, int yearTag) {
        this.id = id;
        this.title = title;
        this.questionCount = questionCount;
        this.instructions = instructions;
        this.description = description;
        this.dateCreated = dateCreated;
        this.iconFileURL = iconFileURL;
        this.duration = duration;
        this.yearTag = yearTag;
    }

    @Generated(hash = 945526930)
    public Exam() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getYearTag() {
        return yearTag;
    }

    public void setYearTag(int yearTag) {
        this.yearTag = yearTag;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getIconFileURL() {
        return iconFileURL;
    }

    public void setIconFileURL(String iconFileURL) {
        this.iconFileURL = iconFileURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1671924082)
    public List<Question> getQuestions() {
        if (questions == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            QuestionDao targetDao = daoSession.getQuestionDao();
            List<Question> questionsNew = targetDao._queryExam_Questions(id);
            synchronized (this) {
                if (questions == null) {
                    questions = questionsNew;
                }
            }
        }
        return questions;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1619718141)
    public synchronized void resetQuestions() {
        questions = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public void setId(long id) {
        this.id = id;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1730563422)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getExamDao() : null;
    }
}

package zewd.com.learnamharic.model;

import com.facebook.stetho.common.ReflectionUtil;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import zewd.com.learnamharic.utils.GreenListConverter;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(indexes = {@Index(value = "question")})
public class Question {

    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    @SerializedName("id")
    private long id;

    @NotNull
    @SerializedName("question")
    private String question;

    @NotNull
    @SerializedName("options")
    @Convert(converter = GreenListConverter.class, columnType = String.class)
    private List<String> options = new ArrayList<>();

    @Transient
    private boolean explained = false;

    @Transient
    private List<Integer> attemptedOptionsIndices = new ArrayList<>();

    @Transient
    @SerializedName("correct-option-indices")
    private List<Integer> correctOptionIndices = new ArrayList<>();

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("audible")
    @Transient
    private boolean audible = false;

    @SerializedName("has-image")
    private boolean hasImage = false;

    @NotNull
    @SerializedName("exam_id")
    private long examId;

    @ToOne(joinProperty = "examId")
    private Exam exam;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 891254763)
    private transient QuestionDao myDao;

    @Generated(hash = 1667973718)
    private transient Long exam__resolvedKey;

    public Question() {
        id = idGenerator.incrementAndGet();
    }

    @Generated(hash = 2040284166)
    public Question(long id, @NotNull String question, @NotNull List<String> options, String explanation, boolean hasImage,
            long examId) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.explanation = explanation;
        this.hasImage = hasImage;
        this.examId = examId;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public long getId() {
        return id;
    }

    public boolean isAttempted() {
        return attemptedOptionsIndices.size() > 0;
    }

    public void resetAttempted() {
        attemptedOptionsIndices.clear();
    }

    public void resetExplained() {
        explained = false;
    }

    public void setAttemptedOptionsIndices(List<Integer> attemptedOptionsIndeices) {

        this.attemptedOptionsIndices = attemptedOptionsIndeices;
    }

    public List<Integer> getAttemptedOptionsIndices() {
        return attemptedOptionsIndices;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public boolean isExplained() {
        return explained;
    }

    public void setExplained() {
        this.explained = true;
    }

    public List<Integer> getCorrectOptionIndices() {
        return correctOptionIndices;
    }

    public void setCorrectOptionIndices(List<Integer> correctOptionIndices) {
        this.correctOptionIndices = correctOptionIndices;
    }

    public boolean isAudible() {
        return audible;
    }

    public void setAudible(boolean audible) {
        this.audible = audible;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public boolean isAttemptCorrect() {
        if(attemptedOptionsIndices.size() != correctOptionIndices.size()) return false;
        return attemptedOptionsIndices.containsAll(correctOptionIndices) && correctOptionIndices.containsAll(attemptedOptionsIndices);
    }

    public int getNumberOfCorrectOptions() {
        return correctOptionIndices.size();
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getExplained() {
        return this.explained;
    }

    public void setExplained(boolean explained) {
        this.explained = explained;
    }

    public boolean getAudible() {
        return this.audible;
    }

    public boolean getHasImage() {
        return this.hasImage;
    }

    public long getExamId() {
        return this.examId;
    }

    public void setExamId(long examId) {
        this.examId = examId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2108148430)
    public Exam getExam() {
        long __key = this.examId;
        if (exam__resolvedKey == null || !exam__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ExamDao targetDao = daoSession.getExamDao();
            Exam examNew = targetDao.load(__key);
            synchronized (this) {
                exam = examNew;
                exam__resolvedKey = __key;
            }
        }
        return exam;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1996539340)
    public void setExam(@NotNull Exam exam) {
        if (exam == null) {
            throw new DaoException("To-one property 'examId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.exam = exam;
            examId = exam.getId();
            exam__resolvedKey = examId;
        }
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
    @Generated(hash = 754833738)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getQuestionDao() : null;
    }
}
package zewd.com.learnamharic.model;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zewd.com.learnamharic.PassApplication;
import zewd.com.learnamharic.R;
import zewd.com.learnamharic.utils.GradeEnum;

public class ExamSession {

    public static final int WARN = 1;

    public static final int OVER = 2;

    public static final int PROGRESS = 3;

    public static final int RESTART = 4;

    private static ExamSession instance;

    private int current = 0;

    private boolean liveCorrectionEnabled = false;

    private boolean submitted = false;

    private boolean showingExplanation = false;

    private int elapsedTime;

    private Timer timer;

    private Exam exam;

    private List<Question> questions = new ArrayList<>();

    private TimerTask timerTask;

    private Context context;

    private ExamSession() {}

    private ExamSession(Context context, long examId) {
        this.context = context;
        DaoSession daoSession = ((PassApplication) (context.getApplicationContext())).getDaoSession();
        ExamDao examDao = daoSession.getExamDao();
        Exam loadedExam = examDao.load(examId);
        if(loadedExam != null) {
            exam = loadedExam;
            questions = loadedExam.getQuestions();
        }
    }

    public static ExamSession getInstance(Context context, long examId) throws ExamDoesNotExistException {

        if(instance == null) {

            instance = new ExamSession(context, examId);
        }

        return instance;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getCurrent() {
        return current;
    }

    public boolean isLiveCorrectionEnabled() {
        return liveCorrectionEnabled;
    }

    public void setLiveCorrectionEnabled(boolean liveCorrectionEnabled) {
        this.liveCorrectionEnabled = liveCorrectionEnabled;
    }

    public boolean isShowingExplanation() {
        return showingExplanation;
    }

    public void setShowingExplanation(boolean showingExplanation) {
        this.showingExplanation = showingExplanation;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public int getCorrectCount() {

        int correctCount = 0;

        for(Question q : questions) {

            if(q.isAttemptCorrect()) {

                correctCount++;
            }
        }

        return correctCount;
    }

    public List<Question> getCorrectQuestions() {
        List<Question> qs = new ArrayList<>();
        for(Question question : questions) {
            if(question.isAttemptCorrect()) {
                qs.add(question);
            }
        }
        return qs;
    }

    public List<Question> getIncorrectQuestions() {
        List<Question> incorrectQuestions = new ArrayList<>(questions);
        incorrectQuestions.removeAll(getCorrectQuestions());
        return incorrectQuestions;
    }

    public boolean areAllQuestionsAttempted() {

        for(Question q : questions) {

            if(!q.isAttempted()) return false;
        }

        return true;
    }

    public void resetAllQuestions() {

        for(Question q : questions) {

            q.resetAttempted();

            q.resetExplained();
        }
    }

    public int getAttemptedCount() {

        int attemptedCount = 0;

        for(Question q : questions) {

            if(q.isAttempted()) {

                attemptedCount++;
            }
        }

        return attemptedCount;
    }

    public int getUndoneCount() {

        return exam.getQuestionCount() - getAttemptedCount();
    }

    public float getScore() {

        return ((float) getCorrectCount() / exam.getQuestionCount()) * 100;
    }

    public GradeEnum getGrade() {

        float score = getScore();

        return GradeEnum.getGrade(score);
    }

    public int getElapsedTIme() {
        return elapsedTime;
    }

    public void start(final Handler handler) {

        timerTask = new TimerTask() {

            boolean warned = false;

            @Override
            public void run() {

                elapsedTime += 1;

                if(!warned && elapsedTime > exam.getDuration() - 20) {

                    handler.obtainMessage(WARN).sendToTarget();

                    warned = true;
                }

                if(elapsedTime >= exam.getDuration()) {

                    handler.obtainMessage(OVER).sendToTarget();

                    stop();
                }

                handler.obtainMessage(PROGRESS, new TimeUnit(elapsedTime)).sendToTarget();
            }
        };

        timer = new Timer();

        elapsedTime = 0;

        current = 0;

        submitted = false;

        resetAllQuestions();

        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public void restartExam(Handler handler) {

        if(timerTask != null) {

            stop();

            handler.obtainMessage(RESTART).sendToTarget();

            start(handler);
        }
    }

    public void stop() {

        submitted = true;

        if(timer != null) {

            timer.cancel();

            timer = null;
        }
    }

    public Exam getExam() {
        return exam;
    }

    public class TimeUnit {

        private int seconds;

        private int minutes;

        TimeUnit(int seconds) {

            minutes = seconds / 60;

            this.seconds = seconds % 60;
        }

        TimeUnit(int minutes, int seconds) {

            this.minutes = minutes;

            this.seconds = seconds;
        }

        public String getTimeString(Context context) {
            return String.format(context.getString(R.string.elapsed_time), (minutes < 10) ? "0" + minutes : minutes, (seconds < 10) ? "0" + seconds : seconds);
        }
    }

    public static class ExamDoesNotExistException extends Exception {
        @Override
        public String getMessage() {
            return "Exam file does not exist!";
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }
}

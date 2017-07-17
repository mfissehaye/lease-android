package zewd.com.learnamharic.utils;

import android.content.Context;

import zewd.com.learnamharic.model.Exam;
import zewd.com.learnamharic.model.ExamSession;

public interface ExamFactory {
    Exam getExam(Context context);
}

package zewd.com.learnamharic.client;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import zewd.com.learnamharic.model.Exam;
import zewd.com.learnamharic.model.Question;

public interface ExamClient {
    @GET("/exam-server/public/api/v1/exams")
    Call<List<Exam>> getExams();

    @GET("/exam-server/public/api/v1/exams/{id}/questions")
    Call<List<Question>> getQuestions(@Path("id") long id);
}

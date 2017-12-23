package ahadoo.com.collect.client;

import java.util.List;

import ahadoo.com.collect.model.Survey;
import ahadoo.com.collect.model.SurveyResults;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SurveyClient {
    @GET("/surveys/")
    public Call<SurveyResults> all();
}

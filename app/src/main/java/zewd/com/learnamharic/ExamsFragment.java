package zewd.com.learnamharic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zewd.com.learnamharic.adapter.Constants;
import zewd.com.learnamharic.adapter.ExamsAdapter;
import zewd.com.learnamharic.client.ExamClient;
import zewd.com.learnamharic.model.Exam;

public class ExamsFragment extends Fragment {

    private ExamsAdapter adapter;

    public ExamsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_exams, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        final List<Exam> exams = new ArrayList<>();

        adapter = new ExamsAdapter(getContext(), exams);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setNestedScrollingEnabled(true);

        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        ExamClient examClient = retrofit.create(ExamClient.class);
        Call<List<Exam>> examsCall = examClient.getExams();
        progressBar.setVisibility(View.VISIBLE);
        examsCall.enqueue(new Callback<List<Exam>>() {
            @Override
            public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    exams.clear();
                    exams.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Invalid response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Exam>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Connection failed", Toast.LENGTH_SHORT).show();
//                Log.e(ExamsFragment.class.getSimpleName(), t.getMessage());
            }
        });

        return view;
    }

    public static ExamsFragment newInstance() {

        ExamsFragment fragment = new ExamsFragment();

        return fragment;
    }
}

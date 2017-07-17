package zewd.com.learnamharic.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.picasso.Picasso;
import java.util.List;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zewd.com.learnamharic.ExamDetailsActivity;
import zewd.com.learnamharic.PassApplication;
import zewd.com.learnamharic.R;
import zewd.com.learnamharic.client.ExamClient;
import zewd.com.learnamharic.model.DaoSession;
import zewd.com.learnamharic.model.Exam;
import zewd.com.learnamharic.model.ExamDao;
import zewd.com.learnamharic.model.Question;
import zewd.com.learnamharic.model.QuestionDao;

public class ExamsAdapter extends RecyclerView.Adapter<ExamsAdapter.ExamViewHolder> {

    private List<Exam> exams;
    private Context context;

    public ExamsAdapter(Context context, List<Exam> exams) {
        this.context = context;
        this.exams = exams;
    }

    @Override
    public ExamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exams_adapter_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExamsAdapter.ExamViewHolder holder, int position) {
        Exam exam = exams.get(position);
        holder.bind(exam);
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    class ExamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView icon;
        private TextView title;
        private TextView description;
        private ImageView favoriteButton;
        private View view;
        DaoSession daoSession = ((PassApplication) context.getApplicationContext()).getDaoSession();
        ExamViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            icon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            favoriteButton = (ImageView) itemView.findViewById(R.id.favorite);

            favoriteButton.setOnClickListener(this);
        }

        void bind(Exam exam) {
            Picasso.with(context).load(exam.getIconFileURL()).resize(50, 50).into(icon);
            title.setText(exam.getTitle());
            description.setText(exam.getDescription());
            favoriteButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_black_24dp));
            favoriteButton.setColorFilter(ContextCompat.getColor(context, R.color.blue));
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));

            // check if exam is saved in database
            ExamDao examDao = daoSession.getExamDao();
            final Exam loadedExam = examDao.load(exam.getId());
            if(loadedExam != null) {
                favoriteButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_black_24dp));
                favoriteButton.setColorFilter(ContextCompat.getColor(context, R.color.blue));
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.very_light_gray));

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Exam is already saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ExamDetailsActivity.class);
                        intent.putExtra(ExamDetailsActivity.EXAM_TITLE, loadedExam.getTitle());
                        intent.putExtra(ExamDetailsActivity.EXAM_DURATION, loadedExam.getDuration());
                        intent.putExtra(ExamDetailsActivity.EXAM_NUMBER_OF_QUESTIONS, loadedExam.getQuestionCount());
                        intent.putExtra(ExamDetailsActivity.EXAM_INSTRUCTIONS, loadedExam.getInstructions());
                        context.startActivity(intent);
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor()).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ExamClient examClient = retrofit.create(ExamClient.class);
            final Exam exam = exams.get(getAdapterPosition());
            Call<List<Question>> call = examClient.getQuestions(exam.getId());
            call.enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(context, "Received " + response.body().size() + " Questions", Toast.LENGTH_SHORT).show();

                        // save the exam first
                        ExamDao examDao = daoSession.getExamDao();
                        examDao.insert(exam);

                        // save the questions
                        List<Question> questions = response.body();
                        QuestionDao questionDao = daoSession.getQuestionDao();
                        for(Question question: questions) {
                            questionDao.insert(question);
                        }
                        Toast.makeText(context, "Saved exam", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Unsuccessful response", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    Toast.makeText(context, "Unable to connect", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
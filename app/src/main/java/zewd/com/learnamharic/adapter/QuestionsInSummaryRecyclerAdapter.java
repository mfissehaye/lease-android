package zewd.com.learnamharic.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import zewd.com.learnamharic.QuestionActivity;
import zewd.com.learnamharic.R;
import zewd.com.learnamharic.model.Exam;
import zewd.com.learnamharic.model.ExamSession;
import zewd.com.learnamharic.model.Question;

public class QuestionsInSummaryRecyclerAdapter extends RecyclerView.Adapter<QuestionsInSummaryRecyclerAdapter.CustomViewHolder> {

    private List<Question> questions;

    public enum Filter {

        ALL,

        CORRECT,

        INCORRECT,

        FLAGGED
    }

    private Filter filter = Filter.ALL;

    private Context context;

    private ExamSession session;

    public QuestionsInSummaryRecyclerAdapter(Context context, ExamSession session) {

        this.context = context;

        this.session = session;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_single_question_in_summary_recycler, null);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        switch(filter) {

            case ALL:

                this.questions = session.getQuestions();

                break;

            case CORRECT:

                this.questions = session.getCorrectQuestions();

                break;

            case INCORRECT:

                this.questions = session.getIncorrectQuestions();

                break;

            case FLAGGED:

                this.questions = new ArrayList<>();
        }

        Question question = questions.get(position);

        holder.bind(question);
    }

    @Override
    public int getItemCount() {

        int questionCount = session.getExam().getQuestionCount();

        int correctCount = session.getCorrectCount();

        int incorrectCount = questionCount - correctCount;

        int resultCount = 0;

        switch (filter) {

            case ALL:

                return questionCount;

            case CORRECT:

                return correctCount;

            case INCORRECT:

                return incorrectCount;

            case FLAGGED:

                return 0;
        }

        return resultCount;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView questionTextView;

        private View borderView;

        private TextView correctIncorrectTextView;

        private View view;

        CustomViewHolder(View itemView) {

            super(itemView);

            view = itemView;

            questionTextView = (TextView) itemView.findViewById(R.id.question_text);

            borderView = itemView.findViewById(R.id.border);

            correctIncorrectTextView = (TextView) itemView.findViewById(R.id.correct_incorrect_text);
        }

        private void bind(final Question question) {

            if(question.isAttemptCorrect()) {

                borderView.setBackgroundColor(ContextCompat.getColor(context, R.color.green));

                correctIncorrectTextView.setText(context.getString(R.string.correct));

            } else {

                borderView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));

                correctIncorrectTextView.setText(context.getString(R.string.incorrect));
            }

            questionTextView.setText(question.getQuestion());

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    int viewPagerPosition = session.getQuestions().indexOf(question);

                    ((QuestionActivity) context).scrollViewPagerToPosition(viewPagerPosition + 1);
                }
            });
        }
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}

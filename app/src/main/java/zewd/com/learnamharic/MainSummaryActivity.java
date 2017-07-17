package zewd.com.learnamharic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import zewd.com.learnamharic.adapter.QuestionsInSummaryRecyclerAdapter;
import zewd.com.learnamharic.model.ExamSession;

public class MainSummaryActivity extends AppCompatActivity {

    private QuestionsInSummaryRecyclerAdapter adapter;

    private TextView filterAllButton;

    private TextView filterCorrectButton;

    private TextView filterIncorrectButton;

    private ExamSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_fragment_exam_summary);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        TextView startButton = (TextView) findViewById(R.id.start_button);

        TextView scoreTextView = (TextView) findViewById(R.id.score);

        TextView elapsedTimeTextView = (TextView) findViewById(R.id.time_elapsed);

        filterAllButton = (TextView) findViewById(R.id.filter_all);

        filterCorrectButton = (TextView) findViewById(R.id.filter_correct);

        filterIncorrectButton = (TextView) findViewById(R.id.filter_incorrect);

        setSupportActionBar(toolbar);

        try {

            session = ExamSession.getInstance(this, 1);

            adapter = new QuestionsInSummaryRecyclerAdapter(this, session);

        } catch (ExamSession.ExamDoesNotExistException e) {

            Toast.makeText(this, "Exam not found", Toast.LENGTH_SHORT).show();

            finish();

            e.printStackTrace();
        }

        recyclerView.setAdapter(adapter);

        recyclerView.setFocusable(false);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                session.setSubmitted(false);

                //todo remove attempted labels from all questions

                startActivity(new Intent(MainSummaryActivity.this, QuestionActivity.class));
            }
        });

        int elapsedTime = session.getElapsedTIme();

        int minutes = elapsedTime / 60;

        int seconds = elapsedTime % 60;

        elapsedTimeTextView.setText(String.format(getString(R.string.elapsed_time), (minutes < 10) ? "0" + minutes : minutes, (seconds < 10) ? "0" + seconds : seconds));

        if(session.getScore() == 100f) {

            // remove the trailing decimal points to save space
            scoreTextView.setText(String.valueOf((int) session.getScore()) + "%");

        } else {

            scoreTextView.setText(String.format(getString(R.string.score_percentage), session.getScore()));
        }

        int questionsCount = session.getQuestions().size();

        int correctCount = session.getCorrectCount();

        int incorrectCount = questionsCount - correctCount;

        filterAllButton.setText(String.format(getString(R.string.all_count), questionsCount));

        filterCorrectButton.setText(String.format(getString(R.string.correct_count), correctCount));

        filterIncorrectButton.setText(String.format(getString(R.string.incorrect_count), incorrectCount));
    }

    public void resetAllFilterButtons() {
        filterAllButton.setBackground(null);
        filterAllButton.setTextColor(ContextCompat.getColor(this, R.color.blueBlack));
        filterCorrectButton.setBackground(null);
        filterCorrectButton.setTextColor(ContextCompat.getColor(this, R.color.blueBlack));
        filterIncorrectButton.setBackground(null);
        filterIncorrectButton.setTextColor(ContextCompat.getColor(this, R.color.blueBlack));
    }

    public void filterAll(View view) {
        adapter.setFilter(QuestionsInSummaryRecyclerAdapter.Filter.ALL);
        resetAllFilterButtons();
        filterAllButton.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_background_narrow_blue_black));
        filterAllButton.setTextColor(ContextCompat.getColor(this, R.color.white));
        adapter.notifyDataSetChanged();
    }

    public void filterCorrect(View view) {
        adapter.setFilter(QuestionsInSummaryRecyclerAdapter.Filter.CORRECT);
        resetAllFilterButtons();
        filterCorrectButton.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_background_narrow_green));
        filterCorrectButton.setTextColor(ContextCompat.getColor(this, R.color.white));
        adapter.notifyDataSetChanged();
    }

    public void filterIncorrect(View view) {
        adapter.setFilter(QuestionsInSummaryRecyclerAdapter.Filter.INCORRECT);
        resetAllFilterButtons();
        filterIncorrectButton.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_background_narrow_red));
        filterIncorrectButton.setTextColor(ContextCompat.getColor(this, R.color.white));
        adapter.notifyDataSetChanged();
    }
}

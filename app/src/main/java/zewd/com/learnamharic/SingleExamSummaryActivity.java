package zewd.com.learnamharic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import zewd.com.learnamharic.model.ExamSession;

public class SingleExamSummaryActivity extends AppCompatActivity {

    private ExamSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_exam_summary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        TextView startButton = (TextView) findViewById(R.id.start_button);

        TextView correctCountTextView = (TextView) findViewById(R.id.button_correct_count);

        TextView attemptedCountTextView = (TextView) findViewById(R.id.button_attempted_count);

        TextView undoneCountTextView = (TextView) findViewById(R.id.button_undone_count);

        TextView scoreTextView = (TextView) findViewById(R.id.score);

        TextView timerTextView = (TextView) findViewById(R.id.time_elapsed);

        setSupportActionBar(toolbar);

        try {

            session = ExamSession.getInstance(this, 1);

        } catch (ExamSession.ExamDoesNotExistException e) {

            Toast.makeText(this, "Exam not found", Toast.LENGTH_SHORT).show();

            finish();

            e.printStackTrace();

            return;
        }

        correctCountTextView.setText(String.valueOf(session.getCorrectCount()));

        attemptedCountTextView.setText(String.valueOf(session.getAttemptedCount()));

        undoneCountTextView.setText(String.valueOf(session.getUndoneCount()));

        int elapsedTime = session.getElapsedTIme();

        int minutes = elapsedTime / 60;

        int seconds = elapsedTime % 60;

        timerTextView.setText(String.format(getString(R.string.elapsed_time), (minutes < 10) ? "0" + minutes : minutes, (seconds < 10) ? "0" + seconds : seconds));

        if(session.getScore() == 100f) {

            // remove the trailing decimal points to save space
            scoreTextView.setText(String.valueOf((int) session.getScore()) + "%");

        } else {

            scoreTextView.setText(String.format(getString(R.string.score_percentage), session.getScore()));
        }

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SingleExamSummaryActivity.this, QuestionActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
        });
    }
}

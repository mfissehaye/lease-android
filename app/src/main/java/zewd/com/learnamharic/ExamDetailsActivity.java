package zewd.com.learnamharic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import zewd.com.learnamharic.adapter.InstructionsArrayAdapter;

public class ExamDetailsActivity extends AppCompatActivity {

    public static final String EXAM_TITLE = "title-extra";

    public static final String EXAM_DURATION = "duration-extra";

    public static final String EXAM_NUMBER_OF_QUESTIONS = "number-of-questions-extra";

    public static final String EXAM_INSTRUCTIONS = "instructions-extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exam_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        String examTitle = bundle.getString(EXAM_TITLE);

        toolbar.setTitle(examTitle);

        long examDuration = bundle.getLong(EXAM_DURATION);

        int examNumberOfQuestions = bundle.getInt(EXAM_NUMBER_OF_QUESTIONS);

        String examInstructions = bundle.getString(EXAM_INSTRUCTIONS);

        ListView instructionListView = (ListView) findViewById(R.id.instruction_list);

        instructionListView.setAdapter(new InstructionsArrayAdapter(this, examInstructions.split("\\|")));

        View headerView = getLayoutInflater().inflate(R.layout.exam_details_header, null);

        View footerView = getLayoutInflater().inflate(R.layout.exam_details_footer, null);

        TextView examTitleTextView = (TextView) headerView.findViewById(R.id.exam_title);

        TextView examDurationTextView = (TextView) headerView.findViewById(R.id.exam_duration);

        TextView examNumberOfQuestionsTextView = (TextView) headerView.findViewById(R.id.number_of_questions);

        examTitleTextView.setText(examTitle);

        examDurationTextView.setText("" + examDuration);

        examNumberOfQuestionsTextView.setText("" + examNumberOfQuestions);

        instructionListView.addHeaderView(headerView);

        instructionListView.addFooterView(footerView);
    }

    public void onStartButtonClicked(View view) {

        Intent intent = new Intent(ExamDetailsActivity.this, QuestionActivity.class);

        startActivity(intent);

        finish();
    }
}
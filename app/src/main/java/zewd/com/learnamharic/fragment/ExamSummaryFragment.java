package zewd.com.learnamharic.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import zewd.com.learnamharic.QuestionActivity;
import zewd.com.learnamharic.R;
import zewd.com.learnamharic.adapter.QuestionsInSummaryRecyclerAdapter;
import zewd.com.learnamharic.model.ExamSession;

public class ExamSummaryFragment extends Fragment {

    private ExamSession session;

    private QuestionsInSummaryRecyclerAdapter adapter;

    private TextView filterAllButton;

    private TextView filterCorrectButton;

    private TextView filterIncorrectButton;

    public ExamSummaryFragment() {
        // Required empty public constructor
    }

    public static ExamSummaryFragment getInstance(ExamSession session) {

        ExamSummaryFragment fragment = new ExamSummaryFragment();

        fragment.session = session;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_fragment_exam_summary, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        TextView startButton = (TextView) view.findViewById(R.id.start_button);

        TextView scoreTextView = (TextView) view.findViewById(R.id.score);

        TextView elapsedTimeTextView = (TextView) view.findViewById(R.id.time_elapsed);

        TextView subjectTextView = (TextView) view.findViewById(R.id.subject);

        TextView examTagTextView = (TextView) view.findViewById(R.id.exam_tag);

        TextView gradeTextView = (TextView) view.findViewById(R.id.grade);

        filterAllButton = (TextView) view.findViewById(R.id.filter_all);

        filterCorrectButton = (TextView) view.findViewById(R.id.filter_correct);

        filterIncorrectButton = (TextView) view.findViewById(R.id.filter_incorrect);

        subjectTextView.setText(session.getExam().getTitle());

        examTagTextView.setText(String.format(getString(R.string.exam_tag), session.getExam().getYearTag()));

        gradeTextView.setText(session.getGrade().toString());

        try {

            session = ExamSession.getInstance(getContext(), 1);

            adapter = new QuestionsInSummaryRecyclerAdapter(getContext(), session);

            recyclerView.setAdapter(adapter);

            recyclerView.setFocusable(false);

            recyclerView.setNestedScrollingEnabled(false);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        } catch (ExamSession.ExamDoesNotExistException e) {

            Toast.makeText(getContext(), "Exam not found", Toast.LENGTH_SHORT).show();

            getActivity().finish();

            e.printStackTrace();

            return view;
        }

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                QuestionActivity activity = ((QuestionActivity) getActivity());

                activity.restartExam();

                activity.scrollViewPagerToPosition(1);
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

        filterAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterAll();
            }
        });

        filterCorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterCorrect();
            }
        });

        filterIncorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterIncorrect();
            }
        });

        return view;
    }

    public void resetAllFilterButtons() {
        filterAllButton.setBackground(null);
        filterAllButton.setTextColor(ContextCompat.getColor(getContext(), R.color.blueBlack));
        filterCorrectButton.setBackground(null);
        filterCorrectButton.setTextColor(ContextCompat.getColor(getContext(), R.color.blueBlack));
        filterIncorrectButton.setBackground(null);
        filterIncorrectButton.setTextColor(ContextCompat.getColor(getContext(), R.color.blueBlack));
    }

    public void filterAll() {
        adapter.setFilter(QuestionsInSummaryRecyclerAdapter.Filter.ALL);
        resetAllFilterButtons();
        filterAllButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_background_narrow_blue_black));
        filterAllButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        adapter.notifyDataSetChanged();
    }

    public void filterCorrect() {
        adapter.setFilter(QuestionsInSummaryRecyclerAdapter.Filter.CORRECT);
        resetAllFilterButtons();
        filterCorrectButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_background_narrow_green));
        filterCorrectButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        adapter.notifyDataSetChanged();
    }

    public void filterIncorrect() {
        adapter.setFilter(QuestionsInSummaryRecyclerAdapter.Filter.INCORRECT);
        resetAllFilterButtons();
        filterIncorrectButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_background_narrow_red));
        filterIncorrectButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        adapter.notifyDataSetChanged();
    }
}

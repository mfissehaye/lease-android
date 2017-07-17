package zewd.com.learnamharic.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import zewd.com.learnamharic.adapter.SingleQuestionRecyclerAdapter;
import zewd.com.learnamharic.R;
import zewd.com.learnamharic.model.Exam;
import zewd.com.learnamharic.model.ExamSession;
import zewd.com.learnamharic.model.Question;

public class QuestionFragment extends Fragment {

    private int answerPosition = -1;

    private int index;

    private ExamSession session;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(int position) {

        QuestionFragment fragment = new QuestionFragment();

        fragment.index = position;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_question, container, false);

        try {

            this.session = ExamSession.getInstance(getContext(), 1);

            final Question q = session.getQuestions().get(index);

            RecyclerView optionsRecyclerView = (RecyclerView) view.findViewById(R.id.options_recycler_view);

            final SingleQuestionRecyclerAdapter adapter = new SingleQuestionRecyclerAdapter(getContext(), q);

            optionsRecyclerView.setAdapter(adapter);

            optionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        } catch (ExamSession.ExamDoesNotExistException e) {

            Toast.makeText(getContext(), "Exam not found", Toast.LENGTH_SHORT).show();

            getActivity().finish();

            e.printStackTrace();

            return view;
        }

        return view;
    }
}

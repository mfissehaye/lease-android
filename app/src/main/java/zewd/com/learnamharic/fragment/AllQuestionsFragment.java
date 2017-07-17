package zewd.com.learnamharic.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import zewd.com.learnamharic.QuestionActivity;
import zewd.com.learnamharic.adapter.AllQuestionsRecyclerAdapter;
import zewd.com.learnamharic.R;
import zewd.com.learnamharic.model.ExamSession;

public class AllQuestionsFragment extends Fragment {

    ExamSession session;

    public AllQuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_questions, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        RelativeLayout completeExamButtonContainer = (RelativeLayout) view.findViewById(R.id.complete_exam_button_container);

        final AllQuestionsRecyclerAdapter adapter;

        try {

            session = ExamSession.getInstance(getContext(), 1);

            adapter = new AllQuestionsRecyclerAdapter(getContext(), session);

            recyclerView.setAdapter(adapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

            if(session.areAllQuestionsAttempted() && !session.isSubmitted()) {

                completeExamButtonContainer.setVisibility(View.VISIBLE);
            }

            completeExamButtonContainer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    ((QuestionActivity) getActivity()).stopExam(null);
                }
            });

        } catch (ExamSession.ExamDoesNotExistException e) {

            e.printStackTrace();

            Toast.makeText(getContext(), "Exam not found", Toast.LENGTH_SHORT).show();

            getActivity().finish();
        }

        return view;
    }
}

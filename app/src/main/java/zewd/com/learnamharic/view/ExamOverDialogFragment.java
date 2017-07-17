package zewd.com.learnamharic.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import zewd.com.learnamharic.QuestionActivity;
import zewd.com.learnamharic.R;
import zewd.com.learnamharic.model.ExamSession;

public class ExamOverDialogFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_dialog_exam_over, container);

        TextView seeResultsTextView = (TextView) view.findViewById(R.id.see_results);

        TextView restartExamTextView = (TextView) view.findViewById(R.id.restart_exam);

        seeResultsTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ((QuestionActivity) getActivity()).showResults();

                dismiss();
            }
        });

        restartExamTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((QuestionActivity) getActivity()).retakeExam();

                dismiss();
            }
        });

        return view;
    }
}

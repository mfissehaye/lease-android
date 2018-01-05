package ahadoo.com.collect.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ahadoo.com.collect.QuestionActivity;
import ahadoo.com.collect.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubmitSurveyFragment extends Fragment {

    public static SubmitSurveyFragment getInstance() {
        return new SubmitSurveyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.single_question_layout_submit, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.review_responses) void enterReviewMode() {

        QuestionActivity parentActivity = (QuestionActivity) getActivity();

        if(parentActivity != null) {

            parentActivity.setReviewing();
        }
    }
}

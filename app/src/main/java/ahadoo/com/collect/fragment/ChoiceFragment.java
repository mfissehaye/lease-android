package ahadoo.com.collect.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ahadoo.com.collect.QuestionActivity;
import ahadoo.com.collect.R;
import ahadoo.com.collect.adapter.ChoicesAdapter;
import ahadoo.com.collect.model.SurveyChoice;
import ahadoo.com.collect.model.SurveyQuestion;
import ahadoo.com.collect.util.OnChoiceSelected;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoiceFragment extends Fragment {

    private SurveyQuestion question;

    @BindView(R.id.question)
    TextView titleTextView;

    @BindView(R.id.bullet)
    TextView bulletTextView;

    @BindView(R.id.choices)
    RecyclerView choicesRecyclerView;

    private boolean allowMultipleChoiceSelection;

    public static Fragment getInstance(SurveyQuestion question, boolean allowMultipleChoiceSelection) {

        ChoiceFragment fragment = new ChoiceFragment();

        fragment.question = question;

        fragment.allowMultipleChoiceSelection = allowMultipleChoiceSelection;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null) {

            question.response = savedInstanceState.getString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER);
        }

        // Get the selected choice
        SurveyChoice selectedChoice = null;

        for(SurveyChoice choice : question.choices) {

            String response = question.response;

            if(response != null && response.equals(choice.uuid)) {

                selectedChoice = choice;
            }
        }

        View root = inflater.inflate(R.layout.single_question_layout_choose_one, container, false);

        ButterKnife.bind(this, root);

        bulletTextView.setText(String.format(getString(R.string.bullet), question.index));

        titleTextView.setText(question.text.toString());

        choicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ChoicesAdapter adapter = new ChoicesAdapter(getContext(), question.choices, selectedChoice);

        choicesRecyclerView.setAdapter(adapter);

        // set the selected choice and response
        for (SurveyChoice choice : question.choices) {

            String response = question.response;

            if (response != null && response.equals(choice.uuid)) {

                adapter.selectedChoice = choice;
            }
        }

        adapter.setOnChoiceSelectedListener(new OnChoiceSelected() {

            @Override
            public void choiceSelected(SurveyChoice choice) {

                question.response = choice.uuid;
            }
        });

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER, question.response);
    }
}

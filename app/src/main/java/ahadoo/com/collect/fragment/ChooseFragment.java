package ahadoo.com.collect.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ahadoo.com.collect.QuestionActivity;
import ahadoo.com.collect.R;
import ahadoo.com.collect.adapter.ChoicesAdapter;
import ahadoo.com.collect.model.SurveyChoice;
import ahadoo.com.collect.model.SurveyQuestion;
import ahadoo.com.collect.util.OnChoiceSelected;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseFragment extends Fragment {

    private SurveyQuestion question;

    @BindView(R.id.question)
    TextView titleTextView;

    @BindView(R.id.bullet)
    TextView bulletTextView;

    @BindView(R.id.choices)
    RecyclerView choicesRecyclerView;

    private boolean allowMultipleChoiceSelection;

    public static Fragment getInstance(SurveyQuestion question, boolean allowMultipleChoiceSelection) {

        ChooseFragment fragment = new ChooseFragment();

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

        List<SurveyChoice> selectedChoices = new ArrayList<>();

        String response = question.response;

        if(response != null) {

            String[] selectedChoiceUUIDs = TextUtils.split(response, ",");

            for(SurveyChoice choice : question.choices) {

                for(String choiceUUID : selectedChoiceUUIDs) {

                    if(choice.uuid.equals(choiceUUID)) {

                        selectedChoices.add(choice);
                    }
                }
            }
        }

        View root = inflater.inflate(R.layout.single_question_layout_choose_one, container, false);

        ButterKnife.bind(this, root);

        bulletTextView.setText(String.format(getString(R.string.bullet), question.index));

        titleTextView.setText(question.text.toString());

        choicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ChoicesAdapter adapter = new ChoicesAdapter(getContext(), question.choices, selectedChoices, allowMultipleChoiceSelection);

        choicesRecyclerView.setAdapter(adapter);

        adapter.setOnChoiceSelectedListener(new OnChoiceSelected() {

            @Override
            public void choicesSelected(List<SurveyChoice> choices) {

                StringBuilder builder = new StringBuilder();

                for(int i = 0; i < choices.size(); i++) {

                    SurveyChoice choice = choices.get(i);

                    builder.append(choice.uuid);

                    if(i < choices.size() - 1) {

                        builder.append(',');
                    }
                }

                question.response = builder.toString();
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

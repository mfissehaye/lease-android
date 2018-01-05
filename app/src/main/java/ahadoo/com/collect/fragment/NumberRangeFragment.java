package ahadoo.com.collect.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ahadoo.com.collect.QuestionActivity;
import ahadoo.com.collect.R;
import ahadoo.com.collect.model.SurveyQuestion;
import ahadoo.com.collect.util.SurveyNumberRange;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NumberRangeFragment extends Fragment {

    private SurveyQuestion question;

    private SurveyNumberRange range;

    @BindView(R.id.range_text)
    TextView rangeText;

    public static NumberRangeFragment getInstance(SurveyQuestion question) {

        NumberRangeFragment fragment = new NumberRangeFragment();

        fragment.question = question;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null) {

            question.response = savedInstanceState.getString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER);
        }

        View view = inflater.inflate(R.layout.single_question_layout_number_range, container, false);

        ButterKnife.bind(this, view);

        TextView titleTextView = view.findViewById(R.id.question);

        EditText responseNumberFromEditText = view.findViewById(R.id.response_number_from);

        EditText responseNumberToEditText = view.findViewById(R.id.response_number_to);

        responseNumberFromEditText.requestFocus();

        titleTextView.setText(question.text.toString());

        SurveyNumberRange range = new SurveyNumberRange(question.response);

        if(range.from > 0) {

            responseNumberFromEditText.setText(String.valueOf(range.from));
        }

        if(range.to > 0) {

            responseNumberToEditText.setText(String.valueOf(range.to));
        }

        responseNumberFromEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                SurveyNumberRange range = new SurveyNumberRange(question.response);

                try {

                    range.from = Double.parseDouble(s.toString());

                } catch (NumberFormatException e) {

//                    range.from = 0;
                    // keep the range unchanged
                }

                question.response = range.toString();
            }
        });

        responseNumberToEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                SurveyNumberRange range = new SurveyNumberRange(question.response);

                try {

                    range.to = Double.parseDouble(s.toString());

                } catch(NumberFormatException e) {

//                    range.to = 0;
                    // keep the range unchanged
                }

                question.response = range.toString();
            }
        });

        QuestionActivity parentActivity = (QuestionActivity) getActivity();

        if(parentActivity != null && parentActivity.isReviewing()) {

            responseNumberFromEditText.setVisibility(View.INVISIBLE);

            responseNumberToEditText.setVisibility(View.INVISIBLE);

            range = new SurveyNumberRange(question.response);

            rangeText.setText(getString(R.string.range, range.from, range.to));

            rangeText.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER, question.response);
    }
}

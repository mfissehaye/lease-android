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
import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencyFragment extends Fragment implements TextWatcher {

    private SurveyQuestion question;

    @BindView(R.id.question)
    TextView questionTextView;

    @BindView(R.id.response_editable)
    EditText responseEditable;

    @BindView(R.id.bullet)
    TextView bullet;

    public static CurrencyFragment getInstance(SurveyQuestion question) {

        CurrencyFragment fragment = new CurrencyFragment();

        fragment.question = question;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null) {

            question.response = savedInstanceState.getString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER);
        }

        View view = inflater.inflate(R.layout.single_question_layout_currency, container, false);

        ButterKnife.bind(this, view);

        questionTextView.setText(question.text.toString());

        bullet.setText(String.format(getString(R.string.bullet), question.index));

        showResponseEditText();

        return view;
    }

    private void showResponseEditText() {

        responseEditable.requestFocus();

        responseEditable.addTextChangedListener(this);

        responseEditable.setText(question.response);

        QuestionActivity parentActivity = (QuestionActivity) getActivity();

        if(parentActivity != null && parentActivity.isReviewing()) {

            responseEditable.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.inactive_editable));

            responseEditable.setEnabled(false);

            responseEditable.setInputType(InputType.TYPE_NULL);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        question.response = s.toString();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER, question.response);
    }
}

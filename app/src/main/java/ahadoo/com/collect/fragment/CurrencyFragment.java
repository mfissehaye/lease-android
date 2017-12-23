package ahadoo.com.collect.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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

        responseEditable.requestFocus();

        questionTextView.setText(question.text.toString());

        bullet.setText(String.format(getString(R.string.bullet), question.index));

        responseEditable.addTextChangedListener(this);

        responseEditable.setText(question.response);

        return view;
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
        outState.putString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER, null);
    }
}

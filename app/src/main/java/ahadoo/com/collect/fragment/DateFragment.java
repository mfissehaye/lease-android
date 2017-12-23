package ahadoo.com.collect.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Locale;

import ahadoo.com.collect.QuestionActivity;
import ahadoo.com.collect.R;
import ahadoo.com.collect.model.SurveyQuestion;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DateFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private SurveyQuestion question;

    @BindView(R.id.date_response)
    TextView dateResponse;

    @BindView(R.id.bullet)
    TextView bullet;

    @BindView(R.id.question)
    TextView questionTitleTextView;

    @BindView(R.id.pick_date_button)
    Button pickDateButton;

    public static DateFragment getInstance(SurveyQuestion question) {

        DateFragment fragment = new DateFragment();

        fragment.question = question;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.single_question_layout_date, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState != null) {

            question.response = savedInstanceState.getString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER);
        }

        if(question.response != null) {

            dateResponse.setText(question.response);
        }

        bullet.setText(String.format(getString(R.string.bullet), question.index));

        questionTitleTextView.setText(question.text.toString());

        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(

                        DateFragment.this,

                        now.get(Calendar.YEAR),

                        now.get(Calendar.MONTH),

                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getActivity().getFragmentManager(), DateFragment.class.getSimpleName());
            }
        });

        return view;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        question.response = String.format(Locale.getDefault(), "%d-%d-%d", year, monthOfYear, dayOfMonth);
        dateResponse.setText(question.response);
        dateResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER, question.response);
    }
}

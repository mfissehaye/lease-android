package ahadoo.com.collect.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ahadoo.com.collect.R;
import ahadoo.com.collect.model.SurveyChoice;

public class QuestionAdapter extends ArrayAdapter<SurveyChoice> {

    public QuestionAdapter(@NonNull Context context, List<SurveyChoice> surveyChoices) {
        super(context, 0, surveyChoices);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        SurveyChoice choice = getItem(position);

        if(convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.choice_item, parent, false);
        }

        TextView choiceTextView = (TextView) convertView.findViewById(R.id.choice_text);

        choiceTextView.setText(choice.toString());

        return convertView;
    }
}

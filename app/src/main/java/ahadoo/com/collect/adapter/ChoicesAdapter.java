package ahadoo.com.collect.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ahadoo.com.collect.QuestionActivity;
import ahadoo.com.collect.R;
import ahadoo.com.collect.model.SurveyChoice;
import ahadoo.com.collect.util.OnChoiceSelected;

public class ChoicesAdapter extends RecyclerView.Adapter<ChoicesAdapter.CustomViewHolder> {

    private final Context context;

    private final List<SurveyChoice> choices;

    private OnChoiceSelected onChoiceSelectedListener;

    public List<SurveyChoice> selectedChoices;

    private boolean allowMultipleChoice = false;

    public ChoicesAdapter(Context context, List<SurveyChoice> choices, List<SurveyChoice> selectedChoices, boolean allowMultipleChoice) {

        this.context = context;

        this.choices = choices;

        this.selectedChoices = selectedChoices;

        this.allowMultipleChoice = allowMultipleChoice;
    }

    @Override
    public ChoicesAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.choice_item, viewGroup, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChoicesAdapter.CustomViewHolder holder, int position) {
        holder.bind(choices.get(position));
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public void setOnChoiceSelectedListener(OnChoiceSelected onChoiceSelected) {
        this.onChoiceSelectedListener = onChoiceSelected;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView choiceText;

        CustomViewHolder(View itemView) {

            super(itemView);

            choiceText = itemView.findViewById(R.id.choice_text);

            itemView.setOnClickListener(this);
        }

        void bind(SurveyChoice choice) {

            QuestionActivity baseContext = (QuestionActivity) context;

            if(baseContext != null && baseContext.isReviewing()) {

                choiceText.setBackground(null);

                choiceText.setTextColor(ContextCompat.getColor(context, R.color.grey));

            } else {

                choiceText.setBackground(ContextCompat.getDrawable(context, R.drawable.curved_white_background));

                choiceText.setTextColor(ContextCompat.getColor(context, R.color.appBlack));
            }

            if(selectedChoices.contains(choice)) {

                if(baseContext != null && baseContext.isReviewing()) {

                    choiceText.setBackground(ContextCompat.getDrawable(context, R.drawable.curved_grey_background));

                    choiceText.setTextColor(ContextCompat.getColor(context, R.color.appBlack));

                } else {

                    choiceText.setBackground(ContextCompat.getDrawable(context, R.drawable.curved_black_background));

                    choiceText.setTextColor(ContextCompat.getColor(context, R.color.white));
                }
            }

            choiceText.setText(choice.text.toString());
        }

        @Override
        public void onClick(View view) {

            // disable if it is in review mode
            QuestionActivity baseContext = (QuestionActivity) context;

            if(baseContext != null && baseContext.isReviewing()) return;

            SurveyChoice clickedChoice = choices.get(getAdapterPosition());

            if(!allowMultipleChoice) {

                selectedChoices.clear();
            }

            if(!selectedChoices.contains(clickedChoice)) {

                selectedChoices.add(clickedChoice);

            } else {

                selectedChoices.remove(clickedChoice);
            }


            notifyDataSetChanged();

            onChoiceSelectedListener.choicesSelected(selectedChoices);
        }
    }
}

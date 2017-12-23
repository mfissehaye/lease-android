package ahadoo.com.collect.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ahadoo.com.collect.R;
import ahadoo.com.collect.model.SurveyChoice;
import ahadoo.com.collect.util.OnChoiceSelected;

public class ChoicesAdapter extends RecyclerView.Adapter<ChoicesAdapter.CustomViewHolder> {

    private final Context context;

    private final List<SurveyChoice> choices;

    private OnChoiceSelected onChoiceSelectedListener;

    public SurveyChoice selectedChoice;

    public ChoicesAdapter(Context context, List<SurveyChoice> choices, SurveyChoice selectedChoice) {

        this.context = context;

        this.choices = choices;

        this.selectedChoice = selectedChoice;
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

            choiceText.setBackground(ContextCompat.getDrawable(context, R.drawable.curved_white_background));

            choiceText.setTextColor(ContextCompat.getColor(context, R.color.appBlack));

            if(choice.equals(selectedChoice)) {

                choiceText.setBackground(ContextCompat.getDrawable(context, R.drawable.curved_black_background));

                choiceText.setTextColor(ContextCompat.getColor(context, R.color.white));
            }

            choiceText.setText(choice.text.toString());
        }

        @Override
        public void onClick(View view) {

            selectedChoice = choices.get(getAdapterPosition());

            notifyDataSetChanged();

            onChoiceSelectedListener.choiceSelected(selectedChoice);
        }
    }
}

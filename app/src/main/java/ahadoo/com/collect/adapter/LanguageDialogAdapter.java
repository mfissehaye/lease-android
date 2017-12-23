package ahadoo.com.collect.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import ahadoo.com.collect.QuestionActivity;
import ahadoo.com.collect.R;
import ahadoo.com.collect.fragment.LanguageDialogFragment;
import ahadoo.com.collect.model.Survey;
import ahadoo.com.collect.model.Title;
import ahadoo.com.collect.util.SurveyLanguage;

public class LanguageDialogAdapter extends RecyclerView.Adapter<LanguageDialogAdapter.CustomViewHolder> {

    private List<String> surveyLanguages;

    private LanguageDialogFragment context;

    private Survey survey;

    private int lastPosition = -1;

    public LanguageDialogAdapter(LanguageDialogFragment context, List<String> surveyLanguages, Survey survey) {

        this.surveyLanguages = surveyLanguages;

        this.context = context;

        this.survey = survey;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lang_menu_item, parent, false);

        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

//        holder.text.setText(surveyLanguages.get(position));
        String language = surveyLanguages.get(position);

        holder.title.setText(SurveyLanguage.fromCode(language).name()); // databaseResponseServices.processLanguageTitle(surveyLanguages.get(position)));

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition) {

            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)

            viewToAnimate.startAnimation(anim);

            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return surveyLanguages.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements

            View.OnClickListener,

            View.OnLongClickListener {

        TextView title;

        public CustomViewHolder(View itemView) {

            super(itemView);

            title = (TextView) itemView.findViewById(R.id.lang_dialog_text);

            itemView.setOnClickListener(this);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context.getContext(), QuestionActivity.class);

            Bundle bundle = new Bundle();

            bundle.putString(QuestionActivity.SURVEY_ID_IDENTIFIER, survey.getUuid());

            bundle.putString(QuestionActivity.SURVEY_TITLE_IDENTIFIER, survey.getTitle().toString());

            intent.putExtras(bundle);

            context.startActivity(intent);

            context.dismiss();

            String language = surveyLanguages.get(getAdapterPosition());

            Title.currentLanguage = SurveyLanguage.fromCode(language);
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }
}

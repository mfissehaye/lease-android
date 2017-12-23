package ahadoo.com.collect.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ahadoo.com.collect.R;
import ahadoo.com.collect.fragment.LanguageDialogFragment;
import ahadoo.com.collect.model.Survey;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.CustomViewHolder> {

    private List<Survey> surveys;

    private Context context;

    private int lastPosition = -1;

    public SurveyAdapter(Context context, List<Survey> surveys) {

        this.context = context;

        this.surveys = surveys;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_survey, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        Survey survey = surveys.get(position);

        if (survey.getTitle() != null) {

            String surveyTitle = survey.title.toString();

            String surveyDescription = survey.description != null ? survey.description.en : "\n";

            surveyDescription += "\nTotal Question: " + survey.surveyQuestionList.size()

                    + "\nTotal Languages: " + survey.getLanguages().size();

            holder.titleTextView.setText(surveyTitle);

            holder.descriptionTextView.setText(surveyDescription);

        } else {

            holder.view.setVisibility(View.GONE);

//            bottomUpAnimation(holder.itemView, position);
        }
    }

    private void bottomUpAnimation(View itemView, int position) {

        if (position > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);

            itemView.startAnimation(animation);

            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        return surveys.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements

            View.OnClickListener,

            View.OnLongClickListener {

        TextView titleTextView;

        TextView descriptionTextView;

        View view;

        CustomViewHolder(View itemView) {

            super(itemView);

            view = itemView;

            titleTextView = (TextView) itemView.findViewById(R.id.survey_title);

            descriptionTextView = (TextView) itemView.findViewById(R.id.survey_description);

            itemView.setOnClickListener(this);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Survey survey = surveys.get(getAdapterPosition());

            List<String> languages = survey.getLanguages();

            LanguageDialogFragment languageDialogFragment = LanguageDialogFragment.getInstance(context, languages, survey);

            languageDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), context.getString(R.string.app_name));
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }
}
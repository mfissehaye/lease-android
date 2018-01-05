package ahadoo.com.collect.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ahadoo.com.collect.R;
import ahadoo.com.collect.fragment.LanguageDialogFragment;
import ahadoo.com.collect.model.Survey;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

            holder.bind(survey);

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

        @BindView(R.id.survey_title)
        TextView titleTextView;

        @BindView(R.id.survey_description)
        TextView descriptionTextView;

        @BindView(R.id.questions_languages_count)
        TextView questionsLanguagesCount;

        @BindView(R.id.continue_button)
        TextView continueButton;

        View view;

        Survey survey;

        CustomViewHolder(View itemView) {

            super(itemView);

            view = itemView;

            ButterKnife.bind(this, view);

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

        public void bind(Survey survey) {

            this.survey = survey;

            continueButton.setVisibility(View.INVISIBLE);

            continueButton.setText(context.getString(R.string.continue_survey));

            itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.very_light_gray));

            titleTextView.setText(survey.title.toString());

            questionsLanguagesCount.setText(context.getString(R.string.questions_languages_count, survey.surveyQuestionList.size(), survey.getLanguages().size()));

            if(survey.description != null) {

                descriptionTextView.setText(survey.description.toString());
            }

            if(survey.attempted) {

                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue));

                continueButton.setVisibility(View.VISIBLE);
            }

            if(survey.submitted) {

                if(survey.sent) {

                    itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_green));

                } else {

                    itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_yellow));

                    continueButton.setText(context.getString(R.string.waiting_for_network));
                }
            }
        }

        @OnClick(R.id.continue_button) void continueSurvey() {

            if(survey.submitted && !survey.sent) {

                Toast.makeText(context, "Trying to send again", Toast.LENGTH_SHORT).show();

                // todo: listen to changes in network configuration to retry automatically
            }
        }
    }
}
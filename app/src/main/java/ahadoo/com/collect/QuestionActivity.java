package ahadoo.com.collect;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import ahadoo.com.collect.adapter.QuestionViewPager;
import ahadoo.com.collect.fragment.ChoiceFragment;
import ahadoo.com.collect.fragment.CurrencyFragment;
import ahadoo.com.collect.fragment.DateFragment;
import ahadoo.com.collect.fragment.ImageFragment;
import ahadoo.com.collect.fragment.LocationFragment;
import ahadoo.com.collect.fragment.NumberFragment;
import ahadoo.com.collect.fragment.NumberRangeFragment;
import ahadoo.com.collect.fragment.SubmitSurveyFragment;
import ahadoo.com.collect.fragment.TextFragment;
import ahadoo.com.collect.model.SurveyDao;
import ahadoo.com.collect.model.SurveyGroup;
import ahadoo.com.collect.model.SurveyQuestion;
import ahadoo.com.collect.model.Survey;
import ahadoo.com.collect.util.CollectDatabaseHelpers;
import ahadoo.com.collect.util.CollectHelpers;
import ahadoo.com.collect.util.OnAlertResponse;
import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static final String SURVEY_ID_IDENTIFIER = "surveyId";

    public static final String SURVEY_TITLE_IDENTIFIER = "surveyTitle";

    public static final String SAVED_RESPONSE_IDENTIFIER = "savedResponse";

    @BindView(R.id.question_list_viewpager)
    QuestionViewPager mPager;

    ScreenSlidePagerAdapter screenSlidePagerAdapter;

    @BindView(R.id.btn_previous)
    ImageView btnPrevious;

    @BindView(R.id.send)
    ImageView btnSend;

    @BindView(R.id.btn_next)
    ImageView btnNext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static Survey survey;

    AlertDialog closeDialog = null;

    private List<String> visibleQuestionUUIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPager.setSwipeable(false);

        Bundle bundles = getIntent().getExtras();

        String surveyID = bundles.getString(SURVEY_ID_IDENTIFIER);

        survey = ((CollectApplication) getApplicationContext()).getSurveyDao().load(surveyID);

        /* If the user is editing an already attempted survey
         * load the questions along with their attempted choices
         * else
         * load just the questions
         * survey.surveyQuestionList
         */

        mPager.addOnPageChangeListener(this);

        //surveyTitle = getIntent().getExtras().getString("surveyTitle");
        getSupportActionBar().setTitle(survey.getTitle().toString());

        mPager.setOffscreenPageLimit(1);

        screenSlidePagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(screenSlidePagerAdapter);

        setUpButtonVisibility();

        /*if(!canShowQuestion() && mPager.getCurrentItem() < survey.getSurveyQuestionList().size()) nextQuestion();*/

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPager.getCurrentItem() < survey.surveyQuestionList.size()) {

                    SurveyQuestion currentQuestion = survey.getSurveyQuestionList().get(mPager.getCurrentItem());

                    if (currentQuestion.validate()) {

                        nextQuestion();

                    } else {

                        Toast.makeText(QuestionActivity.this, getString(R.string.question_required), Toast.LENGTH_SHORT).show();
                    }

                    /*if(!canShowQuestion() && mPager.getCurrentItem() < survey.getSurveyQuestionList().size()) {

                        nextQuestion();
                    }*/

                } else {

                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);

                    screenSlidePagerAdapter.notifyDataSetChanged();
                }

                setUpButtonVisibility();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                previousQuestion();

                /*if(!canShowQuestion() && mPager.getCurrentItem() > 0) {

                    previousQuestion();
                }*/

                setUpButtonVisibility();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CollectHelpers.showAlertDialog(QuestionActivity.this, getString(R.string.confirm_submit_survey), new OnAlertResponse() {

                    @Override
                    public void onPositiveResponse() {

                        SurveyDao surveyDao = ((CollectApplication) getApplicationContext()).getSurveyDao();

                        Database db = surveyDao.getDatabase();

                        db.beginTransaction();

                        try {

                            survey.submitted = true;

                            surveyDao.update(survey);

                            db.setTransactionSuccessful();

                        } catch(Exception e) {
                        } finally {
                            db.endTransaction();
                        }

                        Toast.makeText(QuestionActivity.this, "Sending survey response", Toast.LENGTH_SHORT).show();

                        finish();
                    }

                    @Override
                    public void onNegativeResponse() {

                    }
                });
            }
        });
    }

    private boolean canShowQuestion() {

        SurveyQuestion question = survey.getSurveyQuestionList().get(mPager.getCurrentItem());

        SurveyGroup group = CollectDatabaseHelpers.getGroupByUUID(QuestionActivity.this, question.groupUUID, question.surveyUUID);

        return !group.aggregateCondition.resolve(QuestionActivity.this) || !question.aggregateCondition.resolve(QuestionActivity.this);
    }

    private void nextQuestion() {

        screenSlidePagerAdapter.notifyDataSetChanged();

        mPager.setCurrentItem(mPager.getCurrentItem() + 1);

        List<SurveyQuestion> questions = survey.getSurveyQuestionList();

        if(mPager.getCurrentItem() < questions.size()) {

            SurveyQuestion nextQuestion = questions.get(mPager.getCurrentItem());

            if(!visibleQuestionUUIDs.contains(nextQuestion.uuid)) {

                nextQuestion();
            }
        }
    }

    private void previousQuestion() {

        int currentPosition = mPager.getCurrentItem();

        if (currentPosition > 0) {

            mPager.setCurrentItem(currentPosition - 1);

            screenSlidePagerAdapter.notifyDataSetChanged();
        }
    }

    public void setUpButtonVisibility() {

        btnPrevious.setVisibility(View.INVISIBLE);

        btnNext.setVisibility(View.INVISIBLE);

        btnSend.setVisibility(View.INVISIBLE);

        int currentPage = mPager.getCurrentItem();

        int totalPages = survey.getSurveyQuestionList().size() + 1;

        if (currentPage > 0) {

            btnPrevious.setVisibility(View.VISIBLE);
        }

        if (currentPage < totalPages - 1) {

            btnNext.setVisibility(View.VISIBLE);
        }

        if (currentPage == totalPages - 1) {

            btnSend.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {

        mPager.setCurrentItem(position);

        visibleQuestionUUIDs = survey.getVisibleQuestionUUIDs(this);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position < survey.surveyQuestionList.size()) {

                SurveyQuestion question = survey.getSurveyQuestionList().get(position);

                if (!question.validate()) {

                    question.resetResponse();
                }

                switch (question.questionType) {

                    case TEXT:

                        return TextFragment.getInstance(question);

                    case CHOOSE_ANY:

                        return ChoiceFragment.getInstance(question, true);

                    case CHOOSE_ONE:

                        return ChoiceFragment.getInstance(question, false);

                    case CURRENCY:

                        return CurrencyFragment.getInstance(question);

                    case DATE:

                        return DateFragment.getInstance(question);

                    case IMAGE:

                        return ImageFragment.getInstance(question);

                    case LOCATION:

                        return LocationFragment.getInstance(question);

                    case NUMBER:

                        return NumberFragment.getInstance(question);

                    case NUMBER_RANGE:

                        return NumberRangeFragment.getInstance(question);
                }
            }

            return SubmitSurveyFragment.getInstance();
        }

        @Override
        public int getCount() {
            return survey.getSurveyQuestionList().size() + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return position < survey.surveyQuestionList.size() ?

                    survey.surveyQuestionList.get(position).text.toString() :

                    "";
        }


        @Override
        public int getItemPosition(Object object) {
            // return super.getItemPosition(object);
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

    }

    @Override
    public void onBackPressed() {

        CollectHelpers.showAlertDialog(this, getString(R.string.confirm_close_survey), new OnAlertResponse() {
            @Override
            public void onPositiveResponse() {
                finish();
            }

            @Override
            public void onNegativeResponse() {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

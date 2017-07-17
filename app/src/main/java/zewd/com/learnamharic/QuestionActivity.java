package zewd.com.learnamharic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import zewd.com.learnamharic.fragment.AllQuestionsFragment;
import zewd.com.learnamharic.fragment.ExamSummaryFragment;
import zewd.com.learnamharic.fragment.QuestionFragment;
import zewd.com.learnamharic.model.Exam;
import zewd.com.learnamharic.model.ExamSession;
import zewd.com.learnamharic.model.Question;
import zewd.com.learnamharic.view.CustomViewPager;
import zewd.com.learnamharic.view.ExamOverDialogFragment;
import zewd.com.learnamharic.view.ProgressIndicatorView;
import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.BELOW;

public class QuestionActivity extends AppCompatActivity {

    private CustomViewPager viewPager;

    private ImageView previousButton;

    private ImageView flagButton;

    private ImageView nextButton;

    private ImageView showAllQuestionsButton;

    private ProgressIndicatorView progressIndicatorView;

    private LinearLayout navigationContainer;

    private TextView timerTextView;

    private List<Question> questions;

    private ExamSession session;

    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        viewPager = (CustomViewPager) findViewById(R.id.view_pager);

        timerTextView = (TextView) findViewById(R.id.timer);

        previousButton = (ImageView) findViewById(R.id.navigation_previous);

        flagButton = (ImageView) findViewById(R.id.navigation_stop);

        nextButton = (ImageView) findViewById(R.id.navigation_next);

        showAllQuestionsButton = (ImageView) findViewById(R.id.navigation_home);

        progressIndicatorView = (ProgressIndicatorView) findViewById(R.id.progress_indicator);

        navigationContainer = (LinearLayout) findViewById(R.id.navigation_container);

        setSupportActionBar(toolbar);

        try {

            session = ExamSession.getInstance(this, 1);

        } catch (ExamSession.ExamDoesNotExistException e) {

            e.printStackTrace();

            Toast.makeText(this, "Exam not found", Toast.LENGTH_SHORT).show();

            finish();

            return;
        }

        myHandler = new MyHandler();

        session.start(myHandler);

        questions = session.getQuestions();

        progressIndicatorView.setTotal(questions.size());

        progressIndicatorView.setScore(1);

        QuestionsPagerAdapter adapter = new QuestionsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                navigationContainer.setVisibility(View.VISIBLE);

                progressIndicatorView.setVisibility(View.VISIBLE);

                if(position == 0) {

                    moveNavigationContainerToTop();

                    showAllQuestionsButton.setImageResource(R.drawable.ic_clear_black_24dp);

                } else if(position == questions.size() + 1) {

                    navigationContainer.setVisibility(View.GONE);

                    progressIndicatorView.setVisibility(View.GONE);

                } else {

                    session.setCurrent(position - 1);

                    moveNavigationContainerToBottom();

                    showAllQuestionsButton.setImageResource(R.drawable.ic_menu_black_24dp);

                    progressIndicatorView.setScore(position);
                }

                previousButton.setColorFilter(ContextCompat.getColor(QuestionActivity.this, R.color.darkerBluishGrey));

                nextButton.setColorFilter(ContextCompat.getColor(QuestionActivity.this, R.color.darkerBluishGrey));

                if(session.getCurrent() == 0) {

                    previousButton.setColorFilter(ContextCompat.getColor(QuestionActivity.this, R.color.bluishGrey));
                }

                if(session.getCurrent() == questions.size() - 1) {

                    nextButton.setColorFilter(ContextCompat.getColor(QuestionActivity.this, R.color.bluishGrey));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(session.getCurrent() + 1);

        previousButton.setColorFilter(ContextCompat.getColor(QuestionActivity.this, R.color.bluishGrey));
    }

    private void moveNavigationContainerToTop() {

        RelativeLayout.LayoutParams navigationContainerParams = (RelativeLayout.LayoutParams) navigationContainer.getLayoutParams();

        navigationContainerParams.addRule(BELOW, progressIndicatorView.getId());

        navigationContainerParams.addRule(ALIGN_PARENT_BOTTOM, 0);

        navigationContainer.setLayoutParams(navigationContainerParams);

        RelativeLayout.LayoutParams viewPagerParams = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();

        viewPagerParams.addRule(BELOW, navigationContainer.getId());

        viewPager.setLayoutParams(viewPagerParams);
    }

    private void moveNavigationContainerToBottom() {

        ViewGroup parent = (ViewGroup) navigationContainer.getParent();

        if(null != parent) {

            parent.removeView(navigationContainer);

            parent.removeView(viewPager);

            RelativeLayout.LayoutParams viewPagerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            viewPagerParams.addRule(BELOW, progressIndicatorView.getId());

            parent.addView(viewPager, viewPagerParams);

            RelativeLayout.LayoutParams navigationContainerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            navigationContainerParams.addRule(ALIGN_PARENT_BOTTOM);

            parent.addView(navigationContainer, navigationContainerParams);
        }
    }

    private class QuestionsPagerAdapter extends FragmentPagerAdapter {

        public QuestionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if(position == 0) {

                return new AllQuestionsFragment();

            } else if(position == questions.size() + 1) {

                return ExamSummaryFragment.getInstance(session);
            }

            return QuestionFragment.newInstance(position - 1);
        }

        @Override
        public int getCount() {
            return QuestionActivity.this.questions.size() + 2;
        }
    }

    public void nextQuestion(View view) {
        int current = session.getCurrent();
        if(current < questions.size() - 1) {
            viewPager.setCurrentItem(session.getCurrent() + 2);
        }
    }

    public void previousQuestion(View view) {
        int current = session.getCurrent();
        if(current != 0) {
            viewPager.setCurrentItem(current);
        }
    }

    public void showAllQuestions(View view) {
        if(viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setCurrentItem(session.getCurrent() + 1);
        }
    }

    public void scrollViewPagerToPosition(int i) {
        viewPager.setCurrentItem(i);
    }

    public void scrollViewPagerToLastPosition() {
        viewPager.setCurrentItem(session.getQuestions().size() + 1);
    }

    @Override
    public void onBackPressed() {
        if(!session.isSubmitted()) {
            Toast.makeText(this, "Pressing the back button will discard your progress on this session of the exam", Toast.LENGTH_SHORT).show();
            return;
        }
        finish();
    }

    public void restartExam() {

        if(myHandler != null) {

            timerTextView.setTextSize(14);

            timerTextView.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.bluishGrey));

            session.restartExam(myHandler);
        }
    }

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case ExamSession.WARN:

                    Toast.makeText(QuestionActivity.this, getString(R.string.about_to_end), Toast.LENGTH_SHORT).show();

                    timerTextView.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.red));

                    break;

                case ExamSession.OVER:

                    ExamOverDialogFragment dialogFragment = new ExamOverDialogFragment();

                    dialogFragment.setCancelable(false);

                    dialogFragment.show(getSupportFragmentManager(), QuestionActivity.class.getSimpleName());

                    /*new AlertDialog.Builder(QuestionActivity.this)

                            .setMessage(getString(R.string.time_up))

                            .setPositiveButton(getString(R.string.retake_exam), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    timerTextView.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.bluishGrey));

                                    session.restartExam(myHandler);
                                }
                            })

                            .setNegativeButton(getString(R.string.see_results), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    session.stop();

                                    scrollViewPagerToLastPosition();
                                }
                            })

                            .setCancelable(false)

                            .show();*/

                    break;

                case ExamSession.PROGRESS:

                    timerTextView.setText(((ExamSession.TimeUnit) msg.obj).getTimeString(QuestionActivity.this));

                    break;

                case ExamSession.RESTART:

                    timerTextView.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.bluishGrey));

                    break;
            }
        }
    }

    public void retakeExam() {

        if(myHandler != null) {

            timerTextView.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.bluishGrey));

            session.restartExam(myHandler);

            scrollViewPagerToPosition(1);
        }
    }

    public void showResults() {

        stopExam(null);

        scrollViewPagerToLastPosition();
    }

    public void stopExam(View view) {

        session.stop();

        scrollViewPagerToLastPosition();

        timerTextView.setText(getString(R.string.reviewing));

        timerTextView.setTextSize(10);

        timerTextView.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.red));
    }
}

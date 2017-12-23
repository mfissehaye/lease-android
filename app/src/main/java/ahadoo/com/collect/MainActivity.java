package ahadoo.com.collect;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

import ahadoo.com.collect.adapter.SurveyAdapter;
import ahadoo.com.collect.client.SurveyClient;
import ahadoo.com.collect.model.Survey;
import ahadoo.com.collect.model.SurveyDao;
import ahadoo.com.collect.model.SurveyResults;
import ahadoo.com.collect.util.CollectDatabaseHelpers;
import ahadoo.com.collect.util.CollectHelpers;
import ahadoo.com.collect.util.OnAlertResponse;
import ahadoo.com.collect.util.RetrofitServiceGenerator;
import ahadoo.com.collect.util.UserPreferencesManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SHOWING_ANSWERS_IDENTIFIER = "showing-answers";
    private SurveyAdapter surveyAdapter;

    List<Survey> surveys;

    CollectApplication application;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.content_main)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.surveys_recycler)
    RecyclerView surveysRecyclerView;

    private boolean showingAnswers = false;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

            showingAnswers = savedInstanceState.getBoolean(SHOWING_ANSWERS_IDENTIFIER, false);
        }

        setContentView(R.layout.activity_main_tab);

        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        toolbar.setTitle(getString(R.string.app_name));

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(

                this,

                drawer,

                toolbar,

                R.string.navigation_drawer_open,

                R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        application = (CollectApplication) getApplicationContext();

        surveys = new ArrayList<>();

        // setup surveys recycler view
        surveysRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        surveyAdapter = new SurveyAdapter(this, surveys);

        surveysRecyclerView.setAdapter(surveyAdapter);

        fetchSurveys();

        if (showingAnswers) {

            loadSubmittedSurveys();

        } else {

            loadSurveys();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchSurveys();

                if (showingAnswers) {

                    loadSubmittedSurveys();

                } else {

                    loadSurveys();
                }
            }
        });
    }

    public void loadSurveys() {

        surveys.clear();

        List<Survey> nonSubmittedSurveys = application.getSurveyDao()

                .queryBuilder()

                .where(SurveyDao.Properties.Submitted.eq(false))

                .list();

        surveys.addAll(nonSubmittedSurveys);

        surveyAdapter.notifyDataSetChanged();
    }

    public void loadSubmittedSurveys() {

        surveys.clear();

        List<Survey> submittedSurveys = application.getSurveyDao()

                .queryBuilder()

                .where(SurveyDao.Properties.Submitted.eq(true))

                .list();

        surveys.addAll(submittedSurveys);

        surveyAdapter.notifyDataSetChanged();
    }

    public void fetchSurveys() {

        progressBar.setVisibility(View.VISIBLE);

        SurveyClient client = RetrofitServiceGenerator.createService(SurveyClient.class, UserPreferencesManager.getUserToken(this));

        client.all().enqueue(new Callback<SurveyResults>() {

            @Override
            public void onResponse(Call<SurveyResults> call, Response<SurveyResults> response) {

                progressBar.setVisibility(View.GONE);

                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful()) {

                    // Do not touch responses and submitted states while inserting

                    List<Survey> surveys = response.body().surveys;

                    SurveyDao surveyDao = application.getSurveyDao();

                    Database db = surveyDao.getDatabase();

                    db.beginTransaction();

                    try {

                        for (Survey survey : surveys) {

                            if (surveyDao.load(survey.getUuid()) == null) {

                                surveyDao.insertOrReplaceInTx(survey);
                            }
                        }

                        db.setTransactionSuccessful();

                    } catch (Exception e) {
                    } finally {
                        db.endTransaction();
                    }
                }

                surveyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SurveyResults> call, Throwable t) {

                swipeRefreshLayout.setRefreshing(false);

                progressBar.setVisibility(View.GONE);

                Toast.makeText(MainActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.nav_survey:

                showingAnswers = false;

                loadSurveys();

                break;

            case R.id.nav_answer:

                showingAnswers = true;

                Toast.makeText(application, getString(R.string.showing_responses), Toast.LENGTH_SHORT).show();

                loadSubmittedSurveys();

                break;

            case R.id.nav_logout:

                CollectHelpers.showAlertDialog(this, getString(R.string.confirm_logout), new OnAlertResponse() {
                    @Override
                    public void onPositiveResponse() {

                        UserPreferencesManager.clearPreference(MainActivity.this);

                        CollectDatabaseHelpers.clearDatabase(MainActivity.this);

                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);

                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(loginIntent);
                    }

                    @Override
                    public void onNegativeResponse() {

                    }
                });
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (surveyAdapter != null) {

            if (showingAnswers) {

                loadSubmittedSurveys();

            } else {

                loadSurveys();
            }

            surveyAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(SHOWING_ANSWERS_IDENTIFIER, showingAnswers);
    }
}

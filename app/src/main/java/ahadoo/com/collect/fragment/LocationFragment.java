package ahadoo.com.collect.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ahadoo.com.collect.QuestionActivity;
import ahadoo.com.collect.R;
import ahadoo.com.collect.model.SurveyQuestion;
import ahadoo.com.collect.util.GPSTracker;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private SurveyQuestion question;

    private GPSTracker gpsTracker;

    @BindView(R.id.question)
    TextView questionTitleTextView;

    @BindView(R.id.get_location_button)
    Button getLocationButton;

    @BindView(R.id.location)
    TextView locationTextView;

    public static LocationFragment getInstance(SurveyQuestion question) {

        LocationFragment fragment = new LocationFragment();

        fragment.question = question;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            question.response = savedInstanceState.getString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER);
        }

        View view = inflater.inflate(R.layout.single_question_layout_location, container, false);

        ButterKnife.bind(this, view);

        questionTitleTextView.setText(question.text.toString());

        locationTextView.setText(question.response);

        if (requestLocationPermissions()) {

            gpsTracker = new GPSTracker(getContext());
        }

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Location location = getLocation();

                if (location != null) {

                    question.response = String.format(

                            getString(R.string.location_text),

                            location.getLatitude(),

                            location.getLongitude());

                    locationTextView.setText(question.response);
                }
            }
        });

        return view;
    }

    private Location getLocation() {

        return gpsTracker != null ? gpsTracker.getLocation() : null;
    }

    private boolean checkPermissions() {

        return ActivityCompat.checkSelfPermission(

                getContext(), Manifest.permission.ACCESS_FINE_LOCATION)

                != PackageManager.PERMISSION_GRANTED


                &&

                ActivityCompat.checkSelfPermission(

                        getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)

                        != PackageManager.PERMISSION_GRANTED;
    }

    private boolean requestLocationPermissions() {

        if (Build.VERSION.SDK_INT > 23) {

            if (checkPermissions()) {

                requestPermissions(new String[]{

                        Manifest.permission.ACCESS_FINE_LOCATION,

                        Manifest.permission.ACCESS_COARSE_LOCATION,

                }, LOCATION_PERMISSION_REQUEST_CODE);

                return false;
            }

        } else if (!gpsTracker.GPSEnabled()) {

            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }


        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {

            case LOCATION_PERMISSION_REQUEST_CODE:

                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER, question.response);
    }
}

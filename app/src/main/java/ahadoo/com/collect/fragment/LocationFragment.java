package ahadoo.com.collect.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ahadoo.com.collect.QuestionActivity;
import ahadoo.com.collect.R;
import ahadoo.com.collect.model.SurveyQuestion;
import ahadoo.com.collect.util.CollectLocation;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private SurveyQuestion question;

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

        if (savedInstanceState != null) {

            question.response = savedInstanceState.getString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER);
        }

        View view = inflater.inflate(R.layout.single_question_layout_location, container, false);

        ButterKnife.bind(this, view);

        questionTitleTextView.setText(question.text.toString());

        locationTextView.setText(question.response);

        QuestionActivity parentActivity = (QuestionActivity) getActivity();

        if(parentActivity != null && parentActivity.isReviewing()) {

            getLocationButton.setVisibility(View.GONE);
        }

        getLocationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (checkLocationPermissions()) {

                    locationTextView.setText(getString(R.string.getting_location));

                    (new CollectLocation()).getLocation(getContext(), new CollectLocation.CollectLocationResult() {

                        public void gotLocation(Location location) {

                            if(location != null) {

                                question.response = String.format(Locale.getDefault(),"%1$f,%2$f", location.getLatitude(), location.getLongitude()); // getString(R.string.location_text, location.getLatitude(), location.getLongitude());

                                locationTextView.setText(question.response);

                            } else {

                                locationTextView.setText(getString(R.string.enable_location));

                                Toast.makeText(getContext(), "Unable to get location information", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        }
                    });

                } else {

                    Toast.makeText(getContext(), "Location Permission is not granted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean checkLocationPermissions() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            return true;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.title_location_permission)
                    .setMessage(R.string.text_location_permission)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    LOCATION_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .create()
                    .show();
        } else {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER, question.response);
    }
}

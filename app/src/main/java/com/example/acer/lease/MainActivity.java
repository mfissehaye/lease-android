package com.example.acer.lease;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<User> users = new ArrayList<>();
    Retrofit retrofit;
    private LeaseService service;
    private Spinner pickNameDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickNameDropdown = (Spinner) findViewById(R.id.pick_name);

//        options.add(getString(R.string.loading_names));
        final CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(MainActivity.this, android.R.layout.simple_spinner_item, users);
        pickNameDropdown.setAdapter(adapter);
        pickNameDropdown.setEnabled(false);
        pickNameDropdown.setOnItemSelectedListener(this);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(LeaseService.class);

        Call<List<User>> call = service.getNames();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    adapter.clear();
                    users.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    pickNameDropdown.setEnabled(true);
                } else {
                    Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final User user = users.get(position);
        if(service != null) {
            Call<LeaseDescription> call = service.getDescription(user.id);
            pickNameDropdown.setEnabled(false);
            call.enqueue(new Callback<LeaseDescription>() {
                @Override
                public void onResponse(Call<LeaseDescription> call, Response<LeaseDescription> response) {
                    pickNameDropdown.setEnabled(true);

                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setCancelable(true)
                            .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();

                    if(response.isSuccessful()) {
                        dialog.setTitle("Description fot user - " + user.name);
                        dialog.setMessage(response.body().description);
                        dialog.show();
                        Toast.makeText(MainActivity.this, "Description is: " + response.body().description, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LeaseDescription> call, Throwable t) {
                    pickNameDropdown.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Unable to get description", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

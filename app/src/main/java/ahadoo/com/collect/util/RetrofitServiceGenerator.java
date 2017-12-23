package ahadoo.com.collect.util;

import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

import ahadoo.com.collect.model.Survey;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceGenerator {

    public static final String APP_BASE_URL = "http://collect.ahadootec.info/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =

            new Retrofit.Builder()
                    .baseUrl(APP_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(/*gson*/));

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(logging);

        httpClient.addNetworkInterceptor(new StethoInterceptor());

        if(!TextUtils.isEmpty(authToken)) {

            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

            if(!httpClient.interceptors().contains(interceptor)) {

                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());

                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }
}

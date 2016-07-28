package com.remirobert.remirobert.signalstrentgh;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by remirobert on 20/07/16.
 */
public class CellIdService {

    public static final String API_BASE_URL = "http://opencellid.org";

       private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

       private static Retrofit.Builder builder =
               new Retrofit.Builder()
                       .baseUrl(API_BASE_URL)
                       .addConverterFactory(GsonConverterFactory.create());

       public static <S> S createService(Class<S> serviceClass) {
           Retrofit retrofit = builder.client(httpClient.build()).build();
           return retrofit.create(serviceClass);
       }
}

package com.remirobert.remirobert.signalstrentgh;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by remirobert on 09/08/16.
 */
public interface RecordClient {
    @POST("http://remirobert.com:8080/record")
    Call<Void> postNewRecord(@Body JRecord record);
}

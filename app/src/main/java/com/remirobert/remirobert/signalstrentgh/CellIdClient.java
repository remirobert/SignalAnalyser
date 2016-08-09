package com.remirobert.remirobert.signalstrentgh;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by remirobert on 20/07/16.
 */
public interface CellIdClient {

    //http://opencellid.org/cell/get?key=e5dad4a2-e436-412c-8178-064b8fef2ecc&mcc=208&mnc=15&lac=3300&cellid=104187198&format=json

    @GET("http://opencellid.org/cell/get")
    Call<CellIdResponse> cellInformations(
            @Query("key") String key,
            @Query("mcc") int mcc,
            @Query("mnc") int mnc,
            @Query("lac") int lac,
            @Query("cellid") int cellId,
            @Query("format") String format
        );
}

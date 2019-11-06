package ng.riby.androidtest.api;

import java.util.Map;

import ng.riby.androidtest.model.GetDistanceResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {

//    @GET("/maps/api/distancematrix/json")
//    Call<GetDistanceResponse> getDistance(@Query("units") String units,@Query("origins") String origins,@Query("destinations") String destinations, @Query("key") String key);

    @GET("/maps/api/distancematrix/json")
    Call<GetDistanceResponse> getDistance(@QueryMap Map<String, String> data);


}

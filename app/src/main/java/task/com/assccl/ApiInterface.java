package task.com.assccl;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    //Get Conversion rates
    @GET("/latest")
    Call<CurrencyModel.CurrencyModelResponse> getInr(
            @Query("base") String base
    );

    @GET("/SendSMS/sendmsg.php?uname=ASSCCL-OTP&pass=Assccl@17&send=ASSCCL&dest=919916007497&msg=54321%20is%20the%20One%20Time%20Password%20for%20the%20transaction%20of%20Rs.%2049999.99.%20Your%20current%20balance%20is%20Rs.%2050000000.33.%20This%20OTP%20expires%20in%2010%20minutes.%20-%20Spark%20Team")
    Call<String> getOTP();


}

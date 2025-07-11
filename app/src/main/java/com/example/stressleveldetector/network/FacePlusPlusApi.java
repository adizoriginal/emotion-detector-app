package com.example.stressleveldetector.network;

import com.example.stressleveldetector.model.FacePlusPlusResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FacePlusPlusApi {

    @Multipart
    @POST("facepp/v3/detect")
    Call<FacePlusPlusResponse> detectEmotion(
            @Part("api_key") RequestBody apiKey,
            @Part("api_secret") RequestBody apiSecret,
            @Part("return_attributes") RequestBody returnAttributes,
            @Part MultipartBody.Part image
    );
}

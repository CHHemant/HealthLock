package com.healthlock.network

import com.healthlock.models.Record
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("/api/records/upload")
    @FormUrlEncoded
    fun uploadRecord(
        @Field("patientId") patientId: String,
        @Field("file") file: String,
        @Field("accessLevel") accessLevel: String
    ): Call<Map<String, Any>>

    @GET("/api/records/{patientId}")
    fun getRecords(@Path("patientId") patientId: String): Call<List<Record>>

    @POST("/api/access/generate-token")
    @FormUrlEncoded
    fun generateToken(
        @Field("recordId") recordId: String,
        @Field("accessLevel") accessLevel: String
    ): Call<Map<String, String>>

    @POST("/api/access/access-with-token")
    @FormUrlEncoded
    fun accessWithToken(
        @Field("token") token: String,
        @Field("userId") userId: String
    ): Call<Map<String, Any>>
}

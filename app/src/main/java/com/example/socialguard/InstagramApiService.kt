package com.example.socialguard

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface InstagramApiService {
    @GET("comments")
    fun getComments(
        @Query("access_token") accessToken: String
    ): Call<CommentResponse>
}

data class CommentResponse(val data: List<Comment>)
data class Comment(val text: String)

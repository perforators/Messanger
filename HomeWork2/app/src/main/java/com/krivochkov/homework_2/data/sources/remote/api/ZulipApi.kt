package com.krivochkov.homework_2.data.sources.remote.api

import com.krivochkov.homework_2.data.sources.remote.dto.UserDto
import com.krivochkov.homework_2.data.sources.remote.responses.*
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface ZulipApi {

    @GET("streams")
    fun getAllChannels(): Single<AllChannelsResponse>

    @GET("users/me/subscriptions")
    fun getSubscribedChannels(): Single<SubscribedChannelsResponse>

    @GET("users/me/{stream_id}/topics")
    fun getTopicsInChannel(@Path("stream_id") channelId: Long): Single<TopicsResponse>

    @GET("users")
    fun getAllUsers(): Single<UsersResponse>

    @GET("users/me")
    fun getOwnUser(): Single<UserDto>

    @GET("users/{user_email}/presence")
    fun getUserPresence(@Path("user_email") userEmail: String): Single<PresenceResponse>

    @GET("messages")
    fun getAllMessages(@QueryMap queryMap: Map<String, String>): Single<MessagesResponse>

    @POST("messages")
    fun sendMessage(@QueryMap queryMap: Map<String, String>): Completable

    @POST("messages/{message_id}/reactions")
    fun addReaction(
        @Path("message_id") messageId: Long,
        @Query("emoji_name") emojiName: String
    ): Completable

    @DELETE("messages/{message_id}/reactions")
    fun removeReaction(
        @Path("message_id") messageId: Long,
        @Query("emoji_name") emojiName: String
    ): Completable

    @Multipart
    @POST("user_uploads")
    fun uploadFile(
        @Part file: MultipartBody.Part,
    ): Single<FileResponse>
}
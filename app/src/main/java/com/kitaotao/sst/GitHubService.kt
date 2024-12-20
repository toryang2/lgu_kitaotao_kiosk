package com.kitaotao.sst.network // Use your appropriate package name

import com.kitaotao.sst.model.Release
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query // Import Query for parameters

interface GitHubService {
    @GET("repos/{owner}/{repo}/releases/latest")
    suspend fun getLatestRelease(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Release

    // Update the method to include pagination parameters
    @GET("repos/{owner}/{repo}/releases")
    suspend fun getReleases(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int,          // Add page parameter
        @Query("per_page") perPage: Int    // Add per_page parameter
    ): List<Release>
}

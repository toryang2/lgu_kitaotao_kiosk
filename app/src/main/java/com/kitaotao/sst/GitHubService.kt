package com.kitaotao.sst.network // Use your appropriate package name

import com.kitaotao.sst.model.Release
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("repos/{owner}/{repo}/releases/latest")
    suspend fun getLatestRelease(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Release
}

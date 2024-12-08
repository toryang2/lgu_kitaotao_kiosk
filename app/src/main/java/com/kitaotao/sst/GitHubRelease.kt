package com.kitaotao.sst.model // Use your appropriate package name

data class Release(
    val tag_name: String, // e.g. "v1.0.0"
    val assets: List<Asset>,
    val body: String
)

data class Asset(
    val name: String, // e.g. "app-release.apk"
    val browser_download_url: String // URL to the APK
)

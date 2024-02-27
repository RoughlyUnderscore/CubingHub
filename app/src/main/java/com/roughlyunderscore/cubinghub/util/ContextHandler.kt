package com.roughlyunderscore.cubinghub.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import coil.ImageLoader
import coil.disk.DiskCache
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class ContextHandler @Inject constructor(@ApplicationContext private val ctx: Context) {
  val imageLoader = ImageLoader.Builder(ctx)
    .memoryCache { MemoryCache.Builder(ctx).maxSizePercent(0.3).build() }
    .diskCache { DiskCache.Builder().directory(retrieveDir().resolve("imgcache")).maxSizePercent(0.03).build() }
    .build()

  fun getImageRequestBuilder() = ImageRequest.Builder(ctx)

  fun startActivity(intent: Intent) = ctx.startActivity(intent)

  fun retrieveDir(): File = ctx.filesDir

  fun resolveContent(uri: Uri?) = uri?.let { ctx.contentResolver.openInputStream(it) }

  fun self() = ctx
}
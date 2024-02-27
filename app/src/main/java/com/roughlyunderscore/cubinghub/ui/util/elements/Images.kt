package com.roughlyunderscore.cubinghub.ui.util.elements

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.roughlyunderscore.cubinghub.util.ContextHandler
import org.apache.commons.io.IOUtils

/**
 * Asynchronously loads an image from a file in [assetManager] (using a [path]) with the
 * help of [ctxHandler]'s [coil.imageLoader], then displays it with [contentScale]
 * and description [contentDescription]. Applies [modifier] and [colorFilter] to the image.
 */
@Composable
fun AsyncImageFromFile(
  modifier: Modifier? = Modifier,
  assetManager: AssetManager,
  path: String,
  contentDescription: String = "",
  ctxHandler: ContextHandler,
  contentScale: ContentScale = ContentScale.None,
  colorFilter: ColorFilter? = null
) {
  AsyncImage(
    model = ctxHandler.getImageRequestBuilder().data(assetManager.open(path).use(IOUtils::toByteArray)).build(),
    contentDescription = contentDescription,
    imageLoader = ctxHandler.imageLoader,
    contentScale = contentScale,
    modifier = modifier ?: Modifier,
    colorFilter = colorFilter
  )
}

/**
 * Asynchronously loads an image from a url (using a [url]) with the
 * help of [coil.imageLoader], then displays it with [contentScale]
 * and description [contentDescription]. Applies [modifier] and [colorFilter] to the image.
 */
@Composable
fun AsyncImageFromLink(
  modifier: Modifier? = Modifier,
  ctxHandler: ContextHandler,
  url: String,
  contentDescription: String = "",
  contentScale: ContentScale = ContentScale.None,
  colorFilter: ColorFilter? = null
) {
  val loader = ctxHandler.imageLoader

  AsyncImage(
    model = ctxHandler.getImageRequestBuilder().data(url).build(),
    contentDescription = contentDescription,
    imageLoader = loader,
    contentScale = contentScale,
    modifier = modifier ?: Modifier,
    colorFilter = colorFilter
  )
}

/**
 * Asynchronously loads an image from a bitmap (using a [bitmap]) with the
 * help of [coil.imageLoader], then displays it with [contentScale]
 * and description [contentDescription]. Applies [modifier] and [colorFilter] to the image.
 */
@Composable
fun AsyncImageFromBitmap(
  modifier: Modifier? = Modifier,
  ctxHandler: ContextHandler,
  bitmap: Bitmap,
  contentDescription: String = "",
  contentScale: ContentScale = ContentScale.None,
  colorFilter: ColorFilter? = null
) {
  val loader = ctxHandler.imageLoader
  AsyncImage(
    model = ctxHandler.getImageRequestBuilder().data(bitmap).build(),
    contentDescription = contentDescription,
    imageLoader = loader,
    contentScale = contentScale,
    modifier = modifier ?: Modifier,
    colorFilter = colorFilter,

  )
}

/**
 * Synchronously loads an image from a file in [assetManager] (using a [path]), then displays it
 * with [contentScale] and description [contentDescription]. Applies [modifier] and
 * [colorFilter] to the image.
 */
@Composable
fun SyncImageFromFile(
  modifier: Modifier? = Modifier,
  assetManager: AssetManager,
  path: String,
  contentDescription: String = "",
  contentScale: ContentScale = ContentScale.None,
  colorFilter: ColorFilter? = null
) {
  assetManager.open(path).use { stream ->
    val bitmap = BitmapFactory.decodeStream(stream).asImageBitmap()
    Image(
      bitmap = bitmap,
      contentDescription = contentDescription,
      contentScale = contentScale,
      modifier = modifier ?: Modifier,
      colorFilter = colorFilter
    )
  }
}
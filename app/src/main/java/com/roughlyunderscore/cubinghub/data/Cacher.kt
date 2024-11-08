package com.roughlyunderscore.cubinghub.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.core.graphics.scale
import com.roughlyunderscore.cubinghub.data.type.Algorithm
import com.roughlyunderscore.cubinghub.data.type.Subset
import com.roughlyunderscore.cubinghub.ui.util.elements.AsyncImageFromBitmap
import com.roughlyunderscore.cubinghub.ui.util.elements.AsyncImageFromLink
import com.roughlyunderscore.cubinghub.ui.util.elements.subsetImageModifier
import com.roughlyunderscore.cubinghub.util.ContextHandler
import com.roughlyunderscore.cubinghub.util.normalize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.io.FileOutputStream
import java.net.URL

object Cacher {
  private fun createAlgorithmName(subsetName: String, algorithm: Algorithm) = "${subsetName}_${algorithm.name}_${System.currentTimeMillis()}".normalize() + ".png"
  private fun createSubsetName(subset: Subset) = "${subset.name}_${System.currentTimeMillis()}".normalize() + ".png"

  private fun saveBitmap(bitmap: Bitmap, fileName: String, ctx: Context, width: Int, height: Int) = FileOutputStream(File(ctx.filesDir, fileName)).use {
    bitmap.scale(width, height)
    bitmap.compress(Bitmap.CompressFormat.PNG, 70, it)
    it.flush()
  }

  suspend fun cacheSubset(serverLoadedSubset: Subset, cachedSubset: Subset?, ctx: Context) = coroutineScope {
    return@coroutineScope serverLoadedSubset.also {
      val differentUrl = cachedSubset?.imageUrl == it.imageUrl
      if (differentUrl) {
        val bitmap = fetchBitmap(serverLoadedSubset.imageUrl) ?: return@coroutineScope null
        val fileName = createSubsetName(serverLoadedSubset)

        saveBitmap(bitmap, fileName, ctx, 84, 84)
        it.imageFileName = fileName
      }

      it.algorithms = serverLoadedSubset.algorithms
      it.name = serverLoadedSubset.name
    }
  }

  suspend fun requestSubsetAlgorithmsLoading(subset: Subset, serverAlgs: List<Algorithm>, ctx: Context) = coroutineScope {
    val cachedAlgs = subset.algorithms
    cacheAlgorithmsAsync(subset, serverAlgs.toMutableList(), cachedAlgs, ctx)
  }

  private suspend fun cacheAlgorithmsAsync(subset: Subset, serverLoadedAlgs: MutableList<Algorithm>, cachedAlgorithms: List<Algorithm>?, ctx: Context) = coroutineScope {
    return@coroutineScope async(Dispatchers.IO) {
      val algMapping = serverLoadedAlgs.associateWith { alg ->
        cachedAlgorithms?.find { it.id == alg.id }
      }

      val result = mutableListOf<Algorithm>()

      algMapping.forEach { (serverAlg, cachedAlg) ->
        if (serverAlg.imageUrl != cachedAlg?.imageUrl) {
          val name = createAlgorithmName(subset.name!!, serverAlg)
          saveAlgorithm(serverAlg, ctx, name)
          serverAlg.imageFileName = name
        } else {
          serverAlg.imageFileName = cachedAlg?.imageFileName
        }

        result.add(serverAlg)
      }

      return@async result
    }
  }

  private suspend fun saveAlgorithm(alg: Algorithm, ctx: Context, name: String) = coroutineScope {
    saveBitmap(fetchBitmap(alg.imageUrl) ?: return@coroutineScope, name, ctx, 62, 72)
  }

  private suspend fun fetchBitmap(link: String?): Bitmap? = coroutineScope {
    if (link == null) return@coroutineScope null
    val url = URL(link)
    var bitmap: Bitmap? = null

    async(Dispatchers.IO) {
      val connection = url.openConnection()
      connection.connect()
      connection.inputStream.use {
        it.buffered().use { buf -> bitmap = BitmapFactory.decodeStream(buf) }
      }
    }.await()

    return@coroutineScope bitmap
  }

  @Composable
  fun SubsetImage(ctxHandler: ContextHandler, desc: String, subset: Subset) {
    subset.imageFileName?.let {
      // imageFileName has to be resolved into an absolute path
      // before resolving it into a bitmap
      val file = File(ctxHandler.retrieveDir(), it)

      val bitmap = BitmapFactory.decodeFile(file.absolutePath) ?: return@let

      AsyncImageFromBitmap(
        ctxHandler = ctxHandler,
        bitmap = bitmap,
        contentDescription = desc,
        contentScale = ContentScale.Crop,
        modifier = Modifier.subsetImageModifier()
      )
    } ?: run {
      AsyncImageFromLink(
        url = subset.imageUrl ?: "",
        ctxHandler = ctxHandler,
        contentDescription = desc,
        contentScale = ContentScale.Crop,
        modifier = Modifier.subsetImageModifier()
      )
    }
  }

  @Composable
  fun AlgorithmImage(
    modifier: Modifier? = Modifier,
    ctxHandler: ContextHandler,
    desc: String,
    algorithm: Algorithm,
    contentScale: ContentScale = ContentScale.Inside
  ) {
    algorithm.imageFileName?.let {
      // imageFileName has to be resolved into an absolute path
      // before resolving it into a bitmap
      val file = File(ctxHandler.retrieveDir(), it)

      val bitmap = BitmapFactory.decodeFile(file.absolutePath) ?: return@let

      AsyncImageFromBitmap(
        ctxHandler = ctxHandler,
        bitmap = bitmap,
        contentDescription = desc,
        contentScale = contentScale,
        modifier = Modifier.subsetImageModifier().then(modifier ?: Modifier)
      )
    } ?: run {
      AsyncImageFromLink(
        url = algorithm.imageUrl ?: "",
        ctxHandler = ctxHandler,
        contentDescription = desc,
        contentScale = contentScale,
        modifier = Modifier.subsetImageModifier().then(modifier ?: Modifier)
      )
    }
  }
}
package com.roughlyunderscore.cubinghub.store

import android.graphics.Bitmap
import com.roughlyunderscore.cubinghub.data.type.Algorithm
import com.roughlyunderscore.cubinghub.data.type.Subset

class BitmapRepositoryRegistry {
  val algorithmBitmapRepository = AlgorithmBitmapRepository()
  val subsetBitmapRepository = SubsetBitmapRepository()
}

interface GeneralBitmapRepository<T> {
  fun saveBitmap(id: Int, bitmap: Bitmap)
  fun getBitmap(id: Int): Bitmap?
  fun deleteBitmap(id: Int)
  fun deleteAll()
}

class AlgorithmBitmapRepository : GeneralBitmapRepository<Algorithm> {
  private val bitmaps = mutableMapOf<Int, Bitmap>()

  override fun saveBitmap(id: Int, bitmap: Bitmap) {
    bitmaps[id] = bitmap
  }

  override fun getBitmap(id: Int): Bitmap? {
    return bitmaps[id]
  }

  override fun deleteBitmap(id: Int) {
    bitmaps.remove(id)
  }

  override fun deleteAll() {
    bitmaps.clear()
  }
}

class SubsetBitmapRepository : GeneralBitmapRepository<Subset> {
  private val bitmaps = mutableMapOf<Int, Bitmap>()

  override fun saveBitmap(id: Int, bitmap: Bitmap) {
    bitmaps[id] = bitmap
  }

  override fun getBitmap(id: Int): Bitmap? {
    return bitmaps[id]
  }

  override fun deleteBitmap(id: Int) {
    bitmaps.remove(id)
  }

  override fun deleteAll() {
    bitmaps.clear()
  }
}
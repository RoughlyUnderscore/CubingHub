package com.roughlyunderscore.cubinghub.util

// How is this not a standard operator?
operator fun Int?.minus(other: Int?): Int = (this ?: 0) - (other ?: 0)
package com.pragmadreams.redaktor.android.shared

import com.pragmadreams.redaktor.android.base.Effect
import kotlinx.coroutines.flow.MutableSharedFlow

object Hub {

    val effectFlow : MutableSharedFlow<Effect> by lazy {
        MutableSharedFlow()
    }

}
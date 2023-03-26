package com.pragmadreams.redaktor.android.shared

import com.pragmadreams.redaktor.android.base.Effect
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class Hub @Inject constructor() {

    val effectFlow : MutableSharedFlow<Effect> by lazy {
        MutableSharedFlow()
    }

    // serves to transfer effects between screens
    val intermediateEffectFlow : MutableSharedFlow<Effect> by lazy {
        MutableSharedFlow()
    }

}
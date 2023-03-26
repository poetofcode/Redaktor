package com.pragmadreams.redaktor.android.presentation.screen.page.misc

import com.pragmadreams.redaktor.android.base.Effect
import com.pragmadreams.redaktor.android.domain.model.PageUI

data class OnPagePickedEffect(val page: PageUI) : Effect

object OnPagesUpdatedEffect : Effect
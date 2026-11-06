package com.spicerub.ratio.core.di

import android.content.Context
import com.spicerub.ratio.data.local.BlendPreferences
import com.spicerub.ratio.data.repository.BlendRepositoryImpl
import com.spicerub.ratio.data.repository.SpiceRepositoryImpl
import com.spicerub.ratio.domain.repository.BlendRepository
import com.spicerub.ratio.domain.repository.SpiceRepository
import com.spicerub.ratio.domain.session.BlendDraftStore
import com.spicerub.ratio.domain.usecase.BuildTasteProfileUseCase
import com.spicerub.ratio.domain.usecase.FormatAmountUseCase
import com.spicerub.ratio.domain.usecase.ScaleBlendUseCase

object ServiceLocator {

    private var blendRepositoryRef: BlendRepository? = null
    private var spiceRepositoryRef: SpiceRepository? = null

    val draftStore: BlendDraftStore = BlendDraftStore()
    val formatAmount: FormatAmountUseCase = FormatAmountUseCase()
    val buildTasteProfile: BuildTasteProfileUseCase = BuildTasteProfileUseCase()
    val scaleBlend: ScaleBlendUseCase = ScaleBlendUseCase(formatAmount, buildTasteProfile)

    val blendRepository: BlendRepository
        get() = requireNotNull(blendRepositoryRef)

    val spiceRepository: SpiceRepository
        get() = requireNotNull(spiceRepositoryRef)

    fun init(context: Context) {
        if (blendRepositoryRef != null) return
        val preferences = BlendPreferences(context)
        blendRepositoryRef = BlendRepositoryImpl(preferences)
        spiceRepositoryRef = SpiceRepositoryImpl(preferences)
    }
}

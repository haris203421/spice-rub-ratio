package com.spicerub.ratio.data.sample

import com.spicerub.ratio.domain.model.Blend
import com.spicerub.ratio.domain.model.BlendItem
import com.spicerub.ratio.domain.model.MeasureUnit
import com.spicerub.ratio.domain.model.Spice
import com.spicerub.ratio.domain.model.SpiceForm
import com.spicerub.ratio.domain.model.SpiceGroup

object SampleData {

    const val DEFAULT_BLEND_NAME = "New Rub"
    const val DEFAULT_NOTES = ""

    val spices: List<Spice> = listOf(
        Spice("paprika", "Paprika", SpiceGroup.BASE, SpiceForm.GROUND, 2.3, 0xFFC34524, "SMOKY"),
        Spice("smoked_paprika", "Smoked Paprika", SpiceGroup.HEAT, SpiceForm.GROUND, 2.3, 0xFFA63C1E, "SMOKY"),
        Spice("cayenne", "Cayenne", SpiceGroup.HEAT, SpiceForm.GROUND, 1.8, 0xFFD14B22, "FIERY"),
        Spice("black_pepper", "Black Pepper", SpiceGroup.HEAT, SpiceForm.SEED, 2.3, 0xFF4A3B33, "PUNGENT"),
        Spice("cumin", "Cumin", SpiceGroup.BASE, SpiceForm.SEED, 2.1, 0xFF8A5A2B, "EARTHY"),
        Spice("coriander", "Coriander", SpiceGroup.BASE, SpiceForm.SEED, 1.8, 0xFFB98C3E, "CITRUS"),
        Spice("turmeric", "Turmeric", SpiceGroup.BASE, SpiceForm.GROUND, 3.0, 0xFFE0A526, "GOLDEN"),
        Spice("ginger", "Ginger", SpiceGroup.HEAT, SpiceForm.GROUND, 1.8, 0xFFC98A3C, "ZESTY"),
        Spice("cinnamon", "Cinnamon", SpiceGroup.BASE, SpiceForm.GROUND, 2.6, 0xFFA2552F, "WARM"),
        Spice("cardamom", "Cardamom", SpiceGroup.BASE, SpiceForm.SEED, 2.0, 0xFF6F9E57, "FLORAL"),
        Spice("clove", "Clove", SpiceGroup.BASE, SpiceForm.SEED, 2.1, 0xFF6B4226, "RESINOUS"),
        Spice("fenugreek", "Fenugreek", SpiceGroup.BASE, SpiceForm.SEED, 3.7, 0xFFB5912F, "MAPLE"),
        Spice("mustard", "Mustard Seed", SpiceGroup.HEAT, SpiceForm.SEED, 3.4, 0xFFD2A32A, "SHARP"),
        Spice("fennel", "Fennel Seed", SpiceGroup.BASE, SpiceForm.SEED, 2.0, 0xFF9CA85F, "ANISE"),
        Spice("allspice", "Allspice", SpiceGroup.BASE, SpiceForm.SEED, 2.0, 0xFF7A4A2E, "SPICED"),
        Spice("garlic", "Garlic Powder", SpiceGroup.BASE, SpiceForm.GROUND, 3.1, 0xFFD8C9A8, "SAVOURY"),
        Spice("onion", "Onion Powder", SpiceGroup.BASE, SpiceForm.GROUND, 2.4, 0xFFC9B48E, "MELLOW"),
        Spice("brown_sugar", "Brown Sugar", SpiceGroup.BASE, SpiceForm.GROUND, 4.0, 0xFF8C5A2B, "SWEET"),
        Spice("salt", "Flake Salt", SpiceGroup.SALT, SpiceForm.FLAKE, 6.0, 0xFFE8E2D4, "BRINY"),
        Spice("thyme", "Thyme", SpiceGroup.BASE, SpiceForm.FLAKE, 1.0, 0xFF6F8B4B, "HERBAL"),
        Spice("rosemary", "Rosemary", SpiceGroup.BASE, SpiceForm.FLAKE, 1.2, 0xFF5F7F4A, "PINE")
    )

    val onHandOnFirstRun: Set<String> = setOf(
        "paprika", "black_pepper", "cumin", "coriander", "garlic", "brown_sugar", "salt"
    )

    fun starterBlends(today: Long): List<Blend> = listOf(
        blend(
            id = "memphis_pork_rub",
            name = "Memphis Pork Rub",
            items = listOf(
                "paprika" to 4.0,
                "brown_sugar" to 3.0,
                "salt" to 2.0,
                "black_pepper" to 1.5,
                "garlic" to 1.0,
                "cayenne" to 0.5
            ),
            heat = 1.0,
            salt = 1.0,
            batch = 250,
            notes = "Pat onto ribs two hours before the smoker.",
            created = today - 24
        ),
        blend(
            id = "tandoori_masala",
            name = "Tandoori Masala",
            items = listOf(
                "coriander" to 3.0,
                "cumin" to 2.5,
                "paprika" to 2.0,
                "ginger" to 1.0,
                "cardamom" to 1.0,
                "cayenne" to 1.0
            ),
            heat = 1.25,
            salt = 0.75,
            batch = 100,
            notes = "Stir into yoghurt for an overnight marinade.",
            created = today - 18
        ),
        blend(
            id = "ras_el_hanout",
            name = "Ras el Hanout",
            items = listOf(
                "cumin" to 2.0,
                "coriander" to 2.0,
                "cinnamon" to 1.5,
                "ginger" to 1.0,
                "cardamom" to 1.0,
                "clove" to 0.5
            ),
            heat = 0.75,
            salt = 0.5,
            batch = 100,
            notes = "Toast the seeds before grinding.",
            created = today - 11
        ),
        blend(
            id = "jerk_dry_rub",
            name = "Jerk Dry Rub",
            items = listOf(
                "allspice" to 3.0,
                "black_pepper" to 2.0,
                "thyme" to 2.0,
                "brown_sugar" to 1.5,
                "cayenne" to 1.5,
                "salt" to 1.0
            ),
            heat = 1.75,
            salt = 1.0,
            batch = 250,
            notes = "Allspice carries this one, keep it generous.",
            created = today - 7
        ),
        blend(
            id = "garam_masala",
            name = "Garam Masala",
            items = listOf(
                "coriander" to 3.0,
                "cumin" to 2.0,
                "black_pepper" to 1.5,
                "cardamom" to 1.0,
                "cinnamon" to 1.0,
                "clove" to 0.5
            ),
            heat = 1.0,
            salt = 0.0,
            batch = 100,
            notes = "Finishing blend, add it off the heat.",
            created = today - 4
        ),
        blend(
            id = "herb_steak_crust",
            name = "Herb Steak Crust",
            items = listOf(
                "black_pepper" to 3.0,
                "garlic" to 2.0,
                "rosemary" to 1.5,
                "thyme" to 1.5,
                "salt" to 2.0,
                "mustard" to 1.0
            ),
            heat = 0.5,
            salt = 1.25,
            batch = 50,
            notes = "Press hard onto the steak, rest it ten minutes.",
            created = today - 2
        )
    )

    fun seedItems(): List<BlendItem> = listOf(
        BlendItem("paprika", 3.0),
        BlendItem("salt", 1.0)
    )

    private fun blend(
        id: String,
        name: String,
        items: List<Pair<String, Double>>,
        heat: Double,
        salt: Double,
        batch: Int,
        notes: String,
        created: Long
    ): Blend = Blend(
        id = id,
        name = name,
        items = items.map { BlendItem(it.first, it.second) },
        heatMultiplier = heat,
        saltMultiplier = salt,
        batchGrams = batch,
        unit = MeasureUnit.GRAMS,
        notes = notes,
        createdEpochDay = created
    )
}

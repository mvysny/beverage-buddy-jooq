package com.vaadin.starter.beveragebuddy.backend

import com.github.vokorm.db
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.ReviewRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import java.time.LocalDate
import kotlin.random.Random

internal object DemoData {

    private val MINERAL_WATER = "Mineral Water"
    private val SOFT_DRINK = "Soft Drink"
    private val COFFEE = "Coffee"
    private val TEA = "Tea"
    private val DAIRY = "Dairy"
    private val CIDER = "Cider"
    private val BEER = "Beer"
    private val WINE = "Wine"
    private val OTHER = "Other"

    /**
     * Maps beverage name to a beverage category.
     */
    val BEVERAGES: MutableMap<String, String> = LinkedHashMap()

    init {
        listOf("Evian",
                "Voss",
                "Veen",
                "San Pellegrino",
                "Perrier")
                .forEach { name -> BEVERAGES.put(name, MINERAL_WATER) }

        listOf("Coca-Cola",
                "Fanta",
                "Sprite")
                .forEach { name -> BEVERAGES.put(name, SOFT_DRINK) }

        listOf("Maxwell Ready-to-Drink Coffee",
                "Nescafé Gold",
                "Starbucks East Timor Tatamailau")
                .forEach { name -> BEVERAGES.put(name, COFFEE) }

        listOf("Prince Of Peace Organic White Tea",
                "Pai Mu Tan White Peony Tea",
                "Tazo Zen Green Tea",
                "Dilmah Sencha Green Tea",
                "Twinings Earl Grey",
                "Twinings Lady Grey",
                "Classic Indian Chai")
                .forEach { name -> BEVERAGES.put(name, TEA) }

        listOf("Cow's Milk",
                "Goat's Milk",
                "Unicorn's Milk",
                "Salt Lassi",
                "Mango Lassi",
                "Airag")
                .forEach { name -> BEVERAGES.put(name, DAIRY) }

        listOf("Crowmoor Extra Dry Apple",
                "Golden Cap Perry",
                "Somersby Blueberry",
                "Kopparbergs Naked Apple Cider",
                "Kopparbergs Raspberry",
                "Kingstone Press Wild Berry Flavoured Cider",
                "Crumpton Oaks Apple",
                "Frosty Jack's",
                "Ciderboys Mad Bark",
                "Angry Orchard Stone Dry",
                "Walden Hollow",
                "Fox Barrel Wit Pear")
                .forEach { name -> BEVERAGES.put(name, CIDER) }

        listOf("Budweiser",
                "Miller",
                "Heineken",
                "Holsten Pilsener",
                "Krombacher",
                "Weihenstephaner Hefeweissbier",
                "Ayinger Kellerbier",
                "Guinness Draught",
                "Kilkenny Irish Cream Ale",
                "Hoegaarden White",
                "Barbar",
                "Corsendonk Agnus Dei",
                "Leffe Blonde",
                "Chimay Tripel",
                "Duvel",
                "Pilsner Urquell",
                "Kozel",
                "Staropramen",
                "Lapin Kulta IVA",
                "Kukko Pils III",
                "Finlandia Sahti")
                .forEach { name -> BEVERAGES.put(name, BEER) }

        listOf("Jacob's Creek Classic Shiraz",
                "Chateau d’Yquem Sauternes",
                "Oremus Tokaji Aszú 5 Puttonyos")
                .forEach { name -> BEVERAGES.put(name, WINE) }

        listOf("Pan Galactic Gargle Blaster",
                "Mead",
                "Soma")
                .forEach { name -> BEVERAGES[name] = OTHER }
    }

    fun createDemoData() = db2 {
        // generate categories
        BEVERAGES.values.distinct().forEach { name -> CategoryRecord(name = name).executeInsert() }

        /// generate reviews
        val r = Random
        val reviewCount: Int = 20 + r.nextInt(30)
        val beverages: List<MutableMap.MutableEntry<String, String>> = BEVERAGES.entries.toList()

        for (i in 0 until reviewCount) {
            val review = ReviewRecord()
            val beverage: MutableMap.MutableEntry<String, String> = beverages.random()
            val category: CategoryRecord = create.fetchSingle(CATEGORY, CATEGORY.NAME.eq(beverage.value))
            review.name = beverage.key
            val testDay: LocalDate = LocalDate.of(1930 + r.nextInt(88),
                    1 + r.nextInt(12), 1 + r.nextInt(28))
            review.date = testDay
            review.score = (1 + r.nextInt(5)).toByte()
            review.category = category.id
            review.count = (1 + r.nextInt(15)).toByte()
            review.executeInsert()
        }
    }
}

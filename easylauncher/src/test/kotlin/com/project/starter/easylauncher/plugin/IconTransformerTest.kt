package com.project.starter.easylauncher.plugin

import com.project.starter.easylauncher.filter.ChromeLikeFilter
import com.project.starter.easylauncher.filter.ColorRibbonFilter
import com.project.starter.easylauncher.filter.OverlayFilter
import com.project.starter.easylauncher.plugin.models.IconFile
import com.project.starter.easylauncher.plugin.utils.vectorFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class IconTransformerTest {

    @TempDir
    lateinit var tempDir: File
    lateinit var sourceIcon: IconFile.XmlDrawable.Vector
    lateinit var output: File

    @BeforeEach
    internal fun setUp() {
        val drawable = tempDir.resolve("drawable").apply {
            mkdir()
        }
        val sourceIconFile = drawable.resolve("icon_resource.xml").apply { writeText(vectorFile()) }
        sourceIcon = sourceIconFile.tryParseXmlDrawable() as IconFile.XmlDrawable.Vector

        output = drawable.resolve("output.xml")
    }

    @Test
    fun `transforms vector icon pre api 26`() {
        val expected = tempDir.resolve("drawable-anydpi-v26/output.xml")
        sourceIcon.transform(
            outputFile = output,
            minSdkVersion = 21,
            filters = listOf(
                ColorRibbonFilter(label = "test1", ribbonColor = "#0000ff"),
                ColorRibbonFilter(label = "test2", ribbonColor = "#ff00000", gravity = ColorRibbonFilter.Gravity.BOTTOM),
                ChromeLikeFilter(label = "test3"),
                OverlayFilter(fgFile = File("src/test/resources/beta.png")),
            ),
        )

        assertThat(expected).hasContent(
            """
            |<?xml version="1.0" encoding="utf-8"?>
            |<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
            |
            |    <item android:drawable="@drawable/easy_icon_resource" />
            |
            |    <item android:drawable="@drawable/colorribbonfilter_0_output" />
            |
            |    <item android:drawable="@drawable/colorribbonfilter_1_output" />
            |
            |    <item android:drawable="@drawable/chromelikefilter_2_output" />
            |
            |    <item android:drawable="@drawable/overlayfilter_3_output" />
            |</layer-list>
            |
            """.trimMargin(),
        )
    }

    @Test
    fun `transforms vector icon since api 26`() {
        val expected = tempDir.resolve("drawable-anydpi/output.xml")
        sourceIcon.transform(
            outputFile = output,
            minSdkVersion = 26,
            filters = listOf(
                ColorRibbonFilter(label = "test1", ribbonColor = "#0000ff"),
                ColorRibbonFilter(label = "test2", ribbonColor = "#ff0000", gravity = ColorRibbonFilter.Gravity.BOTTOM),
                ChromeLikeFilter(label = "test3"),
                OverlayFilter(fgFile = File("src/test/resources/beta.png")),
            ),
        )

        assertThat(expected).hasContent(
            """
            |<?xml version="1.0" encoding="utf-8"?>
            |<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
            |
            |    <item android:drawable="@drawable/easy_icon_resource" />
            |
            |    <item android:drawable="@drawable/colorribbonfilter_0_output" />
            |
            |    <item android:drawable="@drawable/colorribbonfilter_1_output" />
            |
            |    <item android:drawable="@drawable/chromelikefilter_2_output" />
            |
            |    <item android:drawable="@drawable/overlayfilter_3_output" />
            |</layer-list>
            |
            """.trimMargin(),
        )
    }
}

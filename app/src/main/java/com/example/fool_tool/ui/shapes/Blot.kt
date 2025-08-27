package com.example.fool_tool.ui.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * @param spreadability is a value
 * reflecting how the blot actually looks like. A blot when 1f
 * and a simple circle when 0f
 */

class Blot(
    spreadability: Float,
    maxRadius: Dp,
    numberOfWaves: Int,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(getPath(size, density))
    }

    private fun getPath(size: Size, density: Density): Path {
        return Path()
    }
}
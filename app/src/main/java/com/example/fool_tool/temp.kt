package com.example.fool_tool.ui.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

//@Composable
//fun CustomNavigationBar() {
//    val items = listOf(1, 2)
//
//    SubcomposeLayout { constraints ->
//
//        // 1) Сабкомпозим фон (зелёные плитки)
//        val bgMeasurables = subcompose("backgrounds") {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                items.forEach { _ ->
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .height(50.dp)
//                            .background(Color.Green)
//                    )
//                }
//            }
//        }
//        val bgPlaceables = bgMeasurables.map { it.measure(constraints) }
//
//        // 2) Сабкомпозим индикатор (синий прямоугольник)
//        val indicatorMeasurables = subcompose("indicator") {
//            Box(
//                modifier = Modifier
//                    .width(300.dp)
//                    .height(50.dp)
//                    .background(Color.Blue)
//            )
//        }
//        val indicatorPlaceables = indicatorMeasurables.map { it.measure(constraints) }
//
//        // 3) Сабкомпозим контент (иконки + текст)
//        val contentMeasurables = subcompose("content") {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                items.forEach { _ ->
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center,
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .size(24.dp)
//                                .background(Color.Black)
//                        )
//                        Text("Text", color = Color.Black)
//                    }
//                }
//            }
//        }
//        val contentPlaceables = contentMeasurables.map { it.measure(constraints) }
//
//        // --- Layout: располагаем все слои ---
//        layout(constraints.maxWidth, 64.dp.roundToPx()) {
//            var y = 0
//            bgPlaceables.forEach { it.placeRelative(0, y) }
//            indicatorPlaceables.forEach { it.placeRelative(0, y) }
//            contentPlaceables.forEach { it.placeRelative(0, y) }
//        }
//    }
//}


@Composable
fun CustomNavigationBar() {
    val items = listOf("One", "Two", "Three")
    var selectedIndex by remember { mutableStateOf(0) }

    // Храним размеры айтемов (для позиционирования индикатора)
    val itemWidths = remember { mutableStateListOf<Int>() }

    // Анимация смещения индикатора
    val indicatorOffset by animateDpAsState(
        targetValue = with(LocalDensity.current) {
            // Считаем смещение по сумме ширин всех предыдущих айтемов
            itemWidths.take(selectedIndex).sum().toDp()
        },
        label = "indicatorOffset"
    )

    SubcomposeLayout { constraints ->

        // 1) Сабкомпозим контент айтемов (с измерением)
        val contentMeasurables = subcompose("content") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, label ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { selectedIndex = index }
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    if (selectedIndex == index) Color.Black else Color.Gray,
                                    shape = CircleShape
                                )
                        )
                        Text(label, color = Color.Black)
                    }
                }
            }
        }
        val contentPlaceables = contentMeasurables.map {
            it.measure(constraints)
        }

        // обновляем список ширин айтемов
        if (itemWidths.size != items.size) {
            itemWidths.clear()
            itemWidths.addAll(contentPlaceables.map {
                it.width - 1000
            })
        }

        // 2) Сабкомпозим индикатор (синий прямоугольник)
        val indicatorMeasurables = subcompose("indicator") {
            Box(
                modifier = Modifier
                    .width(with(LocalDensity.current) {
                        itemWidths.getOrNull(selectedIndex)?.toDp() ?: 0.dp
                    })
                    .height(50.dp)
                    .background(Color.Blue, shape = RoundedCornerShape(2.dp))
            )
        }
        val indicatorPlaceables = indicatorMeasurables.map { it.measure(constraints) }

        // --- Layout ---
        val height = contentPlaceables.maxOf { it.height } + 8.dp.roundToPx()

        layout(constraints.maxWidth, height) {
            // 1) Индикатор
            indicatorPlaceables.forEach {
                it.placeRelative(indicatorOffset.roundToPx(), height - it.height) // снизу
            }
            // 2) Контент
            contentPlaceables.forEach {
                it.placeRelative(0, 0)
            }
        }
    }
}

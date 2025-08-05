package com.example.fool_tool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.fool_tool.ui.navigation.RootNavigator
import com.example.fool_tool.ui.theme.FooltoolTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FooltoolTheme {
                val navController = rememberNavController()

                RootNavigator(navController = navController)
            }
        }
    }
}

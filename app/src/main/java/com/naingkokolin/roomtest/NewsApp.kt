package com.naingkokolin.roomtest

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.naingkokolin.roomtest.data.Article
import com.naingkokolin.roomtest.data.Source
import com.naingkokolin.roomtest.view.DetailScreen
import com.naingkokolin.roomtest.view.FirstScreen
import kotlin.random.Random

@Composable
fun NewsApp(
    navController: NavHostController,
) {
    val viewModel:MainViewModel = viewModel()

    val viewState by viewModel.newsState

    val randomDirection by rememberSaveable {
        mutableStateOf(Random.nextBoolean())
    }

//    TODO("TO ADD SEARCH FUNCTIONALITY")
//    Scaffold(
//        topBar = {
//
//        }
//    ) {
//
//    }

    NavHost(
        navController = navController,
        startDestination = Screen.FirstScreen.route
    ) {
        composable(
            route = Screen.FirstScreen.route,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(700),
                    initialOffsetX = {
                        if (randomDirection) -it else it
                    }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(700)
                ){
                    if(randomDirection) it else -it
                }
            }
        ) {
            FirstScreen(
                navigateToDetail = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("news", it)
                    navController.navigate(Screen.DetailScreen.route)
                },
                newsState = viewState
            )
        }
        composable(
            route = Screen.DetailScreen.route,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(700),
                    initialOffsetX = {
                        if (randomDirection) -it else it
                    }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(700)
                ){
                    if(randomDirection) it else -it
                }
            }
        ) {
            val article = navController.previousBackStackEntry?.savedStateHandle?.get<Article>("news") ?: Article(
                Source("001","NKKL"),"NKKL","Sample Title",
                "Sample Description","Sample URL","","",""
            )
            DetailScreen(article)
        }
    }
}
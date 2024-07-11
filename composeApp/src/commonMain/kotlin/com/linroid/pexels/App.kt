@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.linroid.pexels

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.linroid.pexels.screen.feed.HomeScreen
import com.linroid.pexels.screen.photo.PhotoScreen
import com.linroid.pexels.storage.PhotoMemoryCache
import com.linroid.pexels.theme.AppTheme

@Composable
internal fun App(navController: NavHostController = rememberNavController()) = AppTheme {
    SharedTransitionLayout {
        NavHost(
            navController = navController, startDestination = PexelsScreen.Home.name
        ) {
            composable(PexelsScreen.Home.name) {
                HomeScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable,
                    onViewPhoto = { photo ->
                        PhotoMemoryCache.put(photo)
                        navController.navigate(
                            "${PexelsScreen.Photo.name}/${photo.id}",
                            null,
                        )
                    }
                )
            }
            composable(
                "${PexelsScreen.Photo.name}/{photoId}", arguments = listOf(navArgument("photoId") {
                    type = NavType.IntType
                    nullable = false
                })
            ) { backStackEntry ->
                val photo = PhotoMemoryCache.getById(backStackEntry.arguments?.getInt("photoId")!!)
                PhotoScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable,
                    photo = photo,
                    onExit = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

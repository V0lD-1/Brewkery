package `in`.aditya.brewkery.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import `in`.aditya.brewkery.ui.screens.cart.CartScreen
import `in`.aditya.brewkery.ui.screens.detail.ItemDetailScreen
import `in`.aditya.brewkery.ui.screens.home.HomeScreen
import `in`.aditya.brewkery.ui.screens.order.OrderStatusScreen

@Composable
fun BrewkeryNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Screen 1: Home / Menu Catalog
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToDetail = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onNavigateToOrderStatus = {
                    navController.navigate(Screen.OrderStatus.route)
                }
            )
        }

        // Screen 2: Item Detail
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.IntType }
            )
        ) {
            ItemDetailScreen(
                onBackClick = { navController.popBackStack() },
                onItemAddedToCart = {
                    navController.navigate(Screen.Cart.route)
                }
            )
        }

        // Screen 3: Cart & Bill
        composable(route = Screen.Cart.route) {
            CartScreen(
                onBackClick = { navController.popBackStack() },
                onOrderPlaced = {
                    navController.navigate(Screen.OrderStatus.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }

        // Screen 4: Order Status
        composable(route = Screen.OrderStatus.route) {
            OrderStatusScreen(
                onHomeClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

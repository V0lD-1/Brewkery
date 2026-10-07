package `in`.aditya.brewkery.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: Int) = "detail/$itemId"
    }
    object Cart : Screen("cart")
    object OrderStatus : Screen("order_status")
}

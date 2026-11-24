package edu.virginia.cs4720.sgw5ne.finalproject

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

/** Route names for every screen in the app. */
object Routes {
    // Auth flow (no bottom bar)
    const val LOGIN_MENU = "login_menu"
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"

    // Top-level tabs (bottom bar visible)
    const val HOME = "home"
    const val MESSAGES = "messages"
    const val GROUPS = "groups"
    const val SEARCH = "search"
    const val PROFILE = "profile"
}

/**
 * The app's navigation graph. Starts at the Log In Menu (a project requirement).
 * NavController keeps its back stack across rotation and process death on its own.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN_MENU,
        modifier = modifier
    ) {
        composable(Routes.LOGIN_MENU) {
            LoginMenuScreen(
                onLogInClick = { navController.navigate(Routes.LOGIN) },
                onSignUpClick = { navController.navigate(Routes.SIGN_UP) }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                // TEMPORARY: goes straight to Home so the screens can be viewed on a device.
                // Replace with Firebase Auth sign-in, navigating only on success.
                onLogInClick = { _, _ -> navController.goToHomeClearingAuth() },
                onForgotPasswordClick = { /* TODO: password reset */ },
                // Swap Log In for Sign Up instead of stacking them, so Back
                // always returns to the Log In Menu rather than bouncing between the two.
                onSignUpClick = {
                    navController.navigate(Routes.SIGN_UP) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onSignUpClick = { _, _, _, _ -> /* TODO: Firebase Auth, then goToHomeClearingAuth() */ },
                onLogInClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SIGN_UP) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                events = emptyList(), // TODO: load from Firestore
                pendingApprovalCount = 0, // TODO: load from Firestore
                onEventClick = { /* TODO: navigate to Event Details */ },
                onPendingApprovalsClick = { navController.navigateToTab(TopLevelTab.PROFILE) }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                username = null, // TODO: current user's username from Firestore users/{uid}
                events = emptyList(), // TODO: current user's events from Firestore
                onSettingsClick = { /* TODO: settings */ },
                onEventClick = { /* TODO: navigate to Event Details */ }
            )
        }

        composable(Routes.MESSAGES) {
            MessagesScreen(
                conversations = emptyList(), // TODO: load from Firestore
                onConversationClick = { /* TODO: open chat thread */ }
            )
        }

        composable(Routes.GROUPS) {
            GroupsScreen(
                groups = emptyList(), // TODO: load the user's groups from Firestore
                onGroupClick = { /* TODO: open group */ },
                onMembersClick = { /* TODO: show member list */ },
                onNewGroupClick = { /* TODO: create group */ }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                groups = emptyList(), // TODO: public groups matching the query, from Firestore
                events = emptyList(), // TODO: upcoming events matching the query, from Firestore
                onQueryChange = { /* TODO: pass to a ViewModel that queries Firestore */ },
                onGroupClick = { /* TODO: open group / join */ },
                onEventClick = { /* TODO: navigate to Event Details */ }
            )
        }
    }
}

/** After logging in, Home becomes the root so Back exits the app instead of returning to login. */
private fun NavHostController.goToHomeClearingAuth() {
    navigate(Routes.HOME) {
        popUpTo(Routes.LOGIN_MENU) { inclusive = true }
    }
}

/**
 * Standard bottom-bar navigation: one copy of each tab, tab state saved/restored
 * when switching, and Back from any tab returns to Home.
 * Tabs whose screens aren't built yet are ignored instead of crashing.
 */
fun NavHostController.navigateToTab(tab: TopLevelTab) {
    if (graph.findNode(tab.route) == null) return
    navigate(tab.route) {
        // Home (not the graph's start, the Log In Menu) is the root once logged in.
        popUpTo(Routes.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

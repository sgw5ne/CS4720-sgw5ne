package edu.virginia.cs4720.sgw5ne.finalproject

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource

/** The five top-level destinations shown in the bottom bar. */
enum class TopLevelTab(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: @Composable () -> ImageVector
) {
    HOME(Routes.HOME, R.string.tab_home, { Icons.Filled.Home }),
    MESSAGES(Routes.MESSAGES, R.string.tab_messages, { ImageVector.vectorResource(R.drawable.ic_chat) }),
    GROUPS(Routes.GROUPS, R.string.tab_groups, { ImageVector.vectorResource(R.drawable.ic_groups) }),
    SEARCH(Routes.SEARCH, R.string.tab_search, { Icons.Filled.Search }),
    PROFILE(Routes.PROFILE, R.string.tab_profile, { Icons.Filled.Person });

    companion object {
        fun fromRoute(route: String?): TopLevelTab? = entries.firstOrNull { it.route == route }
    }
}

/**
 * Shared bottom navigation bar (home / messages / groups / search / profile).
 * Material NavigationBar items are 80dp tall, well above the 48dp touch-target minimum,
 * and always show a text label so meaning never depends on the icon alone.
 */
@Composable
fun AppBottomBar(
    currentTab: TopLevelTab,
    onTabSelected: (TopLevelTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        TopLevelTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = tab == currentTab,
                onClick = { onTabSelected(tab) },
                // The visible label already names the tab, so the icon is decorative for TalkBack.
                icon = { Icon(tab.icon(), contentDescription = null) },
                label = { Text(stringResource(tab.labelRes)) }
            )
        }
    }
}

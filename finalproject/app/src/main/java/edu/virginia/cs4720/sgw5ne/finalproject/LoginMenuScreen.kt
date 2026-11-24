package edu.virginia.cs4720.sgw5ne.finalproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.virginia.cs4720.sgw5ne.finalproject.ui.theme.FinalprojectTheme

/**
 * Log In Menu: the app's entry point. A hero/branding area on top, with the two
 * choices (Log In, Sign Up) underneath. Stateless: navigation is passed in as lambdas.
 */
@Composable
fun LoginMenuScreen(
    onLogInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // keeps buttons reachable in landscape / large font
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login_menu_welcome),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Hero / logo area (the large grey box in the wireframe).
        // Placeholder until a real logo image is added; described for TalkBack.
        val heroDescription = stringResource(R.string.login_menu_hero_description)
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .aspectRatio(1.2f)
                .semantics { contentDescription = heroDescription }
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(24.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.app_display_name),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.login_menu_tagline),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(Modifier.height(40.dp))

        // Primary action: filled button. Secondary action: outlined button.
        // Both full-width and >= 48dp tall for touch-target accessibility.
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().widthIn(max = 480.dp)
        ) {
            Button(
                onClick = onLogInClick,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
            ) {
                Text(stringResource(R.string.action_log_in))
            }
            OutlinedButton(
                onClick = onSignUpClick,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
            ) {
                Text(stringResource(R.string.action_sign_up))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginMenuScreenPreview() {
    FinalprojectTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoginMenuScreen(onLogInClick = {}, onSignUpClick = {})
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginMenuScreenDarkPreview() {
    FinalprojectTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoginMenuScreen(onLogInClick = {}, onSignUpClick = {})
        }
    }
}

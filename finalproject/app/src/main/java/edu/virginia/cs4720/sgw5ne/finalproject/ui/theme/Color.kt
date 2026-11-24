package edu.virginia.cs4720.sgw5ne.finalproject.ui.theme

import androidx.compose.ui.graphics.Color

// Design intent: a social "get together" app. A deep, trustworthy indigo carries
// structure (headers, primary actions); a warm coral marks the social/joinable
// moments (sign up, request to join). Neutrals stay cool so content stands out.
// All on-colors below are chosen to keep text contrast >= 4.5:1.

// Light scheme
val Indigo700 = Color(0xFF2F3E9E)      // primary          (white text ≈ 9:1)
val Indigo100 = Color(0xFFDDE1FF)      // primaryContainer
val Indigo900 = Color(0xFF0B1660)      // onPrimaryContainer
val Coral700 = Color(0xFFB3401F)       // secondary        (white text ≈ 5.5:1)
val Coral100 = Color(0xFFFFDBD0)       // secondaryContainer
val Coral900 = Color(0xFF3B0A00)       // onSecondaryContainer
val Mist50 = Color(0xFFF7F8FC)         // background / surface
val Mist200 = Color(0xFFE3E5EE)        // surfaceVariant (wireframe grey boxes)
val Slate900 = Color(0xFF1A1C24)       // onBackground / onSurface
val Slate600 = Color(0xFF454857)       // onSurfaceVariant (≈ 8:1 on Mist200)

// Dark scheme
val Indigo200 = Color(0xFFBAC3FF)      // primary
val Indigo800 = Color(0xFF1B2A85)      // primaryContainer
val Coral200 = Color(0xFFFFB59E)       // secondary
val Coral800 = Color(0xFF8A2A0C)       // secondaryContainer
val Night900 = Color(0xFF12131A)       // background / surface
val Night700 = Color(0xFF2C2E3A)       // surfaceVariant
val Mist100 = Color(0xFFE3E4EE)        // onBackground / onSurface
val Mist300 = Color(0xFFC5C6D4)        // onSurfaceVariant

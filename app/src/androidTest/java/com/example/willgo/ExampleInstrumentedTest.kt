package com.example.willgo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.willgo.data.User.User
import com.example.willgo.view.screens.EventDataScreen
import com.example.willgo.view.screens.navScreens.ProfileScreen

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.willgo", appContext.packageName)
    }
}

@RunWith(AndroidJUnit4::class)
class MainActivityTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    val user = User(
        nickname = "camilayoze12",
        name = "Camila Ayoze",
        password = "password123",
        email = "camilayoze@gmail.com",
        followers = 1,
        followed = 1
    )

/*
    @Test
    fun testEventSearch() {
        // Escribe en el campo de búsqueda
        onView(withId(R.id.searchField))
            .perform(typeText("Concierto"), closeSoftKeyboard())

        // Comprueba si un resultado esperado aparece en la lista
        onView(withText("Concierto de Rock"))
            .check(matches(isDisplayed()))
    }
*/
    @Test
    fun testUserProfileDisplayed() {
        composeTestRule.setContent {
            ProfileScreen(navController = rememberNavController(), paddingValues =  PaddingValues(7.dp)
                , user = user) // Renderiza la pantalla del perfil

        }

        // Verifica que el nombre y el nickname del usuario son visibles
        composeTestRule.onNodeWithTag("username").assertTextEquals(user.name)

        // Comprueba que las estadísticas son correctas
        composeTestRule.onNodeWithTag("followerText").assertTextEquals("Seguidores")
        composeTestRule.onNodeWithTag("followedText").assertTextEquals("Seguidos")



    }
}
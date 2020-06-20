package io.bkraszewski.safecam.feature.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.bkraszewski.safecam.TestCoroutineRule
import io.bkraszewski.safecam.feature.StringProvider
import io.bkraszewski.safecam.feature.crypto.UserAuthenticator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

internal class LoginViewModelTest {
    lateinit var cut: LoginViewModel
    val stringProvider: StringProvider = mock()
    val userAuthenticator: UserAuthenticator = mock()

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    @Before
    fun setUp() {
        whenever(stringProvider.provideString(any())).thenReturn("error u fool!")
        cut = LoginViewModel(userAuthenticator, stringProvider)
    }

    @Test
    fun shouldShowErrorOnWrongPassword() {
        testCoroutineRule.runBlockingTest {
            whenever(userAuthenticator.isValid(any())).thenReturn(false)
            cut.onPasswordSubmitted()

            assertTrue(cut.passwordError.value!!.isNotBlank())
        }
    }

    @Test
    fun shouldShowNavigateAwayOnCorrectPassword() {
        testCoroutineRule.runBlockingTest {
            whenever(userAuthenticator.isValid(any())).thenReturn(true)
            cut.onPasswordSubmitted()

            assertNotNull(cut.navigateToCamera.value)
        }
    }
}

package com.example.testapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bwell.BWellSdk
import com.bwell.common.models.domain.user.Person
import com.bwell.common.models.responses.BWellResult
import com.example.testapplication.ui.theme.TestApplicationTheme
import com.bwell.core.config.BWellConfig
import com.bwell.core.config.KeyStoreConfig
import com.bwell.core.network.auth.Credentials
import kotlinx.coroutines.runBlocking



class MainActivity : ComponentActivity() {
    private suspend fun getUserFirstName(): String? {
        val keystore: KeyStoreConfig = KeyStoreConfig.Builder()
            .path(applicationContext.filesDir.absolutePath)
            .build()
        val config: BWellConfig = BWellConfig.Builder()
            .clientKey("<CLIENT_KEY>")
            .keystore(keystore)
            .build()
        BWellSdk.initialize(config = config)
        val credentials = Credentials.OAuthCredentials("<TOKEN>")
        BWellSdk.authenticate(credentials)
        val profile = BWellSdk.user.getProfile() as BWellResult.SingleResource<Person>
        return profile.data?.firstName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var firstName: String?
                    runBlocking {
                        firstName = getUserFirstName()
                    }
                    Greeting(firstName)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String?, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestApplicationTheme {
        Greeting("Android")
    }
}
package com.example.formtestingapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.formtestingapplication.ui.theme.FormTestingApplicationTheme
import joyfill.Form
import joyfill.Mode
import joyfill.editorOf
import joyfill.editors.DocumentEditor
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FormTestingApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->

                    val parsedObj = Json.parseToJsonElement(TestObject.testDefinition()).jsonObject
                    val joyDocEditor = editorOf(parsedObj)
                    Form(
                        navigation = joyDocEditor.pageCount() > 1, // determine if showing page switcher
                        contentPadding = PaddingValues(8.dp),
                        mode = Mode.fill,
                        onUpload = {
                            // this is triggered when user taps "Upload"
                            emptyList()
                        },
                        editor = joyDocEditor,
                    )
                }
            }
        }
    }
}

fun DocumentEditor.pageCount(): Int {
    val mobileViews = views.filter { it.type == "mobile" }
    assert(mobileViews.size < 2) { "Expected to have 0 or max of 1 mobile views" }

    return if (mobileViews.isEmpty()) {
        // No special mobile views, count all pages
        pages.raw().count()
    } else {
        // Has mobile specific views, sum those pages
        mobileViews.flatMap { it.pages }.count()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FormTestingApplicationTheme {
        Greeting("Android")
    }
}
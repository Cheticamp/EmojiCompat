/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * The code in this project has been extracted from the EmojiCompat app in
 *      https://github.com/android/user-interface-samples
 * and modified to display a "Cloudy Face" emoji in all fields including an added field that
 * is just a View that manages a StaticLayout.
 *
 * The build.gradle file dependencies have been modified to include
 *     implementation 'androidx.emoji:emoji-bundled:1.2.0-alpha03'
 * to pick up the latest emojis.
 */

package com.example.android.emojicompat

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.text.EmojiCompat
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    companion object {
        val CLOUDY_FACE by lazy {
            val sb = StringBuilder()
            val cloudyFace = "\\u1F636\\u200D\\u1F32B\\uFE0F"
            cloudyFace.split("\\u").forEach {
                if (it.isNotEmpty()) {
                    sb.append(getUtf16FromInt(it.toInt(16)))
                }
            }
            sb.toString()
        }

        private fun getUtf16FromInt(unicode: Int) = String(Character.toChars(unicode))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TextView variant provided by EmojiCompat library
        val emojiTextView: TextView = findViewById(R.id.emoji_text_view)
        emojiTextView.text = getString(R.string.emoji_text_view, CLOUDY_FACE)

        // EditText variant provided by EmojiCompat library
        val emojiEditText: TextView = findViewById(R.id.emoji_edit_text)
        emojiEditText.text = getString(R.string.emoji_edit_text, CLOUDY_FACE)

        // Button variant provided by EmojiCompat library
        val emojiButton: TextView = findViewById(R.id.emoji_button)
        emojiButton.text = getString(R.string.emoji_button, CLOUDY_FACE)

        // Regular TextView without EmojiCompat support; you have to manually process the text
        val regularTextView: TextView = findViewById(R.id.regular_text_view)
        val myView = findViewById<MyView>(R.id.myView)

        // We will process the regular TextView and a custom View that manages its own StaticLayout.
        EmojiCompat.get().registerInitCallback(InitCallback(regularTextView, myView))

        // Custom TextView
        val customTextView: TextView = findViewById(R.id.emoji_custom_text_view)
        customTextView.text = getString(R.string.custom_text_view, CLOUDY_FACE)
    }

    private class InitCallback(regularTextView: TextView, myView: MyView) :
        EmojiCompat.InitCallback() {

        val regularTextViewRef = WeakReference(regularTextView)
        val myViewRef = WeakReference(myView)

        override fun onInitialized() {
            val regularTextView = regularTextViewRef.get()
            val compat = EmojiCompat.get()
            if (regularTextView != null) {
                val context = regularTextView.context
                regularTextView.text = compat.process(
                    context.getString(R.string.regular_text_view, CLOUDY_FACE)
                )
            }

            val myView = myViewRef.get()

            if (myView != null) {
                val context = myView.context
                myView.setText(
                    compat.process(context.getString(R.string.my_view, CLOUDY_FACE))
                )
            }
        }
    }

}

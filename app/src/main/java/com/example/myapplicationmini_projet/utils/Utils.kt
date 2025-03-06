package com.example.myapplicationmini_projet.utils

import android.text.Html

fun decodeHtml(input: String): String {
    return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY).toString()
}
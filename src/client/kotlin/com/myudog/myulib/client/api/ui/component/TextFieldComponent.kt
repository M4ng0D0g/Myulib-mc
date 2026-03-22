package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component

class TextFieldComponent(
    var text: String = "",
    var placeholder: String = "",
    var maxLength: Int = 32,
    var cursorPos: Int = 0,
    var charFilter: (Char) -> Boolean = { true },
    var onTextChanged: (String) -> Unit = {},
    var onEnter: (String) -> Unit = {}
) : Component {

    // 預設過濾器集 (靜態)
    companion object {
        val NUMBERS_ONLY: (Char) -> Boolean = { it.isDigit() || it == '-' || it == '.' }
        val ALPHANUMERIC: (Char) -> Boolean = { it.isLetterOrDigit() || it == '_' }
        val NO_SPACES: (Char) -> Boolean = { !it.isWhitespace() }
    }
}
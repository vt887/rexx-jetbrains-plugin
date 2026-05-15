package org.rexxlang.intellij.rexx

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object RexxTextAttributes {
    @JvmField
    val COMMENT: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_COMMENT",
        DefaultLanguageHighlighterColors.BLOCK_COMMENT,
    )

    @JvmField
    val KEYWORD: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_KEYWORD",
        DefaultLanguageHighlighterColors.KEYWORD,
    )

    @JvmField
    val STRING: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_STRING",
        DefaultLanguageHighlighterColors.STRING,
    )

    @JvmField
    val NUMBER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_NUMBER",
        DefaultLanguageHighlighterColors.NUMBER,
    )

    @JvmField
    val OPERATOR: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_OPERATOR",
        DefaultLanguageHighlighterColors.OPERATION_SIGN,
    )

    @JvmField
    val PUNCTUATION: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_PUNCTUATION",
        DefaultLanguageHighlighterColors.DOT,
    )

    @JvmField
    val BAD_CHARACTER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_BAD_CHARACTER",
        HighlighterColors.BAD_CHARACTER,
    )
}

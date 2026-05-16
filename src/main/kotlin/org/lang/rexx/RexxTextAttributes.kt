package org.lang.rexx

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
    val COMMENT_TAG: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_COMMENT_TAG",
        DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE,
    )

    @JvmField
    val KEYWORD: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_KEYWORD",
        DefaultLanguageHighlighterColors.KEYWORD,
    )

    @JvmField
    val VARIABLE: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_VARIABLE",
        DefaultLanguageHighlighterColors.LOCAL_VARIABLE,
    )

    @JvmField
    val FUNCTION: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_FUNCTION",
        DefaultLanguageHighlighterColors.FUNCTION_CALL,
    )

    @JvmField
    val BUILTIN_FUNCTION: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_BUILTIN_FUNCTION",
        DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL,
    )

    @JvmField
    val LABEL: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_LABEL",
        DefaultLanguageHighlighterColors.LABEL,
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
    val ASSIGNMENT_OPERATOR: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_ASSIGNMENT_OPERATOR",
        DefaultLanguageHighlighterColors.OPERATION_SIGN,
    )

    @JvmField
    val COMPARISON_OPERATOR: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_COMPARISON_OPERATOR",
        DefaultLanguageHighlighterColors.OPERATION_SIGN,
    )

    @JvmField
    val LOGICAL_OPERATOR: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_LOGICAL_OPERATOR",
        DefaultLanguageHighlighterColors.OPERATION_SIGN,
    )

    @JvmField
    val ARITHMETIC_OPERATOR: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "REXX_ARITHMETIC_OPERATOR",
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

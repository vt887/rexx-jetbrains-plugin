package org.lang.rexx

import com.intellij.psi.tree.IElementType

object RexxTokenTypes {
    @JvmField val ARITHMETIC_OPERATOR = RexxTokenType("ARITHMETIC_OPERATOR")

    @JvmField val ASSIGNMENT_OPERATOR = RexxTokenType("ASSIGNMENT_OPERATOR")

    @JvmField val BUILTIN_FUNCTION = RexxTokenType("BUILTIN_FUNCTION")

    @JvmField val COMMENT = RexxTokenType("COMMENT")

    @JvmField val COMPARISON_OPERATOR = RexxTokenType("COMPARISON_OPERATOR")

    @JvmField val FUNCTION_CALL = RexxTokenType("FUNCTION_CALL")

    @JvmField val IDENTIFIER = RexxTokenType("IDENTIFIER")

    @JvmField val KEYWORD = RexxTokenType("KEYWORD")

    @JvmField val LABEL = RexxTokenType("LABEL")

    @JvmField val LOGICAL_OPERATOR = RexxTokenType("LOGICAL_OPERATOR")

    @JvmField val NUMBER = RexxTokenType("NUMBER")

    @JvmField val OPERATOR = RexxTokenType("OPERATOR")

    @JvmField val PUNCTUATION = RexxTokenType("PUNCTUATION")

    @JvmField val STRING = RexxTokenType("STRING")
}

class RexxTokenType(
    debugName: String,
) : IElementType(debugName, RexxLanguage)

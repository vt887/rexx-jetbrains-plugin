package org.rexxlang.intellij.rexx

import com.intellij.psi.tree.IElementType

object RexxTokenTypes {
    @JvmField val COMMENT = RexxTokenType("COMMENT")
    @JvmField val IDENTIFIER = RexxTokenType("IDENTIFIER")
    @JvmField val KEYWORD = RexxTokenType("KEYWORD")
    @JvmField val NUMBER = RexxTokenType("NUMBER")
    @JvmField val OPERATOR = RexxTokenType("OPERATOR")
    @JvmField val PUNCTUATION = RexxTokenType("PUNCTUATION")
    @JvmField val STRING = RexxTokenType("STRING")
}

class RexxTokenType(debugName: String) : IElementType(debugName, RexxLanguage)

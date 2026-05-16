package org.lang.rexx

import com.intellij.psi.tree.TokenSet

object RexxTokenSets {
    @JvmField val COMMENTS: TokenSet = TokenSet.create(RexxTokenTypes.COMMENT)
    @JvmField val STRINGS: TokenSet = TokenSet.create(RexxTokenTypes.STRING)
    @JvmField val NUMBERS: TokenSet = TokenSet.create(RexxTokenTypes.NUMBER)
    @JvmField val KEYWORDS: TokenSet = TokenSet.create(RexxTokenTypes.KEYWORD)
    @JvmField val OPERATORS: TokenSet = TokenSet.create(
        RexxTokenTypes.OPERATOR,
        RexxTokenTypes.ASSIGNMENT_OPERATOR,
        RexxTokenTypes.COMPARISON_OPERATOR,
        RexxTokenTypes.LOGICAL_OPERATOR,
        RexxTokenTypes.ARITHMETIC_OPERATOR,
    )
    @JvmField val PUNCTUATION: TokenSet = TokenSet.create(RexxTokenTypes.PUNCTUATION)
    @JvmField val LITERALS: TokenSet = TokenSet.create(RexxTokenTypes.STRING, RexxTokenTypes.NUMBER)
}

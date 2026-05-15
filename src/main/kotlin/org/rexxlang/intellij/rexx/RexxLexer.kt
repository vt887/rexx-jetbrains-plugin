package org.rexxlang.intellij.rexx

import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

open class RexxLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset = 0
    private var endOffset = 0
    private var tokenStart = 0
    private var tokenEnd = 0
    private var tokenType: IElementType? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        tokenStart = startOffset
        tokenEnd = startOffset
        locateToken()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = tokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun advance() {
        tokenStart = tokenEnd
        locateToken()
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset

    private fun locateToken() {
        if (tokenStart >= endOffset) {
            tokenType = null
            tokenEnd = tokenStart
            return
        }

        val current = buffer[tokenStart]
        tokenType = when {
            current.isWhitespace() -> {
                tokenEnd = scanWhile(tokenStart, Char::isWhitespace)
                TokenType.WHITE_SPACE
            }
            current == '/' && charAt(tokenStart + 1) == '*' -> {
                tokenEnd = scanComment(tokenStart)
                RexxTokenTypes.COMMENT
            }
            current == '\'' || current == '"' -> {
                tokenEnd = scanString(tokenStart, current)
                RexxTokenTypes.STRING
            }
            current.isDigit() || (current == '.' && charAt(tokenStart + 1)?.isDigit() == true) -> {
                tokenEnd = scanNumber(tokenStart)
                RexxTokenTypes.NUMBER
            }
            isSymbolStart(current) -> {
                tokenEnd = scanWhile(tokenStart, ::isSymbolPart)
                if (isKeyword(buffer.subSequence(tokenStart, tokenEnd).toString())) {
                    RexxTokenTypes.KEYWORD
                } else {
                    RexxTokenTypes.IDENTIFIER
                }
            }
            scanOperator(tokenStart) > tokenStart -> {
                tokenEnd = scanOperator(tokenStart)
                RexxTokenTypes.OPERATOR
            }
            current in PUNCTUATION -> {
                tokenEnd = tokenStart + 1
                RexxTokenTypes.PUNCTUATION
            }
            else -> {
                tokenEnd = tokenStart + 1
                TokenType.BAD_CHARACTER
            }
        }
    }

    private fun scanComment(offset: Int): Int {
        var index = offset + 2
        var depth = 1

        while (index < endOffset && depth > 0) {
            when {
                charAt(index) == '/' && charAt(index + 1) == '*' -> {
                    depth++
                    index += 2
                }
                charAt(index) == '*' && charAt(index + 1) == '/' -> {
                    depth--
                    index += 2
                }
                else -> index++
            }
        }

        return index
    }

    private fun scanString(offset: Int, quote: Char): Int {
        var index = offset + 1

        while (index < endOffset) {
            if (buffer[index] == quote) {
                if (charAt(index + 1) == quote) {
                    index += 2
                } else {
                    return index + 1
                }
            } else {
                index++
            }
        }

        return index
    }

    private fun scanNumber(offset: Int): Int {
        var index = offset

        if (charAt(index) == '.') {
            index++
        }

        while (charAt(index)?.isDigit() == true) {
            index++
        }

        if (charAt(index) == '.' && charAt(index + 1)?.isDigit() == true) {
            index++
            while (charAt(index)?.isDigit() == true) {
                index++
            }
        }

        if (charAt(index)?.uppercaseChar() == 'E') {
            val exponentStart = index
            index++
            if (charAt(index) == '+' || charAt(index) == '-') {
                index++
            }
            val digitsStart = index
            while (charAt(index)?.isDigit() == true) {
                index++
            }
            if (digitsStart == index) {
                return exponentStart
            }
        }

        return index
    }

    private fun scanOperator(offset: Int): Int {
        for (operator in MULTI_CHAR_OPERATORS) {
            if (matches(offset, operator)) {
                return offset + operator.length
            }
        }

        return if (charAt(offset) in SINGLE_CHAR_OPERATORS) offset + 1 else offset
    }

    private fun scanWhile(offset: Int, predicate: (Char) -> Boolean): Int {
        var index = offset
        while (index < endOffset && predicate(buffer[index])) {
            index++
        }
        return index
    }

    private fun matches(offset: Int, value: String): Boolean {
        if (offset + value.length > endOffset) {
            return false
        }

        for (index in value.indices) {
            if (buffer[offset + index] != value[index]) {
                return false
            }
        }

        return true
    }

    private fun charAt(offset: Int): Char? = if (offset in startOffset until endOffset) buffer[offset] else null

    private fun isKeyword(value: String): Boolean = value.uppercase() in KEYWORDS

    private fun isSymbolStart(value: Char): Boolean = value.isLetter() || value in SYMBOL_START

    private fun isSymbolPart(value: Char): Boolean = value.isLetterOrDigit() || value in SYMBOL_PART

    private companion object {
        private val KEYWORDS = setOf(
            "ADDRESS",
            "ARG",
            "BY",
            "CALL",
            "DO",
            "DROP",
            "ELSE",
            "END",
            "EXIT",
            "EXPOSE",
            "FOR",
            "FOREVER",
            "IF",
            "INTERPRET",
            "ITERATE",
            "LEAVE",
            "NOP",
            "NUMERIC",
            "OPTIONS",
            "OTHERWISE",
            "PARSE",
            "PROCEDURE",
            "PULL",
            "PUSH",
            "QUEUE",
            "RETURN",
            "SAY",
            "SELECT",
            "SIGNAL",
            "THEN",
            "TO",
            "TRACE",
            "UNTIL",
            "UPPER",
            "VALUE",
            "VAR",
            "WHEN",
            "WHILE",
            "WITH",
        )
        private val MULTI_CHAR_OPERATORS = listOf("\\==", "¬==", "**", "//", "||", "<>", "><", "<=", ">=", "\\=", "¬=", "/=", "==")
        private val PUNCTUATION = setOf('(', ')', ',', ';', ':', '.')
        private val SINGLE_CHAR_OPERATORS = setOf('+', '-', '*', '/', '%', '=', '<', '>', '&', '|', '\\', '¬', '^')
        private val SYMBOL_PART = setOf('.', '!', '?', '_', '@', '#', '$')
        private val SYMBOL_START = SYMBOL_PART - '.'
    }
}

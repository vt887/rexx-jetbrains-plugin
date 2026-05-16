package org.lang.rexx

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.tree.IElementType

internal data class RexxKeywordCompletion(
    val keyword: String,
    val tailText: String? = null,
)

internal val REXX_KEYWORD_COMPLETIONS = listOf(
    RexxKeywordCompletion("ADDRESS", " environment"),
    RexxKeywordCompletion("ARG"),
    RexxKeywordCompletion("CALL", " routine"),
    RexxKeywordCompletion("DO", " ... END"),
    RexxKeywordCompletion("DROP", " variable"),
    RexxKeywordCompletion("ELSE"),
    RexxKeywordCompletion("END"),
    RexxKeywordCompletion("EXIT"),
    RexxKeywordCompletion("IF", " ... THEN"),
    RexxKeywordCompletion("INTERPRET", " expression"),
    RexxKeywordCompletion("ITERATE", " loop"),
    RexxKeywordCompletion("LEAVE", " loop"),
    RexxKeywordCompletion("NOP"),
    RexxKeywordCompletion("NUMERIC", " DIGITS | FORM | FUZZ"),
    RexxKeywordCompletion("OTHERWISE", " default branch"),
    RexxKeywordCompletion("PARSE", " template"),
    RexxKeywordCompletion("PROCEDURE"),
    RexxKeywordCompletion("PULL"),
    RexxKeywordCompletion("PUSH"),
    RexxKeywordCompletion("QUEUE"),
    RexxKeywordCompletion("RETURN", " value"),
    RexxKeywordCompletion("SAY", " expression"),
    RexxKeywordCompletion("SELECT", " ... WHEN ... END"),
    RexxKeywordCompletion("SIGNAL", " label"),
    RexxKeywordCompletion("THEN"),
    RexxKeywordCompletion("TRACE", " setting"),
    RexxKeywordCompletion("WHEN", " condition"),
    RexxKeywordCompletion("WHILE", " condition"),
)

internal fun isRexxKeywordCompletionAllowed(fileLanguage: com.intellij.lang.Language, tokenType: IElementType?): Boolean =
    fileLanguage.isKindOf(RexxLanguage) && tokenType != RexxTokenTypes.COMMENT && tokenType != RexxTokenTypes.STRING

internal fun rexxKeywordLookupElements(): List<LookupElementBuilder> =
    REXX_KEYWORD_COMPLETIONS.map { keyword ->
        LookupElementBuilder.create(keyword.keyword)
            .withBoldness(true)
            .let { builder ->
                keyword.tailText?.let { builder.withTailText(it, true) } ?: builder
            }
    }

internal fun tokenTypeAtCompletion(parameters: CompletionParameters): IElementType? {
    val originalFile = parameters.originalFile
    val offset = parameters.offset.coerceAtMost(originalFile.textLength)
    val positionToken = parameters.position.node?.elementType
    if (positionToken == RexxTokenTypes.COMMENT || positionToken == RexxTokenTypes.STRING) {
        return positionToken
    }

    if (offset <= 0) {
        return positionToken
    }

    return originalFile.findElementAt(offset - 1)?.node?.elementType ?: positionToken
}

class RexxCompletionContributor : CompletionContributor() {
    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val file = parameters.originalFile
        val tokenType = tokenTypeAtCompletion(parameters)
        if (!isRexxKeywordCompletionAllowed(file.language, tokenType)) {
            return
        }

        val caseInsensitiveResult = result.caseInsensitive()
        rexxKeywordLookupElements().forEach(caseInsensitiveResult::addElement)
    }
}

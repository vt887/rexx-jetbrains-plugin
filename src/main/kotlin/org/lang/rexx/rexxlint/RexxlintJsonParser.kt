package org.lang.rexx.rexxlint

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.lang.annotation.HighlightSeverity

class RexxlintJsonParser(
    private val objectMapper: ObjectMapper = ObjectMapper(),
) {
    fun parseDiagnostics(json: String): RexxlintCommandResult<List<RexxlintDiagnostic>> =
        try {
            val root = objectMapper.readTree(json)
            RexxlintCommandResult.Success(extractDiagnostics(root))
        } catch (error: Exception) {
            RexxlintCommandResult.Failure(
                code = RexxlintFailureCode.INVALID_OUTPUT,
                message = "rexxlint returned invalid JSON diagnostics.",
                details = error.message,
            )
        }

    private fun extractDiagnostics(root: JsonNode): List<RexxlintDiagnostic> {
        val nodes = when {
            root.isArray -> root.toList()
            root.has("diagnostics") && root.get("diagnostics").isArray -> root.get("diagnostics").toList()
            root.has("issues") && root.get("issues").isArray -> root.get("issues").toList()
            else -> emptyList()
        }
        return nodes.mapNotNull(::toDiagnostic)
    }

    private fun toDiagnostic(node: JsonNode): RexxlintDiagnostic? {
        val startLine = node.intValue("line") ?: node.path("range").path("start").intValue("line") ?: return null
        val startColumn = node.intValue("column") ?: node.path("range").path("start").intValue("column") ?: 1
        val endLine = node.intValue("endLine") ?: node.path("range").path("end").intValue("line")
        val endColumn = node.intValue("endColumn") ?: node.path("range").path("end").intValue("column")
        val message = node.textValue("message") ?: return null
        val severity = severityOf(node.textValue("severity"))
        return RexxlintDiagnostic(
            severity = severity,
            message = message,
            line = startLine,
            column = startColumn,
            endLine = endLine,
            endColumn = endColumn,
            code = node.textValue("code"),
        )
    }

    private fun severityOf(value: String?): HighlightSeverity =
        when (value?.lowercase()) {
            "error" -> HighlightSeverity.ERROR
            "warning", "warn" -> HighlightSeverity.WARNING
            "info", "information" -> HighlightSeverity.INFORMATION
            else -> HighlightSeverity.WARNING
        }

    private fun JsonNode.intValue(field: String): Int? = get(field)?.takeIf(JsonNode::canConvertToInt)?.asInt()

    private fun JsonNode.textValue(field: String): String? = get(field)?.takeIf(JsonNode::isTextual)?.asText()
}

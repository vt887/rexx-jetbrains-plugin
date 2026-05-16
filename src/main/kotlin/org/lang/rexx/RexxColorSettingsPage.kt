package org.lang.rexx

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class RexxColorSettingsPage : ColorSettingsPage {
    override fun getDisplayName(): String = "Rexx"

    override fun getIcon(): Icon = RexxIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = RexxSyntaxHighlighter()

    override fun getDemoText(): String =
        """
        /* Rexx sample */
        total = 0
        do i = 1 to 5
            total = total + i
            say "i=" i " total=" total
        end
        
        if total >= 10 then
            say 'done'
        """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    private companion object {
        private val DESCRIPTORS =
            arrayOf(
                AttributesDescriptor("Comment", RexxTextAttributes.COMMENT),
                AttributesDescriptor("Comment tag", RexxTextAttributes.COMMENT_TAG),
                AttributesDescriptor("Keyword", RexxTextAttributes.KEYWORD),
                AttributesDescriptor("Variable", RexxTextAttributes.VARIABLE),
                AttributesDescriptor("Function call", RexxTextAttributes.FUNCTION),
                AttributesDescriptor("Built-in function", RexxTextAttributes.BUILTIN_FUNCTION),
                AttributesDescriptor("Label", RexxTextAttributes.LABEL),
                AttributesDescriptor("String", RexxTextAttributes.STRING),
                AttributesDescriptor("Number", RexxTextAttributes.NUMBER),
                AttributesDescriptor("Operator", RexxTextAttributes.OPERATOR),
                AttributesDescriptor("Assignment operator", RexxTextAttributes.ASSIGNMENT_OPERATOR),
                AttributesDescriptor("Comparison operator", RexxTextAttributes.COMPARISON_OPERATOR),
                AttributesDescriptor("Logical operator", RexxTextAttributes.LOGICAL_OPERATOR),
                AttributesDescriptor("Arithmetic operator", RexxTextAttributes.ARITHMETIC_OPERATOR),
                AttributesDescriptor("Punctuation", RexxTextAttributes.PUNCTUATION),
                AttributesDescriptor("Bad character", RexxTextAttributes.BAD_CHARACTER),
            )
    }
}

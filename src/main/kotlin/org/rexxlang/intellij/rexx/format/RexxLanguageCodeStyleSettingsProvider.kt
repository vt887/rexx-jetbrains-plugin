package org.rexxlang.intellij.rexx.format

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import org.rexxlang.intellij.rexx.RexxLanguage

class RexxLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun createConfigurable(settings: CodeStyleSettings, modelSettings: CodeStyleSettings): CodeStyleConfigurable =
        object : CodeStyleAbstractConfigurable(settings, modelSettings, "Rexx") {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel =
                object : TabbedLanguageCodeStylePanel(RexxLanguage, currentSettings, settings) {}
        }

    override fun customizeSettings(
        consumer: CodeStyleSettingsCustomizable,
        settingsType: SettingsType,
    ) {
        if (settingsType == SettingsType.INDENT_SETTINGS) {
            consumer.showStandardOptions("INDENT_SIZE", "TAB_SIZE", "USE_TAB_CHARACTER", "KEEP_INDENTS_ON_EMPTY_LINES")
        }
    }

    override fun getCodeSample(settingsType: SettingsType): String = """
        /* Rexx formatting sample */
        total = 0
        do i = 1 to 5
            total = total + i
        end

        if total > 10 then
            say \"large\"
    """.trimIndent()

    override fun getLanguage(): Language = RexxLanguage

    override fun customizeDefaults(
        commonSettings: CommonCodeStyleSettings,
        indentOptions: CommonCodeStyleSettings.IndentOptions,
    ) {
        indentOptions.INDENT_SIZE = 4
        indentOptions.TAB_SIZE = 4
    }
}

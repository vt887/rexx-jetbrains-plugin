package org.lang.rexx.format

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import org.lang.rexx.RexxLanguage

class RexxLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings,
    ): CodeStyleConfigurable =
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

    override fun getCodeSample(settingsType: SettingsType): String =
        """
        /* Rexx formatting sample */
        parse arg inputValue

        if inputValue = '' then do
          say 'No input provided.'
          exit 1
        end

        select
          when inputValue = 'A' then do
            say 'Option A selected.'
          end
          when inputValue = 'B' then do
            say 'Option B selected.'
          end
          otherwise do
            say 'Unknown option:' inputValue
          end
        end /* select */

        total = 0
        do i = 1 to 5
          total = total + i
        end /* do */

        say 'Total:' total
        exit 0
        """.trimIndent()

    override fun getLanguage(): Language = RexxLanguage

    override fun customizeDefaults(
        commonSettings: CommonCodeStyleSettings,
        indentOptions: CommonCodeStyleSettings.IndentOptions,
    ) {
        indentOptions.INDENT_SIZE = 3
        indentOptions.TAB_SIZE = 3
        indentOptions.USE_TAB_CHARACTER = false
    }
}

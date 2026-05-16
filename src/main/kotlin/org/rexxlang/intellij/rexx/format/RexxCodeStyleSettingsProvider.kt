package org.rexxlang.intellij.rexx.format

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.openapi.options.Configurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import org.rexxlang.intellij.rexx.RexxLanguage

class RexxCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    override fun createSettingsPage(settings: CodeStyleSettings, originalSettings: CodeStyleSettings): Configurable =
        object : CodeStyleAbstractConfigurable(settings, originalSettings, "Rexx") {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel =
                object : TabbedLanguageCodeStylePanel(RexxLanguage, currentSettings, settings) {}
        }

    override fun getConfigurableDisplayName(): String = "Rexx"
}

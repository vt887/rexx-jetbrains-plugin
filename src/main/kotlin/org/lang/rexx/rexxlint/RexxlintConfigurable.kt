package org.lang.rexx.rexxlint

import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel

class RexxlintConfigurable : BoundSearchableConfigurable("Rexx Lint", "settings.rexxlint") {
    private val settings: RexxlintSettingsState
        get() = service()

    override fun createPanel() =
        panel {
            row("Executable path") {
                cell(
                    TextFieldWithBrowseButton().apply {
                        addBrowseFolderListener(
                            "Select rexxlint executable",
                            "Choose the rexxlint binary to use for linting and formatting.",
                            null,
                            FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(),
                        )
                    },
                ).align(AlignX.FILL)
                    .bindText(settings::executablePath)
            }
            row {
                cell(
                    JBLabel(
                        "If empty, the plugin searches PATH and common install locations such as /opt/homebrew/bin/rexxlint and /usr/local/bin/rexxlint.",
                    ),
                )
            }
        }
}

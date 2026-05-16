package org.lang.rexx.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.util.InvalidDataException
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.xmlb.XmlSerializer
import com.intellij.util.xmlb.annotations.Attribute
import org.jdom.Element
import org.lang.rexx.RexxFileType
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.util.Locale
import javax.swing.JComponent
import javax.swing.JPanel

internal const val DEFAULT_REXX_INTERPRETER = "rexx"
private val REXX_INTERPRETER_CANDIDATES = listOf("rexx", "regina", "oorexx", "ooRexx", "brexx")
private val REXX_EXTENSIONS = setOf("rexx", "rex", "rx")

internal fun validateRexxRunConfiguration(
    project: Project?,
    interpreterPath: String,
    scriptPath: String,
    useCurrentFile: Boolean,
) {
    if (interpreterPath.isBlank()) {
        throw RuntimeConfigurationError("Interpreter path must not be blank.")
    }

    if (useCurrentFile) {
        val currentProject = project ?: throw RuntimeConfigurationError("Project is unavailable.")
        resolveCurrentRexxFilePath(currentProject)
            ?: throw RuntimeConfigurationError("No active Rexx file in editor.")
        return
    }

    if (scriptPath.isBlank()) {
        throw RuntimeConfigurationError("Script path must not be blank.")
    }

    val script =
        try {
            Path.of(scriptPath)
        } catch (_: InvalidPathException) {
            throw RuntimeConfigurationError("Script path is not a valid file path.")
        }

    if (!Files.isRegularFile(script) || !Files.isReadable(script)) {
        throw RuntimeConfigurationError("Script file does not exist or is not readable: $scriptPath")
    }
}

internal fun validateRexxRunConfiguration(
    interpreterPath: String,
    scriptPath: String,
) = validateRexxRunConfiguration(
    project = null,
    interpreterPath = interpreterPath,
    scriptPath = scriptPath,
    useCurrentFile = false,
)

internal fun resolveCurrentRexxFilePath(project: Project): String? {
    val selected = FileEditorManager.getInstance(project).selectedFiles.firstOrNull() ?: return null
    val extension = selected.extension?.lowercase(Locale.ROOT)
    if (selected.fileType != RexxFileType.INSTANCE && extension !in REXX_EXTENSIONS) {
        return null
    }
    return selected.path
}

internal fun detectSystemRexxInterpreters(pathEnv: String? = System.getenv("PATH")): List<String> {
    if (pathEnv.isNullOrBlank()) return listOf(DEFAULT_REXX_INTERPRETER)

    val found = linkedSetOf<String>()
    val separator = java.io.File.pathSeparatorChar
    for (entry in pathEnv.split(separator)) {
        if (entry.isBlank()) continue
        val dir =
            try {
                Path.of(entry)
            } catch (_: InvalidPathException) {
                continue
            }
        if (!Files.isDirectory(dir)) continue

        for (candidate in REXX_INTERPRETER_CANDIDATES) {
            val executable = dir.resolve(candidate)
            if (Files.isRegularFile(executable) && Files.isExecutable(executable)) {
                found += executable.toString()
            }
        }
    }

    if (found.isEmpty()) {
        return listOf(DEFAULT_REXX_INTERPRETER)
    }

    return found.sortedBy { it.lowercase(Locale.ROOT) }
}

class RexxRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String,
) : RunConfigurationBase<Element>(project, factory, name) {
    @get:Attribute("interpreterPath")
    var interpreterPath: String = DEFAULT_REXX_INTERPRETER

    @get:Attribute("scriptPath")
    var scriptPath: String = ""

    @get:Attribute("useCurrentFile")
    var useCurrentFile: Boolean = false

    @get:Attribute("workingDirectory")
    var workingDirectory: String = ""

    @get:Attribute("programArguments")
    var programArguments: String = ""

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = RexxRunConfigurationEditor()

    override fun checkConfiguration() {
        validateRexxRunConfiguration(project, interpreterPath, scriptPath, useCurrentFile)
    }

    override fun getState(
        executor: Executor,
        environment: ExecutionEnvironment,
    ): RexxCommandLineState = RexxCommandLineState(environment, this)

    @Throws(InvalidDataException::class)
    override fun readExternal(element: Element) {
        super.readExternal(element)
        XmlSerializer.deserializeInto(this, element)
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        XmlSerializer.serializeInto(this, element)
    }
}

private class RexxRunConfigurationEditor : SettingsEditor<RexxRunConfiguration>() {
    private val interpreterField = ComboBox<String>()
    private val runCurrentFileCheckbox = JBCheckBox("Run current file from editor")
    private val scriptField = JBTextField()
    private val workingDirectoryField = JBTextField()
    private val argumentsField = JBTextField()

    init {
        interpreterField.isEditable = true
    }

    override fun resetEditorFrom(configuration: RexxRunConfiguration) {
        refillInterpreterChoices(configuration.interpreterPath)
        interpreterField.editor.item = configuration.interpreterPath
        runCurrentFileCheckbox.isSelected = configuration.useCurrentFile
        scriptField.text = configuration.scriptPath
        workingDirectoryField.text = configuration.workingDirectory
        argumentsField.text = configuration.programArguments
        scriptField.isEnabled = !configuration.useCurrentFile
    }

    @Throws(ConfigurationException::class)
    override fun applyEditorTo(configuration: RexxRunConfiguration) {
        val selected = (interpreterField.editor.item as? String).orEmpty().trim()
        configuration.interpreterPath = selected
        configuration.useCurrentFile = runCurrentFileCheckbox.isSelected
        configuration.scriptPath = scriptField.text.trim()
        configuration.workingDirectory = workingDirectoryField.text.trim()
        configuration.programArguments = argumentsField.text.trim()
    }

    override fun createEditor(): JComponent = panel

    private val panel: JPanel =
        FormBuilder
            .createFormBuilder()
            .addLabeledComponent("Interpreter:", interpreterField)
            .addComponent(runCurrentFileCheckbox)
            .addLabeledComponent("Script:", scriptField)
            .addLabeledComponent("Working directory:", workingDirectoryField)
            .addLabeledComponent("Arguments:", argumentsField)
            .addComponentFillVertically(JPanel(), 0)
            .panel

    init {
        runCurrentFileCheckbox.addActionListener {
            scriptField.isEnabled = !runCurrentFileCheckbox.isSelected
        }
    }

    private fun refillInterpreterChoices(currentValue: String) {
        val choices = linkedSetOf<String>()
        choices += detectSystemRexxInterpreters()
        if (currentValue.isNotBlank()) {
            choices += currentValue
        }

        interpreterField.removeAllItems()
        for (choice in choices) {
            interpreterField.addItem(choice)
        }
    }
}

package org.rexxlang.intellij.rexx.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.configurations.RuntimeConfigurationException
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.InvalidDataException
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.xmlb.XmlSerializer
import com.intellij.util.xmlb.annotations.Attribute
import org.jdom.Element
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.Path
import javax.swing.JComponent
import javax.swing.JPanel

internal const val DEFAULT_REXX_INTERPRETER = "rexx"

internal fun validateRexxRunConfiguration(interpreterPath: String, scriptPath: String) {
    if (interpreterPath.isBlank()) {
        throw RuntimeConfigurationError("Interpreter path must not be blank.")
    }

    if (scriptPath.isBlank()) {
        throw RuntimeConfigurationError("Script path must not be blank.")
    }

    val script = try {
        Path.of(scriptPath)
    } catch (_: InvalidPathException) {
        throw RuntimeConfigurationError("Script path is not a valid file path.")
    }

    if (!Files.exists(script)) {
        throw RuntimeConfigurationError("Script file does not exist: $scriptPath")
    }
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

    @get:Attribute("workingDirectory")
    var workingDirectory: String = ""

    @get:Attribute("programArguments")
    var programArguments: String = ""

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = RexxRunConfigurationEditor()

    override fun checkConfiguration() {
        validateRexxRunConfiguration(interpreterPath, scriptPath)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RexxCommandLineState =
        RexxCommandLineState(environment, this)

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
    private val interpreterField = JBTextField()
    private val scriptField = JBTextField()
    private val workingDirectoryField = JBTextField()
    private val argumentsField = JBTextField()

    override fun resetEditorFrom(configuration: RexxRunConfiguration) {
        interpreterField.text = configuration.interpreterPath
        scriptField.text = configuration.scriptPath
        workingDirectoryField.text = configuration.workingDirectory
        argumentsField.text = configuration.programArguments
    }

    @Throws(ConfigurationException::class)
    override fun applyEditorTo(configuration: RexxRunConfiguration) {
        configuration.interpreterPath = interpreterField.text.trim()
        configuration.scriptPath = scriptField.text.trim()
        configuration.workingDirectory = workingDirectoryField.text.trim()
        configuration.programArguments = argumentsField.text.trim()
    }

    override fun createEditor(): JComponent = panel

    private val panel: JPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent("Interpreter:", interpreterField)
        .addLabeledComponent("Script:", scriptField)
        .addLabeledComponent("Working directory:", workingDirectoryField)
        .addLabeledComponent("Arguments:", argumentsField)
        .addComponentFillVertically(JPanel(), 0)
        .panel
}

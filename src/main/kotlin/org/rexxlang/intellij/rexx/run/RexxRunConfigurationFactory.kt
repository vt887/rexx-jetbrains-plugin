package org.rexxlang.intellij.rexx.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project

class RexxRunConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getId(): String = "Rexx"

    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        RexxRunConfiguration(project, this, "Rexx")
}

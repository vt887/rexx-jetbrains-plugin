package org.lang.rexx.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import org.lang.rexx.RexxIcons
import javax.swing.Icon

class RexxRunConfigurationType : ConfigurationType {
    private val factory = RexxRunConfigurationFactory(this)

    override fun getDisplayName(): String = "Rexx"

    override fun getConfigurationTypeDescription(): String = "Run Rexx scripts with a configured interpreter"

    override fun getIcon(): Icon = RexxIcons.FILE

    override fun getId(): String = "RexxRunConfiguration"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(factory)
}

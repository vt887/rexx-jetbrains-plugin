package org.lang.rexx

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class RexxFileType private constructor() : LanguageFileType(RexxLanguage) {
    override fun getName(): String = "Rexx"

    override fun getDescription(): String = "Rexx program"

    override fun getDefaultExtension(): String = "rexx"

    override fun getIcon(): Icon = RexxIcons.FILE

    companion object {
        @JvmField
        val INSTANCE = RexxFileType()
    }
}

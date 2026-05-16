package org.lang.rexx

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import javax.swing.Icon

class RexxFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, RexxLanguage) {
    override fun getFileType() = RexxFileType.INSTANCE

    override fun toString(): String = "Rexx File"

    override fun getIcon(flags: Int): Icon = RexxIcons.FILE
}

package org.lang.rexx.rexxlint

import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project

object RexxlintNotifier {
    fun notifyFormattingFailure(
        project: Project,
        message: String,
    ) {
        Notifications.Bus.notify(
            com.intellij.notification.Notification(
                "Rexxlint",
                "Rexx formatting failed",
                message,
                NotificationType.WARNING,
            ),
            project,
        )
    }
}

package com.logicalclocks.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

/**
 * Creates notification in IDE
 */

public class PluginNoticifaction {


private static final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("Hopsworks Plugin", NotificationDisplayType.BALLOON, true);


public static Notification notify(Project project, String content) {
    final Notification notification = NOTIFICATION_GROUP.createNotification(content, NotificationType.INFORMATION);
    notification.notify(project);
    return notification;
}

}

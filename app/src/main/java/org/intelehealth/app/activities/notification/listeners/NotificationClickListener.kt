package org.intelehealth.app.activities.notification.listeners

import org.intelehealth.app.models.NotificationModel

interface NotificationClickListener {
    fun deleteNotification(notificationModel: NotificationModel, position: Int)

    fun openNotification(notificationModel: NotificationModel, position: Int)
}
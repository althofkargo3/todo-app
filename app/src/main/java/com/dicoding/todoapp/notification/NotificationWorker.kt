package com.dicoding.todoapp.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.ui.detail.DetailTaskActivity
import com.dicoding.todoapp.utils.CHANNEL_ID
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.NOTIFICATION_CHANNEL_ID
import com.dicoding.todoapp.utils.TASK_ID

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID) ?: ""

    private fun getPendingIntent(task: Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun doWork(): Result {

        //TODO 14 : If notification preference on, get nearest active task from repository and show notification with pending intent

        return try {

            val repository = TaskRepository.getInstance(applicationContext)
            val activeTask = repository.getNearestActiveTask() as Task?

            if (activeTask != null) {

                val pendingIntent = getPendingIntent(activeTask)

                val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
                    priority = NotificationCompat.PRIORITY_DEFAULT
                    setSmallIcon(R.drawable.ic_notifications)
                    setContentTitle(activeTask.title)
                    setContentText(
                        applicationContext.getString(
                            R.string.notify_content,
                            DateConverter.convertMillisToString(activeTask.dueDateMillis)
                        )
                    )
                    setContentIntent(pendingIntent)
                    setAutoCancel(true)
                }
                with(NotificationManagerCompat.from(applicationContext)) {
                    notify(123, builder.build())
                }

                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Error) {
            Result.failure()
        }
    }

}

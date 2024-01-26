package com.ozancansari.mobilprogramlamaodev

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.time.Duration
import java.time.LocalDateTime
import java.util.Timer
import java.util.TimerTask
import kotlin.time.Duration.Companion.days

class CounterService : Service() {
    companion object {
        // Channel ID for notifications
        const val CHANNEL_ID = "Stopwatch_Notifications"

        // Service Actions
        const val START = "START"
        const val MOVE_TO_FOREGROUND = "MOVE_TO_FOREGROUND"
        const val MOVE_TO_BACKGROUND = "MOVE_TO_BACKGROUND"

        // Intent Extras
        const val STOPWATCH_ACTION = "STOPWATCH_ACTION"
    }

    private var isStopWatchRunning = false

    private var updateTimer = Timer()
    private var stopwatchTimer = Timer()

    private lateinit var notificationManager: NotificationManager

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createChannel() //Bildirim Kanalı Oluşturma
        getNotificationManager() // Bildirim yöneticisini alır.

        when (intent?.getStringExtra(STOPWATCH_ACTION)!!) { // intent ile gelen eyleme göre işlemi başlatır
            START -> startStopwatch()
            MOVE_TO_FOREGROUND -> moveToForeground()
            MOVE_TO_BACKGROUND -> moveToBackground()
        }

        return START_STICKY
    }

    /** Sayaç çalışıyorsa, sayaç zamanlayıcısını iptal eder, ön planda çalışma bildirimi oluşturur ve arka planda çalışacak zamanlayıcıyı başlatır. */
    private fun moveToForeground()
    {

        if (isStopWatchRunning) {
            stopwatchTimer.cancel()
            startForeground(1, buildNotification("Uygulama arka planda çalışmakta"))

            updateTimer = Timer()

            updateTimer.scheduleAtFixedRate(object : TimerTask() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun run() {
                    checkExamDate()
                }
            }, 0, 1000)
        }
    }
    /**Zamanlayıcıyı iptal eder ve ön plandaki bildirimi durdurur.*/
    private fun moveToBackground()
    {
        updateTimer.cancel()
        stopForeground(true)
    }

    /**Sayaç çalıştırma işlemlerini başlatır.*/
    private fun startStopwatch()
    {
        isStopWatchRunning = true;
        stopwatchTimer = Timer()
        stopwatchTimer.scheduleAtFixedRate(object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                checkExamDate()
            }
        }, 0, 1000)
    }

    /**bildirim kanalı oluşturma*/
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Stopwatch",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.setSound(null, null)
            notificationChannel.setShowBadge(true)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationManager() {
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
    }

    /**bildirim oluşturma.Başlık, metin, renk, simge ve tıklama işlemleri gibi özellikleri içerir. */
    private fun buildNotification(text : String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("UYARI")
            .setOngoing(true)
            .setContentText(text)
            .setColorized(true)
            .setColor(Color.parseColor("#3700B3"))
            .setSmallIcon(R.drawable.notification)
            .setOnlyAlertOnce(true)
            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .build()
    }

    /**Bildirimi günceller.*/
    private fun updateNotification(text : String) {
        notificationManager.notify(
            1,
            buildNotification(text)
        )
    }

    /**Belirli bir sınavın tarihine göre zamanı kontrol eder ve kullanıcıyı bilgilendirme bildirimleri gönderir. Şu an sadece 30 dakika, 1 saat, 15 dakika ve 5 dakika kala bildirimleri içeriyor. */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkExamDate() {
        for(item in PrefUtil.getExams(MainActivity.instance!!)) {
            val difference = Duration.between(LocalDateTime.now(), item.date).toMinutes()
            //if(difference.days.inWholeDays.toInt() <= 1 && difference >59  )

            if (difference in 28..29) {
                updateNotification("${item.name} sınavına 30 dakika KALDI! UNUTMA!!")
            }
            if (difference in 58..59) {
                updateNotification("${item.name} sınavına 1 saat KALDI! UNUTMA!!")
            }
            if (difference in 13..14) {
                updateNotification("${item.name} sınavına 15 dakika KALDI! UNUTMA!!")
            }
            if (difference in 3..4) {
                updateNotification("${item.name} sınavına 5 dakika KALDI! UNUTMA!!")
            }
        }
    }

}

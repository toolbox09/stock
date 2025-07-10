package com.example.stock

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager


private var currentRingtone: Ringtone? = null

fun playSystemWarningSound(context: Context) {
    try {
        // 이전에 재생중인 소리가 있다면 중지
        currentRingtone?.stop()

        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(context, notificationSoundUri)
        ringtone.play()
        currentRingtone = ringtone // 현재 재생 객체 저장

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

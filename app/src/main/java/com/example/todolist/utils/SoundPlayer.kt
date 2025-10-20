package com.example.todolist.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.todolist.R

object SoundPlayer {

    private var soundPool: SoundPool? = null
    private var soundAddId: Int = 0
    private var soundFinishId: Int = 0
    private var soundDeleteId: Int = 0
    private var isLoaded = false

    fun loadSounds(context: Context) {
        if (isLoaded) return // Chỉ load một lần

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool?.setOnLoadCompleteListener { _, _, _ ->
            // Có thể dùng để kiểm tra khi nào load xong, nhưng ở đây không cần thiết
        }

        soundAddId = soundPool?.load(context, R.raw.sound_add, 1) ?: 0
        soundFinishId = soundPool?.load(context, R.raw.sound_finish, 1) ?: 0
        soundDeleteId = soundPool?.load(context, R.raw.sound_delete, 1) ?: 0
        isLoaded = true
    }

    fun playSound(soundId: Int) {
        soundPool?.play(soundId, 1f, 1f, 0, 0, 1f)
    }

    // Các hàm tiện ích
    fun playAddSound() = playSound(soundAddId)
    fun playFinishSound() = playSound(soundFinishId)
    fun playDeleteSound() = playSound(soundDeleteId)

    fun release() {
        soundPool?.release()
        soundPool = null
        isLoaded = false
    }
}
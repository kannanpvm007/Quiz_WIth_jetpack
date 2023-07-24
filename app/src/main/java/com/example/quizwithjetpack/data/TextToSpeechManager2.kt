package com.example.quizwithjetpack.data

/**
 * Created by kannanpvm007 on 24-07-2023.
 */
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.speech.tts.TextToSpeech
import java.util.*

class TextToSpeechManager2(private val context: Context, private val onInit: (TextToSpeech) -> Unit) {
    private var textToSpeech: TextToSpeech? = null
    private var mediaPlayer: MediaPlayer? = null
    private var isPaused: Boolean = false

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.getDefault()
                onInit(textToSpeech!!)
            }
        }
    }

    fun speak(text: String) {
        if (isPaused) {
            mediaPlayer?.start()
            isPaused = false
        } else {
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        isPaused = true
    }

    fun stop() {
        textToSpeech?.stop()
        mediaPlayer?.stop()
        isPaused = false
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        mediaPlayer?.release()
        mediaPlayer = null
        isPaused = false
    }

    private fun createMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
                mediaPlayer?.setAudioAttributes(audioAttributes)
            } else {
                mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            }
        }
    }
}

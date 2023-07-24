package com.example.quizwithjetpack.data

/**
 * Created by kannanpvm007 on 24-07-2023.
 */
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.speech.tts.TextToSpeech
import java.util.*
import android.os.Handler
import android.os.Looper


class TextToSpeechManager(private val context: Context, private val onInit: (TextToSpeech) -> Unit) {
    private var textToSpeech: TextToSpeech? = null
    private var isPaused: Boolean = false
    private val handler = Handler(Looper.getMainLooper())

    private var audioFocusRequest: AudioFocusRequest? = null


    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.getDefault()
                onInit(textToSpeech!!)
            }
        }
    }

    fun speak(text: String) {
        if (isPaused && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech?.speak(text, TextToSpeech.QUEUE_ADD, null, null)
            isPaused = false
        } else {
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
    fun pause1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
            isPaused = true
        }
    }
    fun pause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                .setAudioAttributes(audioAttributes)
                .setOnAudioFocusChangeListener { }
                .build()

            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.requestAudioFocus(audioFocusRequest!!)
            isPaused = true
        }
    }
    fun resume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            isPaused = false
            handler.postDelayed({
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audioManager.abandonAudioFocus(null)
                textToSpeech?.playSilentUtterance(1, TextToSpeech.QUEUE_ADD, null)
            }, 100) // Adjust the delay (in milliseconds) as needed
        }
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
}

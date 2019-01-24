package com.plumbaria.e_6_7_videoplayer

import android.app.Activity
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log

class MainActivity : Activity(),
    MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener,
    SurfaceHolder.Callback,
    View.OnClickListener {

    private var mediaPlayer:MediaPlayer? = null
    private lateinit var surfaceView:SurfaceView
    private lateinit var surfaceHolder:SurfaceHolder
    private lateinit var editText:EditText
    private lateinit var bPlay:ImageButton
    private lateinit var bPause:ImageButton
    private lateinit var bStop:ImageButton
    private lateinit var bLog:ImageButton
    private lateinit var logTextView:TextView
    private var pause:Boolean = false
    private var stop:Boolean = false
    lateinit var path:String
    private var savePos:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        surfaceView = findViewById(R.id.surfaceView)
        surfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(this)
        // obsoleto, pero necesario para versones de Android
        // anteriores a la 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        editText = findViewById(R.id.path)
        editText.setText("http://campus.somtic.net/android/video1617.mp4")
        logTextView = findViewById(R.id.Log)
        bPlay = findViewById(R.id.play)
        bPlay.setOnClickListener(this)
        bPause = findViewById(R.id.pause)
        bPause.setOnClickListener(this)
        bStop = findViewById(R.id.stop)
        bStop.setOnClickListener(this)
        bLog = findViewById(R.id.logButton)
        bLog.setOnClickListener(this)
        log("")
    }

    private fun log(s:String){
        logTextView.append(s + "\n")
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.play -> {
                if (mediaPlayer!=null){
                    mediaPlayer?.start()
                }else{
                    playVideo()
                }
            }

            R.id.pause -> {
                if (mediaPlayer != null){
                    pause = true
                    mediaPlayer?.pause()
                }
            }

            R.id.stop -> {
                if (mediaPlayer!= null){
                    pause = false
                    mediaPlayer?.stop()
                    savePos = 0
                    stop = true
                }
            }

            R.id.logButton -> {
                if (logTextView.visibility == TextView.VISIBLE){
                    logTextView.visibility = TextView.VISIBLE
                }else{
                    logTextView.visibility = TextView.VISIBLE
                }
            }
        }
    }

    private fun playVideo() {
        try {
            pause = false
            path = editText.text.toString()
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            }

            if (stop) {
                mediaPlayer?.reset()
            }

            mediaPlayer?.setDataSource(path)
            mediaPlayer?.setDisplay(surfaceHolder)
            //            mediaPlayer?.prepare()
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnBufferingUpdateListener(this)
            mediaPlayer?.setOnCompletionListener(this)
            mediaPlayer?.setOnPreparedListener(this)
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer?.seekTo(savePos)
            stop = false
        }catch (e:Exception){
            log("ERROR: " + e.message)
        }
    }


    override fun surfaceCreated(holder: SurfaceHolder?) {
        log("llamda a surfaceCreated")
        playVideo()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        log("llamada a surfaceDestroyed")
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        log("llamada a surfaceChanged")
    }

    override fun onPrepared(mp: MediaPlayer?) {
        log("llamada a onPrepared")
        var mVideoWidth:Int = mediaPlayer!!.videoWidth
        var mVideoHeight:Int = mediaPlayer!!.videoHeight
        if (mVideoWidth != 0 && mVideoHeight !=0){
            surfaceHolder.setFixedSize(mVideoWidth,mVideoHeight)
            mediaPlayer?.start()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        log("llamada a onCompletion")
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        log("onBufferingUpdate porcentaje:" + percent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer!=null){
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

}

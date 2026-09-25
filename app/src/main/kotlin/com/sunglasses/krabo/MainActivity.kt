package com.sunglasses.krabo

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.sqrt

class MainActivity : Activity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private var soundPool: SoundPool? = null
    private var soundRole1 = 0
    private var soundRole2 = 0
    private var sound1Loaded = false
    private var sound2Loaded = false

    private var currentRole = 0

    private var lastActionTime = 0L
    private val ANR_LOCK_TIME = 400L

    private lateinit var tvRoleName: TextView
    private lateinit var ivRoleIcon: ImageView
    private lateinit var btnLeft: TextView
    private lateinit var btnRight: TextView
    private lateinit var vibrator: Vibrator
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootView = findViewById(R.id.root_view)
        tvRoleName = findViewById(R.id.tv_role_name)
        ivRoleIcon = findViewById(R.id.iv_role_icon) // 初始化贴图控件
        btnLeft = findViewById(R.id.btn_left)
        btnRight = findViewById(R.id.btn_right)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder().setMaxStreams(2).setAudioAttributes(attrs).build()

        soundPool!!.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                if (sampleId == soundRole1) sound1Loaded = true
                if (sampleId == soundRole2) sound2Loaded = true
            }
        }
        soundRole1 = soundPool!!.load(this, R.raw.role1, 1)
        soundRole2 = soundPool!!.load(this, R.raw.role2, 1)

        btnLeft.setOnClickListener { switchRole() }
        btnRight.setOnClickListener { switchRole() }

        rootView.setOnClickListener {
            playShootSound()
        }
    }

    private fun switchRole() {
        currentRole = if (currentRole == 0) 1 else 0
        
        if (currentRole == 0) {
            tvRoleName.text = "role1"
            ivRoleIcon.setImageResource(R.drawable.role1) // 换成角色1的贴图
        } else {
            tvRoleName.text = "role2"
            ivRoleIcon.setImageResource(R.drawable.role2) // 换成角色2的贴图
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { accel ->
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        soundPool?.release()
        soundPool = null
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val now = SystemClock.elapsedRealtime()
        if (now - lastActionTime < ANR_LOCK_TIME) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val gForce = sqrt((x * x + y * y + z * z).toDouble())

        if (gForce > 25) {
            lastActionTime = now
            playShootSound()
        }
    }

    private fun playShootSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(80)
        }

        if (currentRole == 0 && sound1Loaded) {
            soundPool?.play(soundRole1, 1f, 1f, 1, 0, 1f)
        } else if (currentRole == 1 && sound2Loaded) {
            soundPool?.play(soundRole2, 1f, 1f, 1, 0, 1f)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

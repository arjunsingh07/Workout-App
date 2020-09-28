package com.example.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_confirm_back_confirmation.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class exerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseList:ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        tts = TextToSpeech(this,this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(tb_exercise_activity)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        tb_exercise_activity.setNavigationOnClickListener {

            customDialogForBackButton()
        }

        setupRestView()

        exerciseList = Constants.defaultExerciseList()
        setExerciseStatusRecyclerView()

    }

    override fun onDestroy() {
        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress=0
        }
        if(exerciseTimer!=null){
            exerciseTimer!!.cancel()
            exerciseProgress=0
        }
        if(tts!= null){
            tts!!.stop()
            tts!!.shutdown()
        }
        if(player!=null){
            player!!.stop()
        }

        super.onDestroy()
    }



    private fun setRestProgressBar(){
        progressBar.progress = restProgress
        restTimer = object : CountDownTimer(1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 1-restProgress
                tvTimer.text = (1-restProgress).toString()
                tv_exercise_name_rv.text = exerciseList!![currentExercisePosition].getName()
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onFinish() {




                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start()
    }
    private fun setupRestView(){
        try {
            player = MediaPlayer.create(applicationContext,R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        }catch (e: Exception){
            e.printStackTrace()
        }
        currentExercisePosition++

        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility = View.GONE
        if (restTimer!=null){
            restTimer!!.cancel()
            restProgress=0
        }



        setRestProgressBar()
    }
    private fun setExerciseProgressBar(){
        Exercise_progressBar.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(3000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                Exercise_progressBar.progress = 3-exerciseProgress
                tvExerciseTimer.text = (3-exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                    finish()
                    val intent = Intent(this@exerciseActivity, finalActivity::class.java)
                    startActivity(intent)

                }
            }
        }.start()
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupExerciseView(){
        llExerciseView.visibility = View.VISIBLE
        llRestView.visibility = View.GONE

        if (exerciseTimer!=null){
            exerciseTimer!!.cancel()
            exerciseProgress=0
        }
        setExerciseProgressBar()
        iv_image.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
        var name = exerciseList!![currentExercisePosition].getName()
        onSpeak(name)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_NOT_SUPPORTED || result ==TextToSpeech.LANG_MISSING_DATA) {
                Log.i("TTS", "Language Not Supported")
            }
        }else{
            Log.i("TTS", "Initialization Failed")
        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onSpeak(text:String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }
    private fun setExerciseStatusRecyclerView(){
        rvExerciseStatus.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false )
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)
        rvExerciseStatus.adapter = exerciseAdapter
    }
    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_confirm_back_confirmation)
        customDialog.confirmDialog_yes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.confirmDialog_no.setOnClickListener {

            customDialog.dismiss()
        }
        customDialog.show()
    }
}
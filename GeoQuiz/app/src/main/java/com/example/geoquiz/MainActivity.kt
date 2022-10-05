package com.example.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.app.DownloadManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_HINTS = "hints"
private const val REQUEST_CODE_CHEAT = 0
class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)


        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        trueButton.setOnClickListener{view: View->
            checkAnswer(true)
        }
        falseButton.setOnClickListener{view: View->
            checkAnswer(false)
        }
        nextButton.setOnClickListener {

            quizViewModel.moveToNext()
       updateQuestion()
        }
        prevButton.setOnClickListener {
          quizViewModel.moveToPrev()
            updateQuestion()
        }
        cheatButton.setOnClickListener{

           val answerIsTrue = quizViewModel.currentQuestionAnswer
           val intent = CheatActivity.newIntent(
               this@MainActivity, answerIsTrue)
            val options = ActivityOptions
                .makeClipRevealAnimation(it,0,0,it.width,it.height)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

  updateQuestion()
    }

    private fun updateQuestion(){
        falseButton.isClickable = true
        trueButton.isClickable = true
        quizViewModel.isCheater = false
        onClick = false
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {

                quizViewModel.isCheater =
                    data?.getBooleanExtra(EXRTA_ANSWER_IS_TRUE, false) ?: false
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
        if (userAnswer) falseButton.isClickable = false
        if (!userAnswer) trueButton.isClickable = false
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
       outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        outState.putInt(KEY_HINTS,quizViewModel.hints)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val currentIndex = savedInstanceState.getInt(KEY_INDEX,0)
        val hints = savedInstanceState.getInt(KEY_HINTS,3)
        quizViewModel.currentIndex = currentIndex
        quizViewModel.hints = hints
    }

    override fun onResume() {
        super.onResume()
        falseButton.isClickable = true
        trueButton.isClickable = true
        if(quizViewModel.hints != 0){
            quizViewModel.decHints()
            Log.d(TAG,"${quizViewModel.hints}")
        } else {cheatButton.isClickable = false}
    }
}
package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

 const val EXRTA_ANSWER_IS_TRUE =
    "com.example.geoquiz.answer_is_true"
private const val KEY_ANSWER = "answer"
private const val KEY_CLICK = "click"
//private const val TAG ="CheatActivity"
var answerIsTrue = false
var onClick = false
class CheatActivity : AppCompatActivity() {
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(
            EXRTA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)


        if (onClick) onCreate() else answerTextView.setText("")

        showAnswerButton.setOnClickListener{
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            onClick = true
            setAnswerShownResult(onClick)
        }
    }

    private fun onCreate(){
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answerTextView.setText(answerText)
        setAnswerShownResult(onClick)
    }
private fun setAnswerShownResult(isAnswerShown: Boolean){
    val data = Intent().apply {
        putExtra(EXRTA_ANSWER_IS_TRUE,isAnswerShown)
    }
    setResult(Activity.RESULT_OK, data)
}
    companion object {
        fun newIntent(packageContext: Context,answerIsTrue:Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXRTA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
      outState.putBoolean(KEY_ANSWER,answerIsTrue)
        outState.putBoolean(KEY_CLICK, onClick)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val answer = savedInstanceState.getBoolean(KEY_ANSWER,false)
        val click = savedInstanceState.getBoolean(KEY_CLICK,false)
        answerIsTrue = answer
        onClick = click

    }
}
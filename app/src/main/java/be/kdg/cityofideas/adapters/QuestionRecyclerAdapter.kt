package be.kdg.cityofideas.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import be.kdg.cityofideas.R
import be.kdg.cityofideas.login.loggedInUser
import be.kdg.cityofideas.model.surveys.Answer
import be.kdg.cityofideas.model.surveys.Question
import be.kdg.cityofideas.model.surveys.QuestionType.*

class QuestionRecyclerAdapter(val context: Context?) :
    RecyclerView.Adapter<QuestionRecyclerAdapter.SurveyViewHolder>() {

    var questions: Array<Question> = arrayOf()
        set(question) {
            field = question
            notifyDataSetChanged()
        }

    class SurveyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val question: TextView = view.findViewById(R.id.QuestionText)
        val questionNr: TextView = view.findViewById(R.id.QuestionNr)
        val layout: LinearLayout = view.findViewById(R.id.LinearLayoutQuestion)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SurveyViewHolder {
        val surveyViewHolder = LayoutInflater.from(p0.context).inflate(R.layout.question_list, p0, false)
        return SurveyViewHolder(surveyViewHolder)
    }

    override fun getItemCount() = questions.size

    override fun onBindViewHolder(p0: SurveyViewHolder, p1: Int) {
        p0.questionNr.text = getQuestionNr(questions[p1])
        p0.question.text = questions[p1].QuestionText
        getQuestionAnswers(questions[p1], context, p0.layout)
    }

    private fun getQuestionNr(question: Question): String {
        return question.QuestionNr.toString() + "/" + itemCount
    }

    private fun getQuestionAnswers(question: Question, context: Context?, layout: LinearLayout) {
        when (question.QuestionType) {
            OPEN -> getOpenAnswer(question, context, layout)
            RADIO -> getRadioAnswers(question, context, layout)
            CHECK -> getCheckAnswers(question, context, layout)
            DROP -> getDropAnswers(question, context, layout)
            EMAIL -> giveEmail(question, context, layout)
            null -> Log.d("question", question.QuestionText)
        }
    }

    private fun getOpenAnswer(question: Question, context: Context?, layout: LinearLayout) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val editText = EditText(context)
        editText.id = question.QuestionId
        editText.hint = context!!.getString(R.string.edittext)
        editText.layoutParams = params
        layout.addView(editText)
    }

    private fun getRadioAnswers(question: Question, context: Context?, layout: LinearLayout) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val radioGroup = RadioGroup(context)
        radioGroup.layoutParams = params
        question.Answers!!.forEach {
            val radioButton = RadioButton(context)
            radioButton.id = it.AnswerId
            radioButton.text = it.AnswerText
            radioButton.layoutParams = params
            radioGroup.addView(radioButton)
        }
        layout.addView(radioGroup)
    }

    private fun getCheckAnswers(question: Question, context: Context?, layout: LinearLayout) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        question.Answers!!.forEach {
            val checkBox = CheckBox(context)
            checkBox.id = it.AnswerId
            checkBox.text = it.AnswerText
            checkBox.layoutParams = params
            layout.addView(checkBox)
        }
    }

    private fun getDropAnswers(question: Question, context: Context?, layout: LinearLayout) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val spinner = Spinner(context)

        val answers: Array<Answer> = question.Answers!!.toTypedArray()
        val answerText: ArrayList<String> = arrayListOf()
        answers.forEach {
            answerText.add(it.AnswerText!!)
        }

        val arrayAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, answerText)
        spinner.id = question.QuestionId
        spinner.adapter = arrayAdapter
        spinner.layoutParams = params
        layout.addView(spinner)
    }

    private fun giveEmail(question: Question, context: Context?, layout: LinearLayout) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val email = EditText(context)
        email.id = question.QuestionId
        email.hint = context!!.getString(R.string.Email)
        email.inputType = TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        email.layoutParams = params

        if (loggedInUser != null)
            email.setText(loggedInUser?.Email)

        layout.addView(email)
    }
}
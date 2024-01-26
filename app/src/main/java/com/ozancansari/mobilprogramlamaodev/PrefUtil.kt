package com.ozancansari.mobilprogramlamaodev

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.AnimatedContentScope
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PrefUtil {

    companion object {
        private const val ID = "PrefOzan"

        fun getExams(activity: Activity) : MutableList<Exam> {
            val sharedPref : SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
            return Json.decodeFromString(sharedPref.getString(ID, "[]")!!)
        }

        fun putExam(activity: Activity, exam: Exam) {
            val sharedPref : SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                val exams = getExams(activity)
                exams.add(exam)
                putString(ID, Json.encodeToString(exams))
                apply()
            }
        }

        fun deleteAllExams(activity: Activity) {
            val sharedPref : SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                remove(ID)
                apply()
            }
        }

    }

}
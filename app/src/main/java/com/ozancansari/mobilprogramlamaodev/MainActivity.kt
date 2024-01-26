package com.ozancansari.mobilprogramlamaodev

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ozancansari.mobilprogramlamaodev.ui.theme.MobilProgramlamaOdevTheme
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {

    companion object {
        var instance: MainActivity? = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        //PrefUtil.deleteAllExams(this)
        /*PrefUtil.putExam(this, Exam(1,"A", LocalDateTime.now()))
        PrefUtil.putExam(this, Exam(2,"B", LocalDateTime.now()))
        

        for (item in PrefUtil.getExams(this)) {
            Log.d("OZAN",item.toString())
        }*/
        startStopwatch()
        setContent {
            MobilProgramlamaOdevTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "examview") {
                        composable("examview") { ExamsCompose(PrefUtil.getExams(LocalContext.current as Activity), navController) }
                        composable("addexam") { ExamView(LocalContext.current as Activity, navController) }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        moveToBackground()
    }

    override fun onDestroy() {
        super.onDestroy()
        moveToForeground()
    }

    private fun moveToForeground() {
        val stopwatchService = Intent(this, CounterService::class.java)
        stopwatchService.putExtra(
            CounterService.STOPWATCH_ACTION,
            CounterService.MOVE_TO_FOREGROUND
        )
        startService(stopwatchService)
    }

    private fun moveToBackground() {
        val stopwatchService = Intent(this, CounterService::class.java)
        stopwatchService.putExtra(
            CounterService.STOPWATCH_ACTION,
            CounterService.MOVE_TO_BACKGROUND
        )
        startService(stopwatchService)
    }

    private fun startStopwatch() {
        val stopwatchService = Intent(this, CounterService::class.java)
        stopwatchService.putExtra(CounterService.STOPWATCH_ACTION, CounterService.START)
        startService(stopwatchService)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExamsCompose(exams : List<Exam>, navController: NavController) {
    val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
    Column(modifier = Modifier.padding(16.dp)) {
        Row {
            Text(text = "Sınav İsmi", modifier = Modifier.weight(1f))
            Text(text = "Sınav Tarihi", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(20.dp))
        for (exam in exams) {
            Row {
                Text(text = exam.name, modifier = Modifier.weight(1f))
                Text(text = formatter.format(exam.date), modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        SmallFloatingActionButton(
            modifier = Modifier
                .align(Alignment.End),
            onClick = { navController.navigate("addexam") },
            shape = CircleShape,
            ) {
            Icon(Icons.Filled.Add, "Sınav Ekle")
        }
    }
}


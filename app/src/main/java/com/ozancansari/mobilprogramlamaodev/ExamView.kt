package com.ozancansari.mobilprogramlamaodev

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExamView(activity: Activity, navController: NavController) {
    var examName by remember { mutableStateOf("") }
    var examDate by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.Center) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Text(text = "Sınav Adı: ", Modifier.weight(0.2f))
            TextField(modifier = Modifier.weight(0.8f), value = examName, onValueChange = {examName = it} )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Text(text = "Tarih: ", Modifier.weight(0.2f))
            TextField(modifier = Modifier.weight(0.8f), placeholder = { Text("Gün/Ay/Yıl Saat:Dakika") },value = examDate, onValueChange = {examDate = it} )
        }
        /**FORMATTER İLE GİRİLEN ZAMANI KAYDETME*/

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
            val date = LocalDateTime.parse(examDate, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.forLanguageTag("tr-TR")))
            val randomNumber = (0..100_000_000).random()
            PrefUtil.putExam(activity, Exam(randomNumber, examName, date))
            navController.navigate("examview")
        }) {
            Text("Kaydet")
        }
    }

}

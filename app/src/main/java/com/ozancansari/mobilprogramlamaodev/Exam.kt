package com.ozancansari.mobilprogramlamaodev

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Exam(val id : Int, val name : String, @Serializable(with = LocalDateTimeSerializer::class) val date : LocalDateTime)

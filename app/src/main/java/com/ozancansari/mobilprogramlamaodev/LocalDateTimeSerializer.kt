package com.ozancansari.mobilprogramlamaodev

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**UYGULAMANIN ZAMANININ FORMATINI ALMA*/
@Serializer(forClass = LocalDateTime::class)
class LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    @RequiresApi(Build.VERSION_CODES.O)
    /**önceden belirlenmiş bir tarih ve saat biçimini belirten bir DateTimeFormatter nesnesini oluşturur.**/
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}

/** Bu kod parçası, LocalDateTime türündeki verilerin serileştirilmesi ve deserializasyonu için kullanılan özel bir KSerializer sınıfını tanımlar.
Bu, genellikle verilerin depolanması veya ağ üzerinden iletilmesi gibi durumlar için kullanılır.
Bu özel serileştirici, tarih ve saat bilgilerini belirli bir formatta (ISO 8601) saklamak ve iletmek için kullanılan standart bir araçtır.*/
package cloud.siebert.ferryapp

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

class FerryNotificationService(private val context: Context) {
    var returnFerryTime: String = "0"
    var returnFerryDate: String = "0"
    var returnFerryWeekday: String = "0"
    //var returnFerryTime: LocalDateTime? = null
    var returnFerryValue = false

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun createNotification(messagePrediction: String, messageDate: String, messageTime: String, messageWeekday: String) {
        val activityIntent = Intent(context, MainActivity::class.java)
        var notification: Notification? = null
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        if (messagePrediction == "true") {
            notification = NotificationCompat.Builder(context, FERRY_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_directions_boat_24)
                .setContentTitle("FÄHRT SIE? JA!")
                .setContentText("Die nächste Fähre am $messageWeekday den $messageDate um $messageTime fährt!")
                .setContentIntent(activityPendingIntent)
                .build()
        }
        if (messagePrediction == "false"){
            notification = NotificationCompat.Builder(context, FERRY_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_directions_boat_24)
                .setContentTitle("FÄHRT SIE? NEIN!")
                .setContentText("Die nächste Fähre am $messageWeekday den $messageDate um $messageTime fährt nicht!")
                .setContentIntent(activityPendingIntent)
                .build()
        }
        notificationManager.notify(1, notification)
    }

    suspend fun getFerryData() = withContext(Dispatchers.IO){
        //val url = "https://catfact.ninja/fact"
        val url = "https://api.alpaka.fyi/data/isComing"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            @RequiresApi(Build.VERSION_CODES.S)
            override fun onResponse(call: Call, response: Response){
                val body = response.body()?.string()
                println(body)
                val gson = GsonBuilder().create()
                val apiReturn = gson.fromJson(body, FerryResponse::class.java)
                //val temp1 = gson.fromJson(body, CatResponse::class.java)
                //returnValue = temp1.fact
                //println("2: $returnValue")
                val instantValue = Instant.ofEpochSecond(apiReturn.departureTimestamp.toLong())
                //Format the time
                val instantTimeHour = LocalTime.ofInstant(instantValue, ZoneId.of("Europe/Berlin")).hour
                val instantTimeMinute = LocalTime.ofInstant(instantValue, ZoneId.of("Europe/Berlin")).minute.toString().padStart(2, '0')
                //Format the date
                val timeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN).withZone(ZoneId.systemDefault())
                val timeFormatterWeekday = ZonedDateTime.ofInstant(Instant.ofEpochSecond(apiReturn.departureTimestamp.toLong()), ZoneId.of("Europe/Berlin")).dayOfWeek
                returnFerryDate = timeFormatter.format(instantValue)
                returnFerryTime = "$instantTimeHour:$instantTimeMinute"
                returnFerryValue = apiReturn.prediction
                returnFerryWeekday = timeFormatterWeekday.toString()
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed")
            }
        })
    }

    suspend fun showNotification() {
        getFerryData()
        delay(4000)
        //TODO: UGLY, PLEASE FIX ASAP!!!!
        //createNotification(returnValue)
        createNotification(returnFerryValue.toString(), returnFerryDate, returnFerryTime, returnFerryWeekday)
    }
    
    companion object {
        const val FERRY_CHANNEL_ID = "FerryChannel"
    }
    class CatResponse(val fact: String, val length: Int)

    class FerryResponse(val prediction: Boolean, val plannendDeparture: Int, val departureTimestamp: Int)

    class FerryUI(val ferryPrediction: Boolean, val ferryWeekday: String, val ferryDay: String, val ferryTime: String) {
    }
}
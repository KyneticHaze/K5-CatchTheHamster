package com.furkanharmanci.catchthehamster

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.furkanharmanci.catchthehamster.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var runnable : Runnable = Runnable {}
    private var handler : Handler = Handler(Looper.getMainLooper())
    private var imageArray = ArrayList<ImageView>()
    private var number : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /// listeye bağlanılmış görüntü ekleme
        imageArray.add(binding.imgOne)
        imageArray.add(binding.imgTwo)
        imageArray.add(binding.imgThree)
        imageArray.add(binding.imgFour)
        imageArray.add(binding.imgFive)
        imageArray.add(binding.imgSix)
        imageArray.add(binding.imgSeven)
        imageArray.add(binding.imgEight)
        imageArray.add(binding.imgNine)
        imageArray.add(binding.imgTen)
        imageArray.add(binding.imgEleven)
        imageArray.add(binding.imgTwelve)

        // başlatma ve sonlandırma butonlarının tanımlanması
        val startButton = binding.start
        val quitButton = binding.quit

        imageInvisible()

        /// buton tıklaması ile oluşan olayların dinlenilmesi
        startButton.setOnClickListener {
            eventStart()
        }
        quitButton.setOnClickListener {
            eventQuit()
        }
    }

    /// görüntüye tıklama ile ekranda skor arttırılma methodu
    fun clickHamster(view : View) {
        number++
        val score = "Score: $number"
        binding.score.text = score
    }

    fun imageInvisible() {
        /// uygulama başlarken tüm görüntülerin görünmez olmasını sağlayan döngü
        for (image in imageArray) {
            image.visibility = View.INVISIBLE
        }
    }
    /// bir sürdürücü(runnable) ile görünmez durumdaki görüntülerin rastgele görünür olması
    private fun hideImages() {
        runnable =  Runnable {
            imageInvisible()
            val random = Random()
            val randomIdx = random.nextInt(12)
            imageArray[randomIdx].visibility = View.VISIBLE
            handler.postDelayed(runnable, 500)
        }

        handler.post(runnable)
    }
    /// oyun başlatma buton methodu
    private fun eventStart() {
        val alertEventStart = AlertDialog.Builder(this@MainActivity)
        alertEventStart.setTitle("Start?")
        alertEventStart.setMessage("Do you want to start the game?")
        alertEventStart.setPositiveButton("Yes") {
            p0, p1 ->
            hideImages()
            /// Belirli süre geriye sayan sayaç
            object : CountDownTimer(25000, 1000) {
                override fun onTick(milliSecondFuture: Long) {
                    val time = "Time: ${milliSecondFuture / 1000}"
                    binding.time.text = time
                }
                override fun onFinish() {
                    val timeOut = "Time Out!"
                    binding.time.text = timeOut
                    for (image in imageArray) {
                        image.visibility = View.INVISIBLE
                    }
                    handler.removeCallbacks(runnable)


                    val alertGameEnd = AlertDialog.Builder(this@MainActivity)
                    alertGameEnd.setTitle("Time Out")
                    alertGameEnd.setMessage("Your Score: $number")
                    alertGameEnd.setPositiveButton("Yes") {
                            p0, p1 ->
                        val intent = intent
                        finish()
                        startActivity(intent)
                    }
                    alertGameEnd.setNegativeButton("No") {
                            p0, p1 ->
                        Toast.makeText(this@MainActivity, "Game Over!", Toast.LENGTH_LONG).show()
                    }
                    alertGameEnd.show()
                }
            }.start()
        }
        alertEventStart.setNegativeButton("No") {
            p0,p1 ->
            Toast.makeText(this@MainActivity, "Game is not started!", Toast.LENGTH_LONG).show()
        }
        alertEventStart.show()
    }
    /// sonlandırma buton methodu
    private fun eventQuit() {
        finish()
    }
}
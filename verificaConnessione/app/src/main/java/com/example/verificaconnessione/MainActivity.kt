package com.example.verificaconnessione

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.net.Uri
import android.media.MediaPlayer
import android.widget.Toast
import android.media.AudioManager
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient




class MainActivity : AppCompatActivity() {

    private lateinit var connectionStatusTextView: TextView
    private lateinit var networkChangeReceiver: NetworkChangeReceiver


    private lateinit var dateTextView: TextView

    private lateinit var image1: ImageView
    private lateinit var image2: ImageView
    private lateinit var image3: ImageView
    private lateinit var image4: ImageView
    private lateinit var commonWebView: WebView
    private val radio1 = ""
    private val radio2 = "https://tunein.com/embed/player/s25596/"
    private val rtl = "https://tunein.com/embed/player/s6684/"
    private val rds = "https://tunein.com/embed/player/s16202/"


    private lateinit var horizontalScrollView: HorizontalScrollView
    private var initialX = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectionStatusTextView = findViewById<TextView>(R.id.connectionStatusTextView)
        networkChangeReceiver = NetworkChangeReceiver()

        dateTextView = findViewById<TextView>(R.id.dateTextView)

        displayCurrentDate()

        image1 = findViewById(R.id.image1)
        image2 = findViewById(R.id.image2)
        image3 = findViewById(R.id.image3)
        image4 = findViewById(R.id.image4)
        commonWebView = findViewById(R.id.WebView)

        setupWebView(commonWebView) // Imposta le impostazioni della WebView

        image1.setOnClickListener {
            toggleWebViewVisibility(radio1)
        }

        image2.setOnClickListener {
            toggleWebViewVisibility(radio2)
        }

        image3.setOnClickListener {
            toggleWebViewVisibility(rtl)
        }

        image4.setOnClickListener {
            toggleWebViewVisibility(rds)
        }

        horizontalScrollView = findViewById(R.id.horizontalScrollView)

        horizontalScrollView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = event.x
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val distanceX = initialX - event.x
                    horizontalScrollView.scrollBy(distanceX.toInt(), 0)
                    initialX = event.x
                    true
                }
                else -> false
            }
        }


        }

    private fun setupWebView(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
    }

    private fun toggleWebViewVisibility(url: String) {
        if (commonWebView.visibility == View.GONE) {
            commonWebView.loadUrl(url)
            commonWebView.visibility = View.VISIBLE
        } else {
            // Se la WebView è visibile e si clicca su un'altra immagine,
            // carica il nuovo URL e mantienila visibile.
            commonWebView.loadUrl(url)
            // Se volessi nasconderla al secondo clic su QUALSIASI immagine,
            // potresti usare: commonWebView.visibility = View.GONE
        }
    }

    private fun checkInternetConnection() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivityManager != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    connectionStatusTextView.text = "Online"
                    connectionStatusTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.online_green))
                    connectionStatusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black)) //testo bianco per contrasto
                    return
                }
            }
        }
        connectionStatusTextView.text = "Offline"
        connectionStatusTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.offline_red))
        connectionStatusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black))//testo nero per contrasto
    }

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkInternetConnection()
        }
    }



    private fun displayCurrentDate() {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) // Formato data
        val formattedDate = dateFormat.format(currentDate)

        dateTextView.text = formattedDate
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, filter)
        checkInternetConnection() // Controllo iniziale
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkChangeReceiver)
    }




    } // end on create






















/*
    private fun playLiveAudio(audioUrl: String) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer().apply {
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    setDataSource(this@MainActivity, Uri.parse(audioUrl))
                    prepareAsync() // Usa prepareAsync per lo streaming
                    setOnPreparedListener {
                        start()

                        Toast.makeText(this@MainActivity, "Radio in diretta...", Toast.LENGTH_SHORT).show()
                    }
                    setOnErrorListener { mp, what, extra ->

                        Toast.makeText(this@MainActivity, "Errore durante la riproduzione.", Toast.LENGTH_SHORT).show()
                        mp.reset()
                        false
                    }
                    setOnCompletionListener {

                        stopPlayback()
                    }
                }
            } else {
                if (isPlaying) {
                    stopPlayback()
                    Toast.makeText(this@MainActivity, "Radio interrotta.", Toast.LENGTH_SHORT).show()
                } else {
                    startPlayback()
                    Toast.makeText(this@MainActivity, "Riproduzione ripresa...", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            isPlaying = false
            Toast.makeText(this@MainActivity, "Errore: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun startPlayback() {
        mediaPlayer?.start()
        isPlaying = true
    }

    private fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }
*/

/*
    override fun onDestroy() {
        super.onDestroy()
        stopPlayback() // Rilascia le risorse quando l'attività viene distrutta
    }

    private fun openAudioLink(audioUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(audioUrl))
        startActivity(intent)
    }



    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, filter)
        checkInternetConnection() // Controllo iniziale
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkChangeReceiver)

    }
*/





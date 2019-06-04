package c.example.loginui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class ControlActivity : AppCompatActivity() {
    //template
    //http://puu.sh/D3jwN.mkv
    private var streamURI = "http://192.168.1.68:8160" // your URL here
    private var playerView: PlayerView? = null
    private var player: SimpleExoPlayer? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        var log:String? = null
        playerView = findViewById(R.id.player_view)
        val textView = findViewById<TextView>(R.id.log_text_view)

        // Get a non-default Storage bucket
        val storage = FirebaseStorage.getInstance()

        // Create a reference to a file from a Google Cloud Storage URI
        val gsReference = storage
                .getReferenceFromUrl("gs://void-remote.appspot.com")
        val accessPointLogReference = gsReference.child("ap_log.txt")

        val localFile = File.createTempFile("ap_log", "txt")

        accessPointLogReference.getFile(localFile).addOnSuccessListener {
            // Local temp file has been created
            Log.d("FileLog", "Successfully retrieve file.\n")
            log = localFile.readText()
            textView.text = log
        }.addOnFailureListener {
            // Handle any errors
            Log.d("FileLog", "Failed to retrieve file.\n")

        }
    }

    override fun onStart() {
        super.onStart()

        player = ExoPlayerFactory.newSimpleInstance(this,
                DefaultTrackSelector())
        playerView!!.player = player

        val dataSourceFactory: DefaultDataSourceFactory =
                DefaultDataSourceFactory(this,
                        Util.getUserAgent(this,
                                "exo-demo"))

        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(streamURI))
        player!!.prepare(mediaSource)
    }

    override fun onStop() {
        super.onStop()

        playerView!!.player = null
        player!!.release()
        player = null
    }

    override fun onBackPressed() {
        Log.d("Control Activity", "onBackPressed called")
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
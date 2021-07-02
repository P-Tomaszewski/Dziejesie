package pl.edu.pjatk.dziejesie.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.edu.pjatk.dziejesie.R
import pl.edu.pjatk.dziejesie.databinding.ActivityDetailEventBinding
import pl.edu.pjatk.dziejesie.model.Event
import java.util.*
import kotlin.concurrent.thread

class EventDetailsActivity: AppCompatActivity() {
    val db = Firebase.firestore

    private val view by lazy {
        ActivityDetailEventBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        val id: String = (intent.extras?.get("id") ?: "0") as String
//        if(!id.equals("0")) {
            templateWithData(id)}
//    }
    private fun templateWithData(id: String) = thread {
      var doc = db.collection("events").document(id)
    doc.get()
            .addOnSuccessListener { document ->
                    findViewById<TextView>(R.id.detailName).setText(document.data!!.get("name").toString())
                    findViewById<TextView>(R.id.detailPlace).setText(document.data!!.get("loc").toString())
                    findViewById<TextView>(R.id.detailNote).setText(document.data!!.get("note").toString())
            }
            .addOnFailureListener { exception ->
//                Log.d(TAG, "get failed with ", exception)
            }

//                .get()
//                .addOnSuccessListener { result ->
//                    for (document in result) {
//                        findViewById<TextView>(R.id.detailName).setText(document.data.getValue("name").toString())
//                        findViewById<TextView>(R.id.detailPlace).setText(document.data.getValue("loc").toString())
//                        findViewById<TextView>(R.id.detailNote).setText(document.data.getValue("note").toString())
//                    }
//                    }
//                .addOnFailureListener { e ->
//                    Log.w("FirebaseInfo", "Error reading document", e)
//                }
    }
}
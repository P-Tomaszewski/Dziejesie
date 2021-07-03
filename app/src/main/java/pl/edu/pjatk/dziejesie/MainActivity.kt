package pl.edu.pjatk.dziejesie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.edu.pjatk.dziejesie.activity.AddEventActivity
import pl.edu.pjatk.dziejesie.activity.EventDetailsActivity
import pl.edu.pjatk.dziejesie.activity.MapActivity
import pl.edu.pjatk.dziejesie.adapter.EventAdapter
import pl.edu.pjatk.dziejesie.databinding.ActivityMainBinding

private const val REQUEST_ADD_PAYMENT = 2

class MainActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val eventAdapter by lazy {
        EventAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAddButton()
        setupAddMap()
        setupPaymentList()
    }
    private fun setupAddButton() = binding.buttonAdd.setOnClickListener {
        val intent = Intent(this, AddEventActivity::class.java)
        startActivityForResult(
            intent, REQUEST_ADD_PAYMENT
        )
    }

    private fun setupAddMap() = binding.buttonMap.setOnClickListener {
        val intent = Intent(this, MapActivity::class.java)
        startActivityForResult(
            intent, REQUEST_ADD_PAYMENT
        )
    }

    fun setupDetails(item: String) {
        var intent = Intent(this, EventDetailsActivity::class.java)
        startActivityForResult(intent.putExtra("id", item), REQUEST_ADD_PAYMENT)
    }

    private fun setupPaymentList() {
        db = Firebase.firestore
        binding.recycler.apply {
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(context)
        }
        eventAdapter.load()
    }

}
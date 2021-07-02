package pl.edu.pjatk.dziejesie.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.edu.pjatk.dziejesie.R
import pl.edu.pjatk.dziejesie.databinding.ActivityAddEventBinding
import pl.edu.pjatk.dziejesie.databinding.ActivityAddEventBinding.inflate
import pl.edu.pjatk.dziejesie.databinding.ActivityMainBinding.inflate
import pl.edu.pjatk.dziejesie.databinding.ItemEventCardBinding.inflate
import pl.edu.pjatk.dziejesie.model.Event
import java.time.LocalDate
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.thread


class AddEventActivity : AppCompatActivity() {
    private val pool by lazy {
        Executors.newSingleThreadExecutor()
    }

        val db = Firebase.firestore

    private val view by lazy {
    ActivityAddEventBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)


        val id: Long = (intent.extras?.get("id") ?: -1L) as Long
        if(id != -1L) {
//            setupSave(true, id)
//            templateWithData(id)
        }
        else setupSave(false, 0L)


        view.eventDate.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val picker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                view, year, month, dayOfMonth -> this.view.eventDate.setText("$year-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}")},
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            picker.show()
        }
    }

//    private fun setupShareButton() = view.share.setOnClickListener {
//        val sendIntent: Intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT,
//                    "Miejsce: " + view.paymentPlace.text + " " + "\n" +
//                            "Kategoria: " + view.paymentCategory.text + "\n" +
//                            "Data: " + view.paymentDate.text + "\n" +
//                            "Typ: " + view.spinner.selectedItem.toString() + "\n" +
//                            "Kwota: " + view.paymentAmount.text.toString())
//            type = "text/plain"
//        }
//
//        val shareIntent = Intent.createChooser(sendIntent, null)
//        startActivity(shareIntent) //share
//    }

//    private fun templateWithData(id: Long) = thread {
//        val transaction = db.payments.getPaymentById(id)
//        findViewById<EditText>(R.id.payment_place).setText(transaction.place)
//        findViewById<EditText>(R.id.payment_amount).setText(transaction.amount.toString())
//        findViewById<EditText>(R.id.payment_date).setText(transaction.date)
//        findViewById<EditText>(R.id.payment_category).setText(transaction.category)
//    }

    private fun setupSave(edit: Boolean, id: Long) = view.saveButton.setOnClickListener {

        if(!edit){ //new
            val event = Event(
                    id = "",
                name = view.name.text.toString(),
                loc =  view.loc.text.toString(),
                date = view.eventDate.text.toString(),
                note = view.note.text.toString()
            )

            db.collection("events")
                .add(event)
                .addOnSuccessListener { documentReference ->
                    Log.d("event", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("event", "Error adding document", e)
                }
            setResult(Activity.RESULT_OK)
            finish()
        }
//        else{ //update
//            val paymentDto = PaymentDto(
//                    id,
//                    place = view.paymentPlace.text.toString(),
//                    category = view.paymentCategory.text.toString(),
//                    amount = amountType(view.paymentAmount.text.toString(), view.spinner.selectedItem.toString()),
//                    date = setDate(view.paymentDate.text.toString()),
//                    type = view.spinner.selectedItem.toString()
//            )
//            pool.submit{
//                db.payments.updatePayment(paymentDto)
//                setResult(Activity.RESULT_OK)
//                finish()
//            }
//        }
    }

    private fun setDate(date: String): String{
        if(date.isNullOrEmpty()){
//            return LocalDate.now().toString()
            return date
        }
        else return date
    }


}
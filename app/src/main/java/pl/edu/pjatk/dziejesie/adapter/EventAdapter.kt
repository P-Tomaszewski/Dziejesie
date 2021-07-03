package pl.edu.pjatk.dziejesie.adapter


import android.os.Build
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.LatLng
import pl.edu.pjatk.dziejesie.EventViewHolder
import pl.edu.pjatk.dziejesie.MainActivity
import pl.edu.pjatk.dziejesie.databinding.ItemEventCardBinding
import pl.edu.pjatk.dziejesie.model.Event
import java.time.LocalDate
import java.util.*
import kotlin.Comparator
import kotlin.concurrent.thread

class EventAdapter(val mainActivity: MainActivity): RecyclerView.Adapter<EventViewHolder>() {

    private val main = HandlerCompat.createAsync(Looper.getMainLooper())
     var events: List<Event> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {

        val view = ItemEventCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )


        return EventViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun load() = thread{
      val db: FirebaseFirestore
        db = Firebase.firestore
        var eventsTemp = mutableListOf<Event>()
        db.collection("events")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var eventTemp = Event()
                    eventTemp.id = document.id
                    eventTemp.name = document.data.getValue("name").toString()
                    eventTemp.date = document.data.getValue("date").toString()
                    eventTemp.loc = document.data.getValue("loc").toString()
                    eventTemp.note = document.data.getValue("note").toString()
                    eventsTemp.add(eventTemp)
                }

                events = eventsTemp


                Collections.sort(events, Comparator<Event> { o1, o2 ->
                    if (converterStringToDate(o1.date) == null ||
                        converterStringToDate(o2.date) == null
                    )
                        0 else converterStringToDate(o1.date)
                        .compareTo(converterStringToDate(o2.date))
                })
                events = events.reversed()

                main.post{
                    notifyDataSetChanged()
                }
            }
    }




    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.onBind(events[position])
        holder.itemView.setOnClickListener{
            val item = events[holder.layoutPosition].id
            mainActivity.setupDetails(item)
        }
    }






    override fun getItemCount(): Int{
        if(events.isEmpty()){
            return 0
        } else return events.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun converterStringToDate(date: String): LocalDate =
            LocalDate.of(
                date.substring(0, 4).toInt(),
                date.substring(6, 7).toInt(),
                date.substring(8, 10).toInt()
            )
}
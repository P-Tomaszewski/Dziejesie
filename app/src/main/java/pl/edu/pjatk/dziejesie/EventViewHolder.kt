package pl.edu.pjatk.dziejesie;

import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import pl.edu.pjatk.dziejesie.databinding.ItemEventCardBinding;
import pl.edu.pjatk.dziejesie.model.Event

class EventViewHolder( val binding: ItemEventCardBinding): RecyclerView.ViewHolder(binding.root) {
    fun onBind(event: Event){
        with(binding){
            nazwa.text = event.name
            lokalizacja.text = event.loc
            date.text = event.date
        }
    }
}

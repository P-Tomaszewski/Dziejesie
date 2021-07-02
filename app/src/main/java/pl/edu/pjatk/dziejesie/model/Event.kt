package pl.edu.pjatk.dziejesie.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Event (
        var id: String,
        var name: String,
        var loc: String,
        var date: String,
        var note: String
        ){
        constructor() : this("", "", "", "", "")
}

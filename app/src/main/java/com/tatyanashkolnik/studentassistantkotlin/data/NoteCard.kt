package com.tatyanashkolnik.studentassistantkotlin.data

class NoteCard {

    var title: String = null.toString()
    var subtitle: String = null.toString()
    var time: String = null.toString()
    var photo: String = null.toString()
    var photoAttached: Boolean = false

    constructor(title: String, login: String, password: String) {

        this.title = title
        this.subtitle = subtitle
        this.time = time
        if(photoAttached){
            this.photoAttached = photoAttached
            this.photo = photo
        }
    }

    constructor()


}
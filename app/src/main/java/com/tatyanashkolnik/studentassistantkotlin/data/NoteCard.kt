package com.tatyanashkolnik.studentassistantkotlin.data

class NoteCard {

    var title: String = null.toString()
    var subtitle: String = null.toString()
    var time: String = null.toString()
    var photo: String = null.toString()
    var photoAttached: Int = 0

    constructor(title: String, subtitle: String, time: String, photoAttached: Int, photo: String) {

        this.title = title
        this.subtitle = subtitle
        this.time = time
        if (photoAttached == 1) {
            this.photoAttached = photoAttached
            this.photo = photo
        }
    }

    constructor()
}

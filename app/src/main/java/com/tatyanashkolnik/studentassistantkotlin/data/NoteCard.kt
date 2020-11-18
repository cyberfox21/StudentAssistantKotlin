package com.tatyanashkolnik.studentassistantkotlin.data

class NoteCard {

    var title: String = null.toString()
    var subtitle: String = null.toString()
    var time: String = null.toString()
    var photo: String = null.toString()
    var priority: String = null.toString()
    var path: String = null.toString()
    var photoAttached: String = "0"

    constructor(title: String, subtitle: String, time: String, photoAttached: String, photo: String, priority: String, path: String) {

        this.title = title
        this.subtitle = subtitle
        this.priority = priority
        this.time = time
        this.photoAttached = photoAttached
        this.photo = photo
        this.path = path
    }

    constructor()
}

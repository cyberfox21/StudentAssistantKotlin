package com.tatyanashkolnik.studentassistantkotlin.data

class NoteCard {

    var service: String = null.toString()
    var login: String = null.toString()
    var password: String = null.toString()

    constructor(service: String, login: String, password: String) {
        this.service = service
        this.login = login
        this.password = password
    }

    constructor()


}
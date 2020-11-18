package com.tatyanashkolnik.studentassistantkotlin.data

class PasswordCard {
    var service: String = null.toString()
    var login: String = null.toString()
    var password: String = null.toString()
    var path: String = null.toString()

    constructor(service: String, login: String, password: String, path: String) {
        this.service = service
        this.login = login
        this.password = password
        this.path = path
    }

    constructor()
}

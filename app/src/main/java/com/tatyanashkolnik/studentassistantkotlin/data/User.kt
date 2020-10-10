package com.tatyanashkolnik.studentassistantkotlin.data

class User {
    var uid: String = null.toString()
    var username: String = null.toString()
    var password: String = null.toString()
    var profileImageUrl: String = null.toString()

    constructor(uid: String, username: String, password: String, profileImageUrl: String) {
        this.uid = uid
        this.username = username
        this.password = password
        this.profileImageUrl = profileImageUrl
    }

    constructor()
}

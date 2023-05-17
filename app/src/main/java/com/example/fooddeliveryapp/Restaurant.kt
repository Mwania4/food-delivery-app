package com.example.fooddeliveryapp

class Restaurant {
    var name:String = ""
    var address:String = ""
    var hours:String = ""
    var image:String = ""
    var id:String = ""

    constructor(name: String, address: String, hours: String, image: String, id: String) {
        this.name = name
        this.address = address
        this.hours = hours
        this.image = image
        this.id = id
    }
    constructor()
}
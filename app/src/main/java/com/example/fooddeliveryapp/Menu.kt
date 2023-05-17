package com.example.fooddeliveryapp

class Menu {
    var type:String = ""
    var price:String = ""
    var quantity:String = ""
    var image:String = ""
    var id:String = ""

    constructor(type: String, price: String, quantity: String, image: String, id: String) {
        this.type = type
        this.price = price
        this.quantity = quantity
        this.image = image
        this.id = id
    }
    constructor()
}
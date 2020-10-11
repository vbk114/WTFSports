package com.e5ctech.wtfsports.accounts.models

data class UpdateUserResponse(var status: Boolean,
    var message: String,
    var Response: CResponse) {

    data class CResponse(var id: Int,
        var name: String,
        var contactno: String,
        var location: String,
        var dob: String){

    }
}

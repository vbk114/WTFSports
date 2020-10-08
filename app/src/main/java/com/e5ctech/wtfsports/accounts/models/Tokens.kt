package com.e5ctech.wtfsports.accounts.models

import java.io.Serializable

class Tokens(
    var refresh: String = "",
    var access: String = ""
):Serializable {
}
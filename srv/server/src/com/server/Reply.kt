package com.server.com.server

import java.io.Serializable
import java.net.InetAddress
import java.util.*


class Reply(adress: InetAddress, port: Int, valeur : Any) {
    // dans le cas d'une reponse, les donnee associe
    var destination: InetAddress? = null        // destinataire du message
    var destinationPort: Int = 0
    val value : Any

    init {
        destinationPort = port
        destination = adress
        value = valeur

    }



    /*fun toString(): String {
        var output = super.toString()
        if (this.value != null) {
            if (this.value is Array<Match>) {
                output += ", value=" + Arrays.toString(this.value as Array<Match>?) + "]"
            } else {
                output += ", value=" + this.value!!.toString() + "]"
            }
        }
        return output
    }*/

}

import kotlinx.io.core.String
import java.io.Serializable
import java.net.InetAddress


class Request : Serializable{

    //protected var type: Int = 0

    var messageID: Int = 0

    var destination: InetAddress? = null
    var destinationPort: Int = 0
    var argument: ArrayList<Any>? = null
    var option: Option? = null
    enum class Option {
        list, detail, betInfo
    }

    //private val serialVersionUID = 361933411720337979L

    //private val MAX_NUM = 50000
    //private var numRequest = 0




    @Synchronized
    fun craftGetMatchList(adress: InetAddress, port: Int): Request {
        val request = Request()
        request.option = Option.list
        request.destinationPort = port
        request.destination = adress
        return request
    }

    @Synchronized
    fun craftGetMatchDetail(adress: InetAddress, port: Int, idMatch: Int): Request {
        val request = Request()
        request.option = Option.detail
        val arg = ArrayList<Any>()
        arg.add(idMatch)
        request.argument = arg
        request.destinationPort = (port)
        request.destination = (adress)
        return request
    }

    /*@Synchronized
    fun craftGetBetInfo(adress: InetAddress, port: Int, idMatch: Int, idBet: String): Request {
        val request = Request()
        request.methode = methodes.betInfo
        val arg = arrayOf<Any>(idMatch, idBet)
        request.argument = arg
        request.setDestinationPort(port)
        request.setDestination(adress)
        request.setNumero(numRequest)
        incrementNumRequest()
        return request
    }

    internal fun incrementNumRequest() {
        if (numRequest == MAX_NUM) {
            numRequest = 0
        }
        numRequest++
    }*/
}

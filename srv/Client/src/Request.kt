
import kotlinx.io.core.String
import java.io.Serializable
import java.net.InetAddress


class Request : Serializable{

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
        request.argument = ArrayList<Any>()
        request.argument?.add(idMatch)
        request.destinationPort = (port)
        request.destination = (adress)
        return request
    }

    fun craftBet(adress: InetAddress, port: Int, idMatch: Int, choice : Int, idBet: Float): Request {
        val request = Request()
        request.option = Option.betInfo
        val arg = arrayListOf<Any>(idMatch, choice, idBet)
        request.argument = arg
        request.destinationPort = (port)
        request.destination = (adress)
        return request
    }

}

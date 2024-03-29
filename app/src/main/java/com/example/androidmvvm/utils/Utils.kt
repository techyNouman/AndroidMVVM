import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class Utils {

     fun isValidEmail(email: CharSequence): Boolean {
        return if (!TextUtils.isEmpty(email)) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else false
    }

     fun isValidPhoneNumber(phoneNumber: CharSequence): Boolean {
        return if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length == 10) {
            Patterns.PHONE.matcher(phoneNumber).matches()
        } else false
    }


    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            ?: return false
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
    //            return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }

            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val networks: Array<Network> = cm.allNetworks
                for (n in networks) {
                    val nInfo: NetworkInfo = cm.getNetworkInfo(n)!!
                    if (nInfo != null && nInfo.isConnected) return true
                }
            }
            else -> {
                val networks = cm.allNetworkInfo
                for (nInfo in networks) {
                    if (nInfo != null && nInfo.isConnected) return true
                }
            }
        }
        return false
    }


    fun convertISOTimeToDate(isoTime: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        var convertedDate: Date? = null
        var formattedDate: String? = null
        try {
            convertedDate = sdf.parse(isoTime)
            formattedDate = SimpleDateFormat("yyyy-MM-dd").format(convertedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formattedDate
    }

}
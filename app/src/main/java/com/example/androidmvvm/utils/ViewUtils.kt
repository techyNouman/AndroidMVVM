
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.*
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.androidmvvm.R
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

fun <A : Activity> Activity.startActivity(activity: Class<A>) {
    Intent(this, activity).also {
        startActivity(it)
    }
}

fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}


fun View.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> this.snackbar(
            resources.getString(R.string.checkInternet),
            retry
        )
//        failure.isNetworkError -> toast("Please check your internet connection")
        failure.errorCode == 403 -> {
//            if (this is LoginFragment) {
//                requireView().snackbar("You've entered incorrect email or password")
//            } else {
//                logout()
//            }
            this.snackbar("Unauthorized access!")
        }

        failure.errorCode == 401 -> {
            this.snackbar("Token expired please login again and continue!")
        }
        failure.errorCode == 100 -> {
            this.snackbar("Failed to connect to server!")
        }
        else -> {
            try {
                val error = failure.errorBody?.string().toString()
                val jsonObj = JSONObject(error)
                val message = jsonObj.optString("message")
                this.snackbar(message)
            } catch (e: Exception) {
                Log.e("handleApiExp",""+e)
            }
        }
    }
}

fun Fragment.getToken(): String {
    var bearerToken = "Bearer "
    val token =
        AppPreference.getInstance()!!.getString(requireActivity(), AppConstants.ACCESS_TOKEN)
    if (!token.isNullOrEmpty()) {
        bearerToken = "$bearerToken$token"
    }
    return bearerToken
}

fun Activity.getToken(): String {
    var bearerToken = "Bearer "
    val token =
        AppPreference.getInstance()!!.getString(this, AppConstants.ACCESS_TOKEN)
    if (!token.isNullOrEmpty()) {
        bearerToken = "$bearerToken$token"
    }
    return bearerToken
}

fun Activity.logout() {
    logout(this)
}

fun Fragment.logout() {
    logout(requireContext())
}

private fun logout(context: Context){
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder.setMessage("Session timed out! Please login again to continue.")
    builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
        dialog.dismiss()
        AppPreference.getInstance()!!.removeAll(context)
//        Intent(context, GetStartedActivity::class.java).also {
//            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            context.startActivity(it)
//        }
    })
    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
}

fun dateFormatOne(date: String): String{
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val output = SimpleDateFormat("E, dd MMM yyyy")
    val formatDate: Date?
    var convertedDate: String? = null
    try {
        formatDate = input.parse(date)
       convertedDate =  output.format(formatDate!!)
    } catch (e: ParseException) {
        Log.e("dateFormatOne", "error-> $e")
    }

    return convertedDate!!
}

fun dateFormatTwo(date: String): String{
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val output = SimpleDateFormat("yyyy-MM-dd")
    val formatDate: Date?
    var convertedDate: String? = null
    try {
        formatDate = input.parse(date)
        convertedDate =  output.format(formatDate!!)
    } catch (e: ParseException) {
        Log.e("dateFormatTwo", "error-> $e")
    }

    return convertedDate!!
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertToLocalDateTime(dateStr: String, outputFormat:String = "dd-MMM-yyyy hh:mm:ss a"): String{
    val inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    var convertedDate: String? = null
    try {
        val parser: DateTimeFormatter =
            DateTimeFormatter.ofPattern(inputFormat, Locale.ENGLISH)
        val ldt: LocalDateTime = LocalDateTime.parse(dateStr, parser)
        val zdtUtc: ZonedDateTime = ldt.atZone(ZoneId.of("Etc/UTC"))
        val zdtIndia: ZonedDateTime = zdtUtc.withZoneSameInstant(ZoneId.systemDefault())
        val odt: OffsetDateTime = zdtIndia.toOffsetDateTime()
        val outputFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(outputFormat)
        convertedDate =  outputFormatter.format(odt)
    } catch (e: ParseException) {
        Log.e("convertToLocalDateTime", "error-> $e")
    }
    return convertedDate!!
}

fun dateFormatThree(date: String): String{
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val output = SimpleDateFormat("yyyy MMM dd HH:mm")
    val formatDate: Date?
    var convertedDate: String? = null
    try {
        formatDate = input.parse(date)
        convertedDate =  output.format(formatDate!!)
    } catch (e: ParseException) {
        Log.e("dateFormatThree", "error-> $e")
    }

    return convertedDate!!
}


fun dateFormatFour(date: Long): String{
//    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
//    val output = SimpleDateFormat("E, dd MMM yyyy")
    val formatDate: Date?
    var convertedDate: String? = null
//    try {
//        formatDate = input.parse(date)
//        convertedDate =  output.format(formatDate!!)
//    } catch (e: ParseException) {
//        Log.e("dateFormatOne", "error-> $e")
//    }

    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        formatDate = Date(date * 1000)
        convertedDate = sdf.format(formatDate)
    } catch (e: Exception) {
        return e.toString()
    }

    return convertedDate!!
}

fun alert(context: Context, message: String) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder.setMessage(message)
    builder.setPositiveButton("Okay", DialogInterface.OnClickListener { dialog, which ->
        dialog.dismiss()

    })
    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
}

fun roundOffValue(value: Double): String {
    val roundedValue = "%.2f".format(Math.round(value).toDouble())
    return roundedValue
}

fun getImageRotation(context: Context, filePath: String, fileUri: Uri): Int? {
    var rotation = 0
    var exif: ExifInterface? = null
    try {
        when (Build.VERSION.SDK_INT) {
            in Int.MIN_VALUE..24 -> exif = ExifInterface(filePath)
            else -> exif = ExifInterface(context.contentResolver.openInputStream(fileUri)!!)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    if (exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL) != null){
        rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return rotation
    }else{
        return 0
    }
}

fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
    val returnBitmap: Bitmap? = null

    val matrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_NORMAL -> return bitmap
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
            matrix.setRotate(180f)
            matrix.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_TRANSPOSE -> {
            matrix.setRotate(90f)
            matrix.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
        ExifInterface.ORIENTATION_TRANSVERSE -> {
            matrix.setRotate(-90f)
            matrix.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
        else -> return bitmap
    }
    try {
        val bmRotated =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return bmRotated
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
    }
    return returnBitmap!!
}

fun loadingDialog(mContext: Context?): Dialog {
    val pd = Dialog(mContext!!, android.R.style.Theme_Dialog)
    val view: View = LayoutInflater.from(mContext).inflate(R.layout.layout_progress, null)
    pd.requestWindowFeature(Window.FEATURE_NO_TITLE)
    pd.window!!.setBackgroundDrawableResource(R.color.transparent)
    pd.setContentView(view)
    return pd
}




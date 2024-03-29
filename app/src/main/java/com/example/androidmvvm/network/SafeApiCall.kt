import android.util.Log
import com.squareup.picasso.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                if (BuildConfig.DEBUG){
                    Log.e("throwable",""+throwable)
                }
                when (throwable) {
                    is HttpException -> {
                        if (BuildConfig.DEBUG){
                           Log.e("SafeApiExp",""+throwable.code() + " " + throwable.response()?.errorBody().toString())
                        }
                        Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
                    }
                    else -> {
                        if (BuildConfig.DEBUG){
                            Log.e("SafeApiElseExp",""+throwable.message)
                        }
                        if (throwable.message!!.contains("Failed to connect to")){
                            Resource.Failure(false, 100, null)
                        }else{
                            Resource.Failure(true, null, null)
                        }
                    }
                }
            }
        }
    }
}
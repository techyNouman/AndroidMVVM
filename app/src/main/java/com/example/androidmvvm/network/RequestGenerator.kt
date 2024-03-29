
import android.content.Context
import com.google.gson.GsonBuilder
import com.squareup.picasso.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RequestGenerator {

    companion object {
        fun create(context: Context, baseUrl: String): NetworkService {
            var apiServices: NetworkService? = null
//            val networkConnectionInterceptor = NetworkConnectionInterceptor(context)

            if (Utils().isNetworkAvailable(context)){
                if (apiServices == null) {
                    val gson = GsonBuilder().setLenient().create()

                    val builder = OkHttpClient.Builder()
                    if (BuildConfig.DEBUG) {
                        val logging = HttpLoggingInterceptor()
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                        builder.interceptors().add(logging)
                    }

//                    builder.addInterceptor(networkConnectionInterceptor)

                    val client = builder.connectTimeout(0, java.util.concurrent.TimeUnit.SECONDS)
                        .writeTimeout(0, java.util.concurrent.TimeUnit.SECONDS)
                        .readTimeout(0, java.util.concurrent.TimeUnit.SECONDS)
                        .build()

                    val retrofit = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(baseUrl)
                        .client(client)
                        .build()

                    apiServices = retrofit.create<NetworkService>(NetworkService::class.java)
                }

            }else{
                context.toast("No internet connection!")
            }
            return apiServices!!
        }

    }
}
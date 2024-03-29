
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository): ViewModel(){

//    private val _verifyMobileResponse: MutableLiveData<Resource<VerifyMobileResponse>> = MutableLiveData()
//    private val _verifyOtpResponse: MutableLiveData<Resource<VerifyOtpResponse>> = MutableLiveData()
//    private val _signupResponse: MutableLiveData<Resource<SignupResponse>> = MutableLiveData()
//    private val _getProfileResponse: MutableLiveData<Resource<GetProfileResponse>> = MutableLiveData()
//    private val _deleteResponse: MutableLiveData<Resource<DeleteResponse>> = MutableLiveData()
//
//    val verifyMobileResponse: LiveData<Resource<VerifyMobileResponse>> get() = _verifyMobileResponse
//    val verifyOtpResponse: LiveData<Resource<VerifyOtpResponse>> get() = _verifyOtpResponse
//    val signupResponse: LiveData<Resource<SignupResponse>> get() = _signupResponse
//    val getProfileResponse: LiveData<Resource<GetProfileResponse>> get() = _getProfileResponse
//    val deleteResponse: LiveData<Resource<DeleteResponse>> get() = _deleteResponse
//
//    fun verifyMobile(
//        req: VerifyMobileRequest
//    ) = viewModelScope.launch {
//        _verifyMobileResponse.value = Resource.Loading
//        _verifyMobileResponse.value = repository.verifyMobile(req)
//    }
//
//    fun verifyOtp(
//        id: String,
//        req: VerifyOtpRequest
//    ) = viewModelScope.launch {
//        _verifyOtpResponse.value = Resource.Loading
//        _verifyOtpResponse.value = repository.verifyOtp(id, req)
//    }
//
//    fun signup(
//        id: String,
//        req: SignupRequest
//    ) = viewModelScope.launch {
//        _signupResponse.value = Resource.Loading
//        _signupResponse.value = repository.signup(id, req)
//    }
//
//    fun getProfile(
//        token: String
//    ) = viewModelScope.launch {
//        _getProfileResponse.value = Resource.Loading
//        _getProfileResponse.value = repository.getProfile(token)
//    }
//
//    fun deleteAccount(
//        status: String,
//        mobile: String
//    ) = viewModelScope.launch {
//        _deleteResponse.value = Resource.Loading
//        _deleteResponse.value = repository.deleteAccount(status,mobile)
//    }
}
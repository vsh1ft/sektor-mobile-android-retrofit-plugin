package lt.boldadmin.sektor.mobile.android.plugin.retrofit.test.unit.web.factory

import com.nhaarman.mockitokotlin2.*
import lt.boldadmin.sektor.mobile.android.plugin.retrofit.PropertyLoader
import lt.boldadmin.sektor.mobile.android.plugin.retrofit.web.factory.RetrofitFactory
import lt.boldadmin.sektor.mobile.android.plugin.retrofit.web.interceptor.AuthenticationInterceptor
import lt.boldadmin.sektor.mobile.android.plugin.retrofit.web.service.CollaboratorWebService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import java.util.*

@ExtendWith(MockitoExtension::class)
class RetrofitFactoryTest {

    @Mock
    private lateinit var retrofitBuilderSpy: Builder

    @Mock
    private lateinit var propertyLoaderSpy: PropertyLoader

    @Mock
    private lateinit var okHttpClientSpy: OkHttpClient

    @Mock
    private lateinit var okHttpBuilderSpy: OkHttpClient.Builder

    private lateinit var retrofitFactory: RetrofitFactory

    @BeforeEach
    fun `Set up`() {
        mockOkHttpClientBuilder()
        retrofitFactory = RetrofitFactory(propertyLoaderSpy, retrofitBuilderSpy, okHttpClientSpy)
    }

    @Test
    fun `Gets Retrofit service without token`() {
        mockRetrofitBuilder()

        retrofitFactory.create(CollaboratorWebService::class.java)

        verifyOkHttpBuilderMock()
        verify(okHttpClientSpy, never()).interceptors()
        verify(retrofitBuilderSpy).client(okHttpClientSpy)
        verify(retrofitBuilderSpy).addConverterFactory(any())
        verify(retrofitBuilderSpy).baseUrl(any<String>())
        verify(retrofitBuilderSpy).build()
        verify(propertyLoaderSpy, times(2)).load(any())
        assertNotNull(retrofitFactory)
    }

    @Test
    fun `Gets Retrofit service with token`() {
        val interceptorsSpy: ArrayList<Interceptor> = mock()
        mockRetrofitBuilder()
        doReturn(interceptorsSpy).`when`(okHttpBuilderSpy).interceptors()
        doReturn(true).`when`(interceptorsSpy).add(any())

        retrofitFactory.create(CollaboratorWebService::class.java, "token")

        verifyOkHttpBuilderMock()
        verify(okHttpBuilderSpy).interceptors()
        verify(interceptorsSpy).add(any<AuthenticationInterceptor>())
        verify(retrofitBuilderSpy).client(okHttpClientSpy)
        verify(retrofitBuilderSpy).addConverterFactory(any())
        verify(retrofitBuilderSpy).baseUrl(any<String>())
        verify(retrofitBuilderSpy).build()
        verify(propertyLoaderSpy, times(2)).load(any())
        assertNotNull(retrofitFactory)
    }

    private fun mockOkHttpClientBuilder() {
        val propertyStub: Properties = mock()
        doReturn(10).`when`(propertyStub)[any()]
        doReturn(propertyStub).`when`(propertyLoaderSpy).load(eq("timeout.properties"))
        doReturn(okHttpBuilderSpy).`when`(okHttpClientSpy).newBuilder()
        doReturn(okHttpBuilderSpy).`when`(okHttpBuilderSpy).readTimeout(any(), any())
        doReturn(okHttpBuilderSpy).`when`(okHttpBuilderSpy).writeTimeout(any(), any())
        doReturn(okHttpBuilderSpy).`when`(okHttpBuilderSpy).connectTimeout(any(), any())
        doReturn(okHttpClientSpy).`when`(okHttpBuilderSpy).build()
    }

    private fun mockRetrofitBuilder() {
        val retrofitDummy: Retrofit = mock()
        doReturn(retrofitDummy).`when`(retrofitBuilderSpy).build()
        val propertyStub: Properties = mock()
        doReturn("URL").`when`(propertyStub)[any()]
        doReturn(propertyStub).`when`(propertyLoaderSpy).load(eq("config.properties"))
        doReturn(retrofitBuilderSpy).`when`(retrofitBuilderSpy).addConverterFactory(any())
        doReturn(retrofitBuilderSpy).`when`(retrofitBuilderSpy).baseUrl(any<String>())
    }

    private fun verifyOkHttpBuilderMock() {
        verify(okHttpClientSpy).newBuilder()
        verify(okHttpBuilderSpy).readTimeout(any(), any())
        verify(okHttpBuilderSpy).writeTimeout(any(), any())
        verify(okHttpBuilderSpy).connectTimeout(any(), any())
    }
}

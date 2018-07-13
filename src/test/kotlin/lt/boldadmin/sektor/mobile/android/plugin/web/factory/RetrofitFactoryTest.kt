package lt.boldadmin.sektor.mobile.android.plugin.web.factory

import com.nhaarman.mockito_kotlin.*
import lt.boldadmin.sektor.mobile.android.plugin.PropertyLoader
import lt.boldadmin.sektor.mobile.android.plugin.web.interceptor.AuthenticationInterceptor
import lt.boldadmin.sektor.mobile.android.plugin.web.service.WorkTimeWebService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class RetrofitFactoryTest {

    @Mock
    private lateinit var retrofitBuilderMock: Builder

    @Mock
    private lateinit var propertyLoaderMock: PropertyLoader

    @Mock
    private lateinit var okHttpClientMock: OkHttpClient

    @Mock
    private lateinit var okHttpBuilderMock: OkHttpClient.Builder

    private lateinit var retrofitFactory: RetrofitFactory

    @Before
    fun `Set up`() {
        mockOkHttpClientBuilder()
        retrofitFactory = RetrofitFactory(
                propertyLoaderMock,
                retrofitBuilderMock,
                okHttpClientMock
        )
    }

    @Test
    fun `Gets Retrofit service without token`() {
        mockOkHttpClientBuilder()
        mockRetrofitBuilder()

        retrofitFactory.create(WorkTimeWebService::class.java)

        verifyOkHttpBuilderMock()
        verify(okHttpClientMock, never()).interceptors()
        verify(retrofitBuilderMock).client(okHttpClientMock)
        verify(retrofitBuilderMock).addConverterFactory(any())
        verify(retrofitBuilderMock).baseUrl(any<String>())
        verify(retrofitBuilderMock).build()
        verify(propertyLoaderMock, times(2)).load(any())
        assertNotNull(retrofitFactory)
    }

    @Test
    fun `Gets Retrofit service with token`() {
        val interceptorsMock = mock<ArrayList<Interceptor>>()
        mockRetrofitBuilder()
        mockOkHttpClientBuilder()
        doReturn(interceptorsMock).`when`(okHttpBuilderMock).interceptors()
        doReturn(true).`when`(interceptorsMock).add(any())

        retrofitFactory.create(WorkTimeWebService::class.java, "token")

        verifyOkHttpBuilderMock()
        verify(okHttpBuilderMock).interceptors()
        verify(interceptorsMock).add(any<AuthenticationInterceptor>())
        verify(retrofitBuilderMock).client(okHttpClientMock)
        verify(retrofitBuilderMock).addConverterFactory(any())
        verify(retrofitBuilderMock).baseUrl(any<String>())
        verify(retrofitBuilderMock).build()
        verify(propertyLoaderMock, times(2)).load(any())
        assertNotNull(retrofitFactory)
    }

    private fun mockOkHttpClientBuilder() {
        val propertyMock = mock<Properties>()
        doReturn(10).`when`(propertyMock)[any()]
        doReturn(propertyMock).`when`(propertyLoaderMock).load(eq("timeout.properties"))
        doReturn(okHttpBuilderMock).`when`(okHttpClientMock).newBuilder()
        doReturn(okHttpBuilderMock).`when`(okHttpBuilderMock).readTimeout(any(), any())
        doReturn(okHttpBuilderMock).`when`(okHttpBuilderMock).writeTimeout(any(), any())
        doReturn(okHttpBuilderMock).`when`(okHttpBuilderMock).connectTimeout(any(), any())
        doReturn(okHttpClientMock).`when`(okHttpBuilderMock).build()
    }

    private fun mockRetrofitBuilder() {
        val retrofitMock = mock<Retrofit>()
        doReturn(retrofitMock).`when`(retrofitBuilderMock).build()
        val propertyMock = mock<Properties>()
        doReturn("URL").`when`(propertyMock)[any()]
        doReturn(propertyMock).`when`(propertyLoaderMock).load(eq("config.properties"))
        doReturn(retrofitBuilderMock).`when`(retrofitBuilderMock).addConverterFactory(any())
        doReturn(retrofitBuilderMock).`when`(retrofitBuilderMock).baseUrl(any<String>())
    }

    private fun verifyOkHttpBuilderMock() {
        verify(okHttpClientMock).newBuilder()
        verify(okHttpBuilderMock).readTimeout(any(), any())
        verify(okHttpBuilderMock).writeTimeout(any(), any())
        verify(okHttpBuilderMock).connectTimeout(any(), any())
    }
}
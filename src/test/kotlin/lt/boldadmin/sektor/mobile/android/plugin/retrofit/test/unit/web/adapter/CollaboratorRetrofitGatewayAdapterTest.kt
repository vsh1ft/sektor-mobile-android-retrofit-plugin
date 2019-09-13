package lt.boldadmin.sektor.mobile.android.plugin.retrofit.test.unit.web.adapter

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.same
import com.nhaarman.mockitokotlin2.verify
import lt.boldadmin.sektor.mobile.android.api.valueobject.GpsCoordinates
import lt.boldadmin.sektor.mobile.android.api.valueobject.WorkTime
import lt.boldadmin.sektor.mobile.android.plugin.retrofit.web.adapter.CollaboratorRetrofitGatewayAdapter
import lt.boldadmin.sektor.mobile.android.plugin.retrofit.web.service.CollaboratorWebService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import retrofit2.Call
import retrofit2.Response

@ExtendWith(MockitoExtension::class)
class CollaboratorRetrofitGatewayAdapterTest {

    @Mock
    private lateinit var collaboratorWebServiceSpy: CollaboratorWebService

    @Mock
    private lateinit var callSpy: Call<Void>

    @Mock
    private lateinit var responseDummy: Response<Void>

    private lateinit var adapter: CollaboratorRetrofitGatewayAdapter

    @BeforeEach
    fun setUp() {
        adapter = CollaboratorRetrofitGatewayAdapter("token", collaboratorWebServiceSpy)
    }

    @Test
    fun `Updates location using Retrofit`() {
        val locationMock = GpsCoordinates(15.0, 20.0)
        doReturn(callSpy).`when`(collaboratorWebServiceSpy).updateLocation(eq(locationMock))
        doReturn(responseDummy).`when`(callSpy).execute()

        adapter.updateLocation(locationMock)

        verify(collaboratorWebServiceSpy).updateLocation(same(locationMock))
        verify(callSpy).execute()
    }

    @Test
    fun `Gets work time`() {
        val workTimeMock = WorkTime()
        val responseMock = Response.success(WorkTime())
        doReturn(callSpy).`when`(collaboratorWebServiceSpy).getWorkTime()
        doReturn(responseMock).`when`(callSpy).execute()

        val workTime = adapter.getWorkTime()

        assertEquals(workTimeMock, workTime)
    }
}

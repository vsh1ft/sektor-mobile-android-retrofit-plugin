package lt.boldadmin.sektor.mobile.android.plugin.retrofit.web.service

import lt.boldadmin.sektor.mobile.android.api.valueobject.DayMinuteInterval
import lt.boldadmin.sektor.mobile.android.api.valueobject.GpsCoordinates
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.*

interface CollaboratorWebService {

    @POST("/collaborator/location/coordinates")
    fun updateCoordinates(@Body gpsCoordinates: GpsCoordinates): Call<Void>

    @GET("/collaborator/work-week")
    fun getWorkWeek(): Call<SortedSet<DayMinuteInterval>>
}

package com.example.underpressurea.maps

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.underpressurea.Model.MyPlaces
import com.example.underpressurea.R
import com.example.underpressurea.common.Common
import com.example.underpressurea.remote.IGoogleAPIService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    companion object {
        fun newInstance() = MapsFragment()
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val PLACE_PICKER_REQUEST = 3
    }


    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMapView: MapView
    private lateinit var mView: View
    private lateinit var lastLocation: Location

    //private var latitude: Double = 0.toDouble()
    //private var longitude: Double = 0.toDouble()

    lateinit var mService: IGoogleAPIService
    internal lateinit var currentPlace: MyPlaces

    private lateinit var viewModel: MapsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this@MapsFragment.context!!)
        mView = inflater.inflate(R.layout.maps_fragment, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MapsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMapView = mView.findViewById(R.id.mapView)
        mMapView.onCreate(null)
        mMapView.onResume()
        mMapView.getMapAsync(this)

        /**init place sarvice*/
        mService = Common.googleApiService

        val builder = AlertDialog.Builder(this@MapsFragment.context)
        builder.setTitle("High pressure values")

        // Display a message on alert dialog
        builder.setMessage("Your pressure is not so good.\nIt's better for you to go to the pharmacy.")

        // Display a neutral button on alert dialog
        builder.setNeutralButton("Ok"){_,_ ->
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }

    override fun onMapReady(p0: GoogleMap?) {

        if (p0 != null) {
            map = p0
            map.getUiSettings().setZoomControlsEnabled(true)
            map.setOnMarkerClickListener(this)
            setUpMap()
        }


    }

    override fun onMarkerClick(p0: Marker?) = false

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this@MapsFragment.context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }


        /**isMyLocationEnabled = true enables the my-location layer which draws a light blue dot on the user’s location.
         * It also adds a button to the map that, when tapped, centers the map on the user’s location*/
        map.isMyLocationEnabled = true

        /**fusedLocationClient.getLastLocation() gives you the most recent location currently available.*/
        fusedLocationClient.lastLocation.addOnSuccessListener(this@MapsFragment.requireActivity()) { location ->
            /**If you were able to retrieve the the most recent location,
             * then move the camera to the user’s current location.*/
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                nearByPlace("pharmacy")
                placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                //nearByPlace("pharmacy")
            }
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
        /**Create a MarkerOptions object and sets the user’s current location as the position for the marker*/
        val markerOptions = MarkerOptions().position(location)
        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(resources, R.mipmap.ic_user_location)
            )
        )

        val titleStr = getAddress(location)
        markerOptions.title(titleStr)
        /**Add the marker to the map*/
        map.addMarker(markerOptions)
    }

    private fun getAddress(latLng: LatLng): String {
        /**Creates a Geocoder object to turn a latitude and longitude coordinate into an address and vice versa.*/
        val geocoder = Geocoder(this@MapsFragment.context, Locale.getDefault())
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""
        val strReturnedAddress: StringBuilder = StringBuilder("")
        Log.i("MapsActivity", "prova")

        try {
            /**Asks the geocoder to get the address from the location passed to the method.*/
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            /**If the response contains any address, then append it to a string and return.*/
            if (null != addresses && !addresses.isEmpty()) {
                Log.i("MapsActivity", "OKKKK")
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex+1) {
                    //Log.i("MapsActivity", "Aaaaaaaa")
                    //addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                   strReturnedAddress.append(address.getAddressLine(i)).append("\n")

                }
                addressText = strReturnedAddress.toString()
                Log.i("MapsActivity", "ATTENZIONE"+addressText)
            }

        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }

    private fun nearByPlace(typePlace: String) {
        /**Clear all marker on Map*/
        map.clear()
        /**Build URL request base on location*/
        val url = getUrl(lastLocation.latitude, lastLocation.longitude, typePlace)

        mService.getNearbyPlaces(url).enqueue(object : Callback<MyPlaces> {
            override fun onResponse(call: Call<MyPlaces>, response: Response<MyPlaces>) {
                currentPlace = response.body()!!
                //val status = currentPlace.status
                //Log.d("NEARBY_DEBUG_STATUS",status)

                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.results!!.size) {

                        Log.d("NEARBY_DEBUG","success")

                        val markerOptions = MarkerOptions()
                        val googlePlace = response.body()!!.results!![i]
                        val lat = googlePlace.geometry!!.location!!.lat
                        val lng = googlePlace.geometry!!.location!!.lng
                        val placeName = googlePlace.name
                        val latLng = LatLng(lat, lng)

                        markerOptions.position(latLng)
                        markerOptions.title(placeName).snippet(getAddress(latLng))
                        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pharmacy))
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pharmacy))

                        //markerOptions.snippet(i.toString()) // Assign index for markers
                        //markerOptions.snippet(getAddress(latLng))

                        //Add marker to map
                        map.addMarker(markerOptions)

                        //Move camera
                        //map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                        //map.animateCamera(CameraUpdateFactory.zoomTo(20f))

                    }

                }

                Log.d("NEARBY_DEBUG","not success")
            }

            override fun onFailure(call: Call<MyPlaces>, t: Throwable) {
                Toast.makeText(this@MapsFragment.context, "" + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUrl(latitude: Double, longitude: Double, typePlace: String): String {
        val googlePlaceUrl =
            StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")
        googlePlaceUrl.append("&radius=2000")   // 1 Km
        googlePlaceUrl.append("&type=$typePlace")
        googlePlaceUrl.append("&key=AIzaSyA5ysZ3pOQrv7Hmu2SXV34mguHHY-Dmf0g")

        Log.d("URL_DEBUG", googlePlaceUrl.toString())
        return googlePlaceUrl.toString()

    }


}

package com.shivam.complete_dashboard.Holders;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shivam.complete_dashboard.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by shivam sharma on 10/20/2017.
 */

public class AdViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public View adCardView;
    public TextView tv_Name, tv_Address, tv_distance, ad_room_type1, ad_room_type2;
    public ImageView adcard_cover_photo, img_location, img_bookmark;

    public double distanceInMeters;

    public AdViewHolder(View iteadCardView) {
        super(iteadCardView);
        adCardView = iteadCardView;
        tv_Name = adCardView.findViewById(R.id.adcard_name);
        tv_Address = adCardView.findViewById(R.id.adcard_address);
        ad_room_type1 = adCardView.findViewById(R.id.ad_room_type1);
        ad_room_type2 = adCardView.findViewById(R.id.ad_room_type2);
        tv_distance = adCardView.findViewById(R.id.adcard_distance);
        adcard_cover_photo = adCardView.findViewById(R.id.adcard_cover_photo);
        img_location = adCardView.findViewById(R.id.adcard_location);
        img_bookmark = adCardView.findViewById(R.id.adcard_bookmark);
    }
    public void setName(String accomName)
    {
        tv_Name.setText(accomName);
    }
    public void setAddress(String address)
    {
        tv_Address.setText(address);
    }
    public void setImage(final String imgURL) {
        Picasso.with(adcard_cover_photo.getContext()).load(imgURL).placeholder(R.drawable.room).into(adcard_cover_photo, new Callback()
        {
            @Override
            public void onSuccess()
            {
                Toast.makeText(adcard_cover_photo.getContext(),"image loded",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError()
            {
                Picasso.with(adcard_cover_photo.getContext()).load(imgURL).placeholder(R.drawable.room).into(adcard_cover_photo);
            }
        });
    }
    public void setDistance(double sourceLatitude, double sourceLongitude, Double latitude, Double longitude)
    {
        tv_distance.setText(getDistance(sourceLatitude,sourceLongitude, latitude, longitude));
//        Location locationA = new Location("point A");
//        locationA.setLatitude(sourceLatitude);
//        locationA.setLongitude(sourceLongitude);
//        Location locationB = new Location("point B");
//        locationB.setLatitude(latitude);
//        locationB.setLongitude(longitude);
//        distanceInMeters = locationA.distanceTo(locationB) ;
//        tv_distance.setText(((int) (distanceInMeters))/1000 + "KM");

        Toast.makeText(img_location.getContext(),"distanceInMeters: " + getDistance(sourceLatitude,sourceLongitude, latitude, longitude) + "",Toast.LENGTH_LONG).show();

    }
    public double getDistanceInMeters()
    {
        return distanceInMeters;
    }
    public void setImg_bookmark()
    {
        Toast.makeText(img_bookmark.getContext(),"bookmark",Toast.LENGTH_LONG).show();

    }
    public void setImg_location()
    {
        Toast.makeText(img_location.getContext(),"location",Toast.LENGTH_LONG).show();
    }
    public void setAd_room_type1(String ad_room_type1)
    {
        this.ad_room_type1.setText("ad_room_type1");
    }
    public void setAd_room_type2(String ad_room_type2)
    {
        this.ad_room_type2.setText(ad_room_type2);
    }
    @Override public void onClick(View v) {
        switch (v.getId())
        {
            /*case R.id.hostel_call:
                Toast.makeText(adCardView.getContext(),"sign in required",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                adCardView.getContext().startActivity(intent);
                break;

            case R.id.img_location:
                Toast.makeText(adCardView.getContext(),"ad_address clicked" + new Global_Ads().getGlobal_latLng(),Toast.LENGTH_LONG).show();
                break;

            case R.id.hostel_bookmark:
                Toast.makeText(adCardView.getContext(),"sign in required",Toast.LENGTH_LONG).show();
                break;

            default:
               Toast.makeText(adCardView.getContext(),"sign in required",Toast.LENGTH_LONG).show();*/
        }
    }


    public String getDistance(final double lat1, final double lon1, final double lat2, final double lon2){
        final String[] parsedDistance = new String[1];
        final StringBuilder response = new StringBuilder();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving");
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                    int ch;
                    while((ch = inputStream.read()) != -1)
                    {
                        response.append((char)ch);
                    }


                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    parsedDistance[0] =distance.getString("text");

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return parsedDistance[0];
    }
}

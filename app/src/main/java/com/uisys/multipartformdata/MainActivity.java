package com.uisys.multipartformdata;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    byte[] byteArray;
    String boundary = "--ARCFormBoundaryvgrvnph55ewmi";
    String base64image;
    String url="http://182.74.133.91:8080/RAPORTAL-1.0/raportal/api/user/userkyc";
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
    Map<String, String> headers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        Intent getIntent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        setHeaders();
       Bitmap bitmap= BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.android_logo);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byteArray = stream.toByteArray();
        base64image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.d("@Ramesh", " " + base64image );

        //      Intent imageInent = Intent.createChooser(intent, "selectImage");
//        startActivityForResult(imageInent, 1);

        VolleyMultiPartRequest volleyMultiPartRequest = new VolleyMultiPartRequest(Request.Method.POST, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("@Ramesh",error.getLocalizedMessage() );

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Map<String, String> headers = new HashMap<>();
                //headers.put("Content-Type", "multipart/form-data;boundary=ARCFormBoundaryvgrvnph55ewmi");

                return MainActivity.this.getHeaders();
            }

            @Override
            public String getBodyContentType() {
                return super.getBodyContentType();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String newLine = "\r\n";
                String contenDisposition1 = null;

                ;
                try {
                    for (int i = 1; i <= 3; i++) {
                        Log.d("@Ramesh ", boundary + newLine);
                        dataOutputStream.writeBytes(boundary);
                        dataOutputStream.writeBytes(newLine);
                        String kycDoc1= "kycDoc1";
                        String fileName="android_logo.png";
                        if (i == 1)
                        //contenDisposition1 = "Content-Disposition:form-data;name=\"kycDoc1\";filename=\"android_logo.png\";";
                        contenDisposition1="Content-Disposition:form-data;name="+"\"" +kycDoc1 +"\";" + "filename=" + "\"" + fileName + "\";";
                           // contenDisposition1 = "Content-Disposition:form-data; name=\"kycDoc1\";";
                        if (i == 2)
                            contenDisposition1 = "Content-Disposition:form-data;name=\"kycDoc2\";filename=\"android_logo.png\";";
                        if (i == 3)
                            contenDisposition1 = "Content-Disposition:form-data;name=\"kycDoc3\";filename=\"android_logo.png\";";

                        Log.d("@Ramesh ", contenDisposition1 + newLine);
                        dataOutputStream.writeBytes(contenDisposition1);
                        dataOutputStream.writeBytes(newLine);
                        String filecontentType = "Content-Type: image/jpeg;";
                        Log.d("@Ramesh ", filecontentType + newLine);
                        dataOutputStream.writeBytes(filecontentType);
                        dataOutputStream.writeBytes(newLine);
                        dataOutputStream.writeBytes(newLine);

                       ByteArrayInputStream fileInputStream = new ByteArrayInputStream(byteArray);
                        int bytesAvailable = fileInputStream.available();

                        int maxBufferSize = 1024 * 1024;
                        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        byte[] buffer = new byte[bufferSize];

                        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {
                            dataOutputStream.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                        }

                        //dataOutputStream.writeBytes(base64image);
                       dataOutputStream.writeBytes(newLine);

                    }

                    dataOutputStream.writeBytes(boundary);
                    dataOutputStream.writeBytes(newLine);

                    contenDisposition1 = "Content-Disposition:form-data; name=\"userKycTblJson\";";
                    Log.d("@Rmesh ", contenDisposition1 + newLine);
                    dataOutputStream.writeBytes(contenDisposition1);
                    dataOutputStream.writeBytes(newLine);

                    String stringcontentType = "Content-Type: application/json;";
                    Log.d("@Ramesh ", stringcontentType + newLine);
                    dataOutputStream.writeBytes(stringcontentType);
                    dataOutputStream.writeBytes(newLine);
                    dataOutputStream.writeBytes(newLine);
                    String kycdocs = "{\"userId\":646,\"kycDocumentTypeId\":[4,6,3]}";
                    Log.d("@Ramesh ", kycdocs + newLine);
                    dataOutputStream.writeBytes(kycdocs);
                    dataOutputStream.writeBytes(newLine);
                    //end of data-tes-
                    Log.d("@Ramesh", boundary + "--");
                    dataOutputStream.writeBytes(boundary + "--");


                } catch (IOException e) {
                    e.printStackTrace();
                }

                return MainActivity.this.byteArrayOutputStream.toByteArray();
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(volleyMultiPartRequest);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.d("@Ramesh",uri.toString());
            File file = new File(getRealPathFromURI(uri));
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            //Bitmap bitmap= BitmapFactory.decodeResource(getApplicationContext().getResources(),
              //      R.drawable.us);

            BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.android_logo);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();


        }

    }

    public String getRealPathFromURI( Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getApplicationContext().getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    void setHeaders()
    {

        headers.put("Content-Type", "multipart/form-data;boundary=ARCFormBoundaryvgrvnph55ewmi");

    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}

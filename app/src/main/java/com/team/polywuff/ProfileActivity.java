/*
 * @author: Christopher Kambayi, x15513473
 *
 */
package com.team.polywuff;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import java.io.File;


public class ProfileActivity extends AppCompatActivity {

    //Dedlaring variables to be used(User, textview, Uri, String, Button, Spinner, String [])
    private FirebaseUser user;
    private static final String APP_ID = "A41D56FF-D61E-46ED-A1A4-09B46FD304FD";

    private String email;
    private TextView name;
    private TextView photoUrl;
    private ImageView avi;
    private Uri url;
    private String loc;
    private Button editName;
    private Button saveChangesBtn;
    private File newPhoto;
    private Spinner locationChanger;

    private String [] province = {"Leinster", "Munster", "Connacht", "Ulster"};

    private User sendBirdUSer;

    private static final int SELECTED_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Getting user info from firebase and Sendbird
        user = FirebaseAuth.getInstance().getCurrentUser();
        sendBirdUSer = SendBird.getCurrentUser();
        email = user.getEmail();

        //Declaring and creating list to be displayed by spinner
        ArrayAdapter<String> list = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_spinner_item, province);
        loc = sendBirdUSer.getNickname();

        //Constructor of textview, spinner and button
        avi = (ImageView) findViewById(R.id.profilePicture);
        name = (TextView) findViewById(R.id.nameTf);
        locationChanger = (Spinner) findViewById(R.id.location);
        locationChanger.setAdapter(list);
        setDefault(loc);
        photoUrl = (TextView) findViewById(R.id.imageUrlTf);
        saveChangesBtn = (Button) findViewById(R.id.saveBtn);

        //Sendbird method to connect user to service
        connect(email);

        //User method to display user info
        refresh();

        //Spinner listener for user selection
        locationChanger.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Sets location to Leinster
                if(position == 0){
                    loc = "Leinster";
                }
                //Sets location to Munster
                else if(position == 1){
                    loc = "Munster";
                }
                else if(position == 2){
                    loc = "Connacht";
                }
                else if(position == 3){
                    loc = "Ulster";
                }
                refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Button clicked listener
        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sendbird method to connect to service
                connect(email);
                //User method to update location
                updateNickname(email, loc);
                //Toast to display succedd
                Toast.makeText(ProfileActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //REUSED CODE
    public void changePick(View v)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECTED_PICTURE);
    }

    ////REUSED CODE
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case SELECTED_PICTURE:
                if(resultCode==RESULT_OK){
                    Uri uri = data.getData();
                    String[]  projection = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePath = cursor.getString(columnIndex);

                    cursor.close();

                    Bitmap img = BitmapFactory.decodeFile(filePath);
                    Drawable pic = new BitmapDrawable(img);

                    newPhoto = new File(filePath);
                    Uri yourUri = Uri.fromFile(newPhoto);

                    photoUrl.setText(yourUri.toString());

                    avi.setImageURI(Uri.parse(filePath));
                    avi.setImageDrawable(pic);
                    avi.setImageBitmap(img);
                }
        }
    }

    //Method to update location using Sendbird nickname feature
    public void updateNickname(String email, String nickname)
    {
        SendBird.updateCurrentUserInfo(nickname, nickname, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if(e!=null){
                    Toast.makeText(ProfileActivity.this, "Name not updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Method to update the user profile pic using sendbird
    public void updatePhotoUrl(String email, File photo)
    {
        SendBird.updateCurrentUserInfoWithProfileImage(email, photo, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if(e!=null){
                    Toast.makeText(ProfileActivity.this, "Picture not updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Method that calls Sendbird connect method
    public void connect(String email)
    {
        SendBird.connect(email, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    Toast.makeText(ProfileActivity.this, "Not connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Method to display user info
    public void refresh()
    {
        if(user != null)
        {
            name.setText(email);
            //uri = sendBirdUSer.getProfileUrl();

            url = Uri.parse("http://pngimg.com/uploads/face/face_PNG5646.png");

            photoUrl.setText(url.toString());

            Bitmap yourSelectedImage = BitmapFactory.decodeFile("http://pngimg.com/uploads/face/face_PNG5646.png");
            avi.setImageBitmap(yourSelectedImage);

            //avi.setImageURI(url);
        }
    }

    //Method to set spinner default element
    public void setDefault(String location)
    {
        if(location.equals("Leinter")){
            locationChanger.setSelection(0);
        }
        else if(location.equals("Munster")){
            locationChanger.setSelection(1);
        }
        else if(location.equals("Connacht")){
            locationChanger.setSelection(2);
        }
        else if(location.equals("Ulster")){
            locationChanger.setSelection(3);
        }
    }
}
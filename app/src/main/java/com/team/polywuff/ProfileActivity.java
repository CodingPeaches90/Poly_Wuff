package com.team.polywuff;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

    private Spinner locationChanger;

    private String [] province = {"Leinster", "Munster", "Connacht", "Ulster"};

    private User sendBirdUSer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        sendBirdUSer = SendBird.getCurrentUser();
        email = user.getEmail();

        ArrayAdapter<String> list = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_spinner_item, province);
        loc = sendBirdUSer.getNickname();

        avi = (ImageView) findViewById(R.id.profilePicture);

        name = (TextView) findViewById(R.id.nameTf);
        locationChanger = (Spinner) findViewById(R.id.location);
        locationChanger.setAdapter(list);
        setDefault(loc);
        photoUrl = (TextView) findViewById(R.id.imageUrlTf);

        saveChangesBtn = (Button) findViewById(R.id.saveBtn);

        connect(email);

        refresh();

        locationChanger.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0){
                    loc = "Leinster";
                }
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

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect(email);
                updateNickname(email, loc);
                Toast.makeText(ProfileActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

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
/**
 *
 *    ADDING PICTURE FILE FROM MEMORY
 *
     private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.imgPicker);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
 **/
}
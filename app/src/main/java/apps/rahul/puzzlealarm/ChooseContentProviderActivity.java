package apps.rahul.puzzlealarm;

import android.hardware.camera2.params.Face;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul on 7/23/2016.
 */
public class ChooseContentProviderActivity extends AppCompatActivity implements View.OnClickListener, Helper.PostHandleAsyncTaskGetBitmap{


    private IContentProvider chosenContentProvider;
    private Bitmap chosenBitmap;
    private List<IContentProvider> contentProviders;


    public void toExecuteDelegate(Bitmap bitmap)
    {

        Log.d(Application.TAG, "Obtained the bitmap in choose content activity");
        this.chosenBitmap = bitmap;
        ((ImageView)this.findViewById(R.id.photoProvided)).setImageBitmap(bitmap);
    }


    private void initializeContentProviderList()
    {
        contentProviders = new ArrayList<IContentProvider>()
        {{
            add(new FacebookContentProvider(ChooseContentProviderActivity.this, ChooseContentProviderActivity.this));
        }};
    }


    private IContentProvider getContentProviderBasedOnType(Class belongingClass)
    {
        for(IContentProvider provider : contentProviders)
        {
            if(provider.getClass().equals(belongingClass))
            {
                return provider;
            }
        }
        return null;
    }

    private void setUpUI()
    {
        for(IContentProvider provider: contentProviders)
        {
            if(provider instanceof  FacebookContentProvider)
            {
                    ((FacebookContentProvider) provider).setUpUI(this.findViewById(R.id.button_facebookLogin));
            }
        }
    }



    private LoginButton  faceBookLoginButton;
    private Button getBitmapButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.initializeContentProviderList();
        this.setUpUI();
        setContentView(R.layout.activity_choosecontentprovider);
        faceBookLoginButton = (LoginButton)this.findViewById(R.id.button_facebookLogin);
        faceBookLoginButton.setOnClickListener(this);
        getBitmapButton = (Button)this.findViewById(R.id.displayPhotoFromContentProvider);
        getBitmapButton.setOnClickListener(this);
        ((Button)this.findViewById(R.id.button_oneDriveLogin)).setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        Log.d(Application.TAG, "Clicked content provider");
        switch(v.getId())
        {
            case R.id.button_facebookLogin:
                chosenContentProvider = new FacebookContentProvider(ChooseContentProviderActivity.this,
                                ChooseContentProviderActivity.this);
                chosenContentProvider.setUpUI(this.findViewById(R.id.button_facebookLogin));
                Application.loggedInProviders.add(chosenContentProvider);
                break;
            case R.id.button_oneDriveLogin:
                Log.d(Application.TAG, "Content Provider is  OneDrive");
                chosenContentProvider = new OneDriveContentProvider(this);
                chosenContentProvider.setUpUI(this.findViewById(R.id.button_oneDriveLogin));
                Application.loggedInProviders.add(chosenContentProvider);
                break;
            case R.id.button_instagramLogin:
                Log.d(Application.TAG, "Instagram is the Content Provider");
                chosenContentProvider = new InstagramClient(this);

            case R.id.displayPhotoFromContentProvider:
                Log.d(Application.TAG, "Display facebook picture");
                chosenContentProvider.getBitmapAsync(this);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent  data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(Application.TAG, "OnaCTIVITYrEsult for chosencontentprovider");
        if(chosenContentProvider!=null)
            chosenContentProvider.onActivityResult(requestCode, resultCode, data);
    }
}

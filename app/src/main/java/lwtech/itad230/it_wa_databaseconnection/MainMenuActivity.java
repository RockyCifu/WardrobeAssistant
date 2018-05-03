package lwtech.itad230.it_wa_databaseconnection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonAdd, buttonBrowseWardrobe, buttonAssembleOutfit, buttonBrowseOutfit,buttonBrowseWishList;
    private Button buttonManageDonation, buttonManageTravel, buttonBrowseFriends;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        buttonAdd = (Button)findViewById(R.id.add);
        buttonBrowseWardrobe = (Button)findViewById(R.id.browseWardrobe);
        buttonAssembleOutfit = (Button)findViewById(R.id.assembleOutfit);
        buttonBrowseOutfit = (Button)findViewById(R.id.browseOutfit);
        buttonBrowseWishList = (Button)findViewById(R.id.browseWishList);
        buttonManageDonation = (Button)findViewById(R.id.ManageDonation);
        buttonManageTravel = (Button)findViewById(R.id.manageTravel);
        buttonBrowseFriends = (Button)findViewById(R.id.browseFriends);

        buttonAdd.setOnClickListener(this);
        buttonBrowseWardrobe.setOnClickListener(this);
        buttonAssembleOutfit.setOnClickListener(this);
        buttonBrowseOutfit.setOnClickListener(this);
        buttonBrowseWishList.setOnClickListener(this);
        buttonManageDonation.setOnClickListener(this);
        buttonManageTravel.setOnClickListener(this);
        buttonBrowseFriends.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view == buttonAdd)
        {
            startActivity(new Intent(getApplicationContext(), AddTags.class));
        }
        if(view == buttonBrowseWardrobe)
        {
        }
        if(view == buttonAssembleOutfit)
        {
        }
        if(view == buttonBrowseOutfit)
        {
        }
        if(view == buttonBrowseWishList)
        {
        }
        if(view == buttonManageDonation)
        {
        }
        if(view == buttonManageTravel)
        {
        }
        if(view == buttonBrowseFriends)
        {
        }
    }
}

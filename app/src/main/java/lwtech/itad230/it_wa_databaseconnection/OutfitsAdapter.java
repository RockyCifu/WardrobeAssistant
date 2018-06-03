package lwtech.itad230.it_wa_databaseconnection;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ashlyluse on 5/23/18.
 */

public class OutfitsAdapter extends RecyclerView.Adapter<OutfitsAdapter.OutfitsViewHolder>{

    private List<OutfitCards> outfitsList;
    private Context mContext;

    public OutfitsAdapter(Context context, List<OutfitCards> outfitsL){
        this.outfitsList = outfitsL;
        this.mContext = context;
    }

    @NonNull
    @Override
    public OutfitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_outfitcards, null);

        return new OutfitsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OutfitsViewHolder holder, int position) {

        OutfitCards OUTFIT = outfitsList.get(position);

        holder.textViewTitle.setText(OUTFIT.getTitle());
        holder.imageView.setImageDrawable(mContext.getResources().getDrawable(OUTFIT.getImage()));
    }

    @Override
    public int getItemCount() {
        return outfitsList.size();
    }



    public class OutfitsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageView;

        public OutfitsViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photoPreview);
            textViewTitle = itemView.findViewById(R.id.outfitTitle);
        }

    }

}

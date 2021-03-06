package lwtech.itad230.it_wa_databaseconnection;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * RecyclerView Adapter for Clothing items
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> imagePaths = new ArrayList<>();
    private Context mContext;

    /**
     *  RecyclerViewAdapter: Setting context and List we're working with
     * @param context
     *        imagePaths - array containing image paths
     */
    public RecyclerViewAdapter(Context context, ArrayList<String> imagePaths){
        this.imagePaths = imagePaths;
        this.mContext = context;
    }

    /**
     *  onCreateViewHolder: Inflating XML Layout and creating a new ViewHolder
     * @param parent - ViewGroup
     *        viewType - integer
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_images,parent,false);
        return new ViewHolder(view);
    }

    /**
     * onBindViewHolder: Binding the image to the view holder to display image
     * @param holder - ViewHolder
     *        position - position in a array containing image paths
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(mContext)
                .asBitmap()
                .load(imagePaths.get(position))
                .into(holder.image);
    }

    /**
     * getItemCount - get the size of the array containing image paths
     * @return size  - integer
     */
    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    /* Class: Object that contains and sets the image view */
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;

        /* Method: Setting XML Views */
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}

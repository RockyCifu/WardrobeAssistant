package lwtech.itad230.it_wa_databaseconnection;

/**
 * Created by ashlyluse on 5/23/18.
 * Class for individual Outfit Cards.
 * Holds info for every outfit, ID, Title, and Image
 */

public class OutfitCards {
    private int id;
    private String title;
    private int image;

    /* Setting variables */
    public OutfitCards(int id, String title, int image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    /* Method: getter for ID */
    public int getId() {
        return id;
    }

    /* Method: getter for Title */
    public String getTitle() {
        return title;
    }

    /* Method: getter for Image */
    public int getImage() {
        return image;
    }
}
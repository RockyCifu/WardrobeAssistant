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

    /**
     *  OutfitCards - Constructor Setting variables
     * @param id - integer
     *        title - String
     *        image - integer
     */
    public OutfitCards(int id, String title, int image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    /**
     *  getId: getter for ID
     * @return id - integer
     */
    public int getId() {
        return id;
    }

    /**
     *  getTitle: getter for Title
     * @return title - String
     */
    public String getTitle() {
        return title;
    }

    /**
     *  getImage: getter for Image
     * @return image - integer
     */
    public int getImage() {
        return image;
    }
}
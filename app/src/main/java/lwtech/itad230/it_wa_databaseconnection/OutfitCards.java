package lwtech.itad230.it_wa_databaseconnection;

/**
 * Created by ashlyluse on 5/23/18.
 */

public class OutfitCards {
    private int id;
    private String title;
    private int image;

    public OutfitCards(int id, String title, int image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
}
package dam.alumno.filmoteca;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.*;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pelicula {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final IntegerProperty year = new SimpleIntegerProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final FloatProperty rating = new SimpleFloatProperty();
    private final StringProperty poster = new SimpleStringProperty();
    private List<String> genre;

    public Pelicula() {}

    public Pelicula(int id, String title, int year, String description, float rating, String poster, List<String> genre) {
        this.id.set(id);
        this.title.set(title);
        this.year.set(year);
        this.description.set(description);
        this.rating.set(rating);
        this.poster.set(poster);
        this.genre = genre;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public int getYear() {
        return year.get();
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public String getDescription() {
        return description.get();
    }


    public void setDescription(String description) {
        this.description.set(description);
    }

    public float getRating() {
        return rating.get();
    }

    public FloatProperty ratingProperty() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating.set(rating);
    }

    public String getPoster() {
        return poster.get();
    }


    public void setPoster(String poster) {
        this.poster.set(poster);
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return title.get();
    }
}



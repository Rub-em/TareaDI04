package dam.alumno.filmoteca;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AddMovieController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField yearField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private Slider ratingSlider;
    @FXML
    private TextField posterField;
    @FXML
    private ImageView posterPreview;

    private Stage dialogStage;
    private Pelicula pelicula;
    private boolean confirmed = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMovie(Pelicula pelicula) {
        this.pelicula = pelicula;
        if (pelicula != null) {
            titleField.setText(pelicula.getTitle());
            yearField.setText(String.valueOf(pelicula.getYear()));
            descriptionField.setText(pelicula.getDescription());
            ratingSlider.setValue(pelicula.getRating());
            posterField.setText(pelicula.getPoster());
            posterPreview.setImage(new Image(pelicula.getPoster()));
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    @FXML
    private void onSave() {
        pelicula.setTitle(titleField.getText());
        pelicula.setYear(Integer.parseInt(yearField.getText()));
        pelicula.setDescription(descriptionField.getText());
        pelicula.setRating((float) ratingSlider.getValue());
        pelicula.setPoster(posterField.getText());
        confirmed = true;
        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }
}

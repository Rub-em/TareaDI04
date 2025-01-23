package dam.alumno.filmoteca;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;

public class HelloController {

    @FXML
    private TableView<Pelicula> moviesTable;
    @FXML
    private TableColumn<Pelicula, Integer> columnId;
    @FXML
    private TableColumn<Pelicula, String> columnTitle;
    @FXML
    private TableColumn<Pelicula, Integer> columnYear;
    @FXML
    private TableColumn<Pelicula, Float> columnRating;
    @FXML
    private TableColumn<Pelicula, String> columnGenre;
    @FXML
    private TextField searchField;
    @FXML
    private Label detailTitle;
    @FXML
    private Label detailYear;
    @FXML
    private Label detailDescription;
    @FXML
    private ImageView detailPoster;
    @FXML
    private VBox detailPane;

    @FXML
    private void initialize() {
        // Configurar columnas
        columnId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columnTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        columnYear.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());
        columnRating.setCellValueFactory(cellData -> cellData.getValue().ratingProperty().asObject());
        columnGenre.setCellValueFactory(cellData -> {
            String genres = String.join(", ", cellData.getValue().getGenre());
            return new ReadOnlyStringWrapper(genres);
        });

        // Cargar datos en la tabla
        ObservableList<Pelicula> peliculas = DatosFilmoteca.getInstancia().getListaPeliculas();
        moviesTable.setItems(peliculas);

        // Mostrar detalles de la primera película (si existe)
        if (!peliculas.isEmpty()) {
            moviesTable.getSelectionModel().select(0);
            mostrarDetalles(peliculas.get(0));
        }

        // Listener para mostrar detalles de la película seleccionada
        moviesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalles(newSelection);
            }
        });

        // Ajustar el tamaño del ImageView dinámicamente
        detailPoster.fitWidthProperty().bind(detailPane.widthProperty().multiply(0.8)); // 80% del ancho del panel
        detailPoster.fitHeightProperty().bind(detailPane.heightProperty().multiply(0.8)); // 80% del alto del panel
    }

    @FXML
    private void onAddMovie() {
        Pelicula nuevaPelicula = new Pelicula(); // Crear una película vacía
        boolean confirm = showEditDialog(nuevaPelicula);
        if (confirm) {
            // Asignar un ID único a la película
            int newId = DatosFilmoteca.getInstancia().getListaPeliculas().stream()
                    .mapToInt(Pelicula::getId)
                    .max()
                    .orElse(0) + 1;
            nuevaPelicula.setId(newId);

            // Agregar la nueva película a la lista
            DatosFilmoteca.getInstancia().getListaPeliculas().add(nuevaPelicula);
        }
    }


    @FXML
    private void onEditMovie() {
        Pelicula seleccionada = moviesTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            boolean confirm = showEditDialog(seleccionada);
            if (confirm) {
                moviesTable.refresh();
            }
        } else {
            showAlert("Selecciona una película para modificar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void onDeleteMovie() {
        Pelicula seleccionada = moviesTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas borrar esta película?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                DatosFilmoteca.getInstancia().getListaPeliculas().remove(seleccionada);
            }
        } else {
            showAlert("Selecciona una película para borrar.", Alert.AlertType.WARNING);
        }
    }



    @FXML
    private void onSearch() {
        String filter = searchField.getText().toLowerCase();
        ObservableList<Pelicula> filteredList = FXCollections.observableArrayList(
                DatosFilmoteca.getInstancia().getListaPeliculas().filtered(
                        pelicula -> pelicula.getTitle().toLowerCase().contains(filter)
                )
        );
        moviesTable.setItems(filteredList);

        // Mostrar detalles de la primera película filtrada (si existe)
        if (!filteredList.isEmpty()) {
            moviesTable.getSelectionModel().select(0);
            mostrarDetalles(filteredList.get(0));
        } else {
            limpiarDetalles();
        }
    }

    private void mostrarDetalles(Pelicula pelicula) {
        detailTitle.setText("Título: " + pelicula.getTitle());
        detailYear.setText("Año: " + pelicula.getYear());
        detailDescription.setText(pelicula.getDescription());
        detailPoster.setImage(new Image(pelicula.getPoster()));
    }

    private void limpiarDetalles() {
        detailTitle.setText("Título: ");
        detailYear.setText("Año: ");
        detailDescription.setText("");
        detailPoster.setImage(null);
    }

    private boolean showEditDialog(Pelicula pelicula) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Película");
        dialog.setHeaderText("Establece los detalles de la película");
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("watching-a-movie.png")));
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField titleField = new TextField(pelicula.getTitle());
        TextField yearField = new TextField(String.valueOf(pelicula.getYear()));
        TextField descriptionField = new TextField(pelicula.getDescription());
        TextField ratingField = new TextField(String.valueOf(pelicula.getRating()));
        TextField posterField = new TextField(pelicula.getPoster());
        TextField genreField = new TextField(
                pelicula.getGenre() != null ? String.join(", ", pelicula.getGenre()) : ""
        );

        dialogPane.setContent(new VBox(10,
                new Label("Título:"), titleField,
                new Label("Año:"), yearField,
                new Label("Descripción:"), descriptionField,
                new Label("Calificación (0-10):"), ratingField,
                new Label("URL del Póster:"), posterField,
                new Label("Géneros (separados por comas):"), genreField
        ));

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                pelicula.setTitle(titleField.getText());
                pelicula.setYear(Integer.parseInt(yearField.getText()));
                pelicula.setDescription(descriptionField.getText());
                pelicula.setRating(Float.parseFloat(ratingField.getText()));
                pelicula.setPoster(posterField.getText());
                pelicula.setGenre(
                        genreField.getText().isEmpty() ? List.of() : List.of(genreField.getText().split(",\\s*"))
                );
                return true;
            } catch (NumberFormatException e) {
                showAlert("Formato de entrada inválido. Por favor, revisa los campos.", Alert.AlertType.ERROR);
            }
        }
        return false;
    }



    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}



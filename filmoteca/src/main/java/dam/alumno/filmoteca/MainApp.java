package dam.alumno.filmoteca;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 900); // Establecer resolución 1280x720
        primaryStage.setTitle("Filmoteca");
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("watching-a-movie.png")));

        // Interceptar el cierre de la ventana
        primaryStage.setOnCloseRequest(event -> {
            // Consumir el evento de cierre
            event.consume();
            // Mostrar el diálogo de confirmación
            onCloseRequest(primaryStage);


    });
        primaryStage.setMinHeight(900);
        primaryStage.setMinWidth(1280);
        primaryStage.show();
    }

    @Override
    public void init() {
        DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Pelicula> lista = objectMapper.readValue(
                    new File("datos/peliculas.json"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Pelicula.class));
            datosFilmoteca.getListaPeliculas().setAll(lista);
        } catch (IOException e) {
            System.out.println("ERROR: No se pudo cargar el archivo JSON.");
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("datos/peliculas.json"), DatosFilmoteca.getInstancia().getListaPeliculas());
        } catch (IOException e) {
            System.out.println("ERROR: No se pudieron guardar los datos.");
            e.printStackTrace();
        }
    }
    // Método para manejar la confirmación de cierre
    private void onCloseRequest(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas cerrar la aplicación?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Confirmación de cierre");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            stage.close(); // Cerrar la ventana si el usuario confirma
        }
    }

    public static void main(String[] args) {
        launch();
    }
}




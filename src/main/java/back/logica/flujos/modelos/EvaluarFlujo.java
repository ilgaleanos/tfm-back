package back.logica.flujos.modelos;

import back.logica.io.modelos.EvaluarIn;
import back.utiles.SerializadorService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class EvaluarFlujo {

    private final SerializadorService serializadorService;


    @Autowired
    public EvaluarFlujo(SerializadorService serializadorService) {
        this.serializadorService = serializadorService;
    }


    /**
     * @param entrada: json con los parámetros requeridos para que funcionen correctamente los modelos
     * @return evaluación del modelo
     */
    public String evaluarRegresion(EvaluarIn entrada) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        MediaType JSON = MediaType.parse("application/json");

        byte[] bytes = this.serializadorService.serialize(entrada);
        RequestBody body = RequestBody.create(new String(bytes), JSON);

        Request request = new Request.Builder()
                .url("https://python-api-modelos-tgz3zht3yq-ue.a.run.app/modelos/regresion")
                .method("POST", body)
                .build();

        try (
                Response response = client.newCall(request).execute();
                ResponseBody respuesta = response.body()
        ) {
            assert respuesta != null;

            return respuesta.string();
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }


    /**
     * @param entrada: json con los parámetros requeridos para que funcionen correctamente los modelos
     * @return evaluación del modelo
     */
    public String evaluarRed(EvaluarIn entrada) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        MediaType JSON = MediaType.parse("application/json");

        byte[] bytes = this.serializadorService.serialize(entrada);
        RequestBody body = RequestBody.create(new String(bytes), JSON);

        Request request = new Request.Builder()
                .url("https://python-api-modelos-tgz3zht3yq-ue.a.run.app/modelos/red")
                .method("POST", body)
                .build();

        try (
                Response response = client.newCall(request).execute();
                ResponseBody respuestaMaestro = response.body()
        ) {
            assert respuestaMaestro != null;

            return respuestaMaestro.string();
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }
}

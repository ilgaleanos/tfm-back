package back.utiles;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Captcha {

    private final static Logger logger = LoggerFactory.getLogger(Captcha.class);

    public boolean verificarInvalido(String token, double umbral) {
        if (token.equals(Env.AUTH_RECAPTCHA_FAKE)) {
            return false;
        }

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create("", JSON);

        Request request = new Request.Builder()
                .url("https://www.google.com/recaptcha/api/siteverify?secret=" + Env.AUTH_RECAPTCHA_CLAVE_SECRETA + "&response=" + token)
                .method("POST", body)
                .build();

        try (
                Response response = client.newCall(request).execute();
                ResponseBody respuestaMaestro = response.body()
        ) {
            assert respuestaMaestro != null;

            String respuesta = respuestaMaestro.string();
            JsonObject respuestaJson = JsonParser.parseString(respuesta).getAsJsonObject();
            logger.info(respuestaJson.toString());
            return !respuestaJson.get("success").getAsBoolean() || !(respuestaJson.get("score").getAsDouble() >= Math.max(0.7, umbral));
        } catch (Exception err) {
            logger.error(err.getMessage() + " -> " + token);
            return true;
        }
    }
}

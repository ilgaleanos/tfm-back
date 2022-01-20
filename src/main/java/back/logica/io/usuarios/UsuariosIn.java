package back.logica.io.usuarios;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UsuariosIn {

    @JsonAttribute(ignore = true)
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /*==================================================================================================================
     * POST
     *================================================================================================================*/

    @CompiledJson
    public static class POST {

        private String correo;
        private String nombre;
        private String apellido;

        // requerido para serialización
        public POST() {
        }

        public boolean esInValido() {
            if (
                    correo == null || correo.isEmpty()
                            || nombre == null || nombre.isEmpty()
                            || apellido == null || apellido.isEmpty()
            ) {
                return true;
            }

            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(correo);
            return !matcher.find();
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            Objects.requireNonNull(correo);
            this.correo = correo.toLowerCase();
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }
    }


    /*==================================================================================================================
     * PUT
     *================================================================================================================*/

    @CompiledJson
    public static class PUT {

        private String correo;
        private String nombre;
        private String apellido;
        private int id;

        // requerido para serialización
        public PUT() {
        }

        public boolean esInValido() {
            if (
                    correo == null || correo.isEmpty()
                            || nombre == null || nombre.isEmpty()
                            || apellido == null || apellido.isEmpty()
                            || id == 0
            ) {
                return true;
            }

            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(correo);
            return !matcher.find();
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            Objects.requireNonNull(correo);
            this.correo = correo.toLowerCase();
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

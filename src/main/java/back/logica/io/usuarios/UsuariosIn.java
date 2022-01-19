package back.logica.io.usuarios;

import com.dslplatform.json.CompiledJson;
import org.springframework.stereotype.Component;

@Component
public class UsuariosIn {

    /*==================================================================================================================
     * POST
     *================================================================================================================*/

    @CompiledJson
    public static class POST {

        private String correo;
        private String nombre;
        private String apellido;
        private String telefono;
        private String documento;
        private int documento_tipo;

        // requerido para serialización
        public POST() {
        }

        public boolean esInValido() {
            return correo == null || correo.isEmpty()
                    || nombre == null || nombre.isEmpty()
                    || apellido == null || apellido.isEmpty()
                    || telefono == null || telefono.isEmpty()
                    || documento == null || documento.isEmpty()
                    || documento_tipo == 0;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
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

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getDocumento() {
            return documento;
        }

        public void setDocumento(String documento) {
            this.documento = documento;
        }

        public int getDocumento_tipo() {
            return documento_tipo;
        }

        public void setDocumento_tipo(int documento_tipo) {
            this.documento_tipo = documento_tipo;
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
        private String telefono;
        private String documento;
        private int documento_tipo;
        private int id;

        // requerido para serialización
        public PUT() {
        }

        public boolean esInValido() {
            return correo == null || correo.isEmpty()
                    || nombre == null || nombre.isEmpty()
                    || apellido == null || apellido.isEmpty()
                    || telefono == null || telefono.isEmpty()
                    || documento == null || documento.isEmpty()
                    || documento_tipo == 0 || id == 0;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
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

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getDocumento() {
            return documento;
        }

        public void setDocumento(String documento) {
            this.documento = documento;
        }

        public int getDocumento_tipo() {
            return documento_tipo;
        }

        public void setDocumento_tipo(int documento_tipo) {
            this.documento_tipo = documento_tipo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

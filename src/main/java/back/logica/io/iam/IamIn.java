package back.logica.io.iam;

import com.dslplatform.json.CompiledJson;
import org.springframework.stereotype.Component;

@Component
public class IamIn {


    /*==================================================================================================================
     * Generar OTP
     *================================================================================================================*/

    @CompiledJson
    public static class Sesion {

        private int plataforma_id;

        // requerido para serialización
        public Sesion() {
        }

        public boolean esInValido() {
            return plataforma_id == 0;
        }

        public int getPlataforma_id() {
            return plataforma_id;
        }

        public void setPlataforma_id(int plataforma_id) {
            this.plataforma_id = plataforma_id;
        }
    }


    /*==================================================================================================================
     * POST Iam
     *================================================================================================================*/

    @CompiledJson
    public static class POST {

        private int usuario_id;
        private int plataforma_id;

        // requerido para serialización
        public POST() {
        }

        public boolean esInValido() {
            return usuario_id == 0 || plataforma_id == 0;
        }

        public int getUsuario_id() {
            return usuario_id;
        }

        public void setUsuario_id(int usuario_id) {
            this.usuario_id = usuario_id;
        }

        public int getPlataforma_id() {
            return plataforma_id;
        }

        public void setPlataforma_id(int plataforma_id) {
            this.plataforma_id = plataforma_id;
        }
    }


}

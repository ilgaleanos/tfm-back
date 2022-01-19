package back.logica.io.usuarios;

import com.dslplatform.json.CompiledJson;
import org.springframework.stereotype.Component;

@Component
public class PermisosIn {

    /*==================================================================================================================
     * POST
     *================================================================================================================*/

    @CompiledJson
    public static class POST {

        private int usuario_id;
        private int permiso_id;
        private int alcance;

        // requerido para serialización
        public POST() {
        }

        public boolean esInValido() {
            return usuario_id == 0 || permiso_id == 0 || alcance == 0;
        }

        public int getUsuario_id() {
            return usuario_id;
        }

        public void setUsuario_id(int usuario_id) {
            this.usuario_id = usuario_id;
        }

        public int getPermiso_id() {
            return permiso_id;
        }

        public void setPermiso_id(int permiso_id) {
            this.permiso_id = permiso_id;
        }

        public int getAlcance() {
            return alcance;
        }

        public void setAlcance(int alcance) {
            this.alcance = alcance;
        }
    }
}

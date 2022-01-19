package back.logica.flujos.usuarios;

import back.logica.basededatos.usuarios.PermisosDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class PermisosFlujo {

    private final static Logger logger = LoggerFactory.getLogger(UsuariosFlujo.class);
    private final PermisosDB permisosDB;

    @Autowired
    public PermisosFlujo(PermisosDB permisosDB) {
        this.permisosDB = permisosDB;
    }


    public ArrayList<HashMap<String, Object>> obtenerPermisos() throws SQLException {
        return permisosDB.obtenerPermisos();
    }

    public int asignar(int usuario_id, int permiso_id, int alcance) throws SQLException {
        return permisosDB.asignar(usuario_id, permiso_id, alcance);
    }

    public int eliminar(int usuario_id, int permiso_id) throws SQLException {
        return permisosDB.eliminar(usuario_id, permiso_id);
    }
}

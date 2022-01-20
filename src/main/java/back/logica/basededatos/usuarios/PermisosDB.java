package back.logica.basededatos.usuarios;

import back.utiles.DBCoreService;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static back.logica.basededatos.usuarios.Consultas.*;

@Component
public class PermisosDB extends DBCoreService {

    public ArrayList<HashMap<String, Object>> obtenerPermisos() throws SQLException {
        return obtenerElementos(SP_PERMISOS_SELECT, new Object[]{});
    }

    public ArrayList<HashMap<String, Object>> obtenerPermisosUsuario(int id) throws SQLException {
        Object[] parametros = new Object[]{id};
        return obtenerElementos(SP_PERMISOS_SELECT_USUARIO, parametros);
    }

    public int asignar(int usuario_id, int permiso_id, int alcance, String uuid) throws SQLException {
        Object[] parametros = new Object[]{usuario_id, permiso_id, alcance, uuid};
        return ejecutarQuery(SP_PERMISOS_UPSERT, parametros);
    }

    public int eliminar(int usuario_id, int permiso_id) throws SQLException {
        Object[] parametros = new Object[]{usuario_id, permiso_id};
        return ejecutarQuery(SP_PERMISOS_DELETE, parametros);
    }
}

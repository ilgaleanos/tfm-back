package back.logica.basededatos.usuarios;

import back.utiles.DBCoreService;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static back.logica.basededatos.usuarios.Consultas.*;

@Component
public class UsuariosDB extends DBCoreService {

    public HashMap<String, Object> obtenerUsuario(int id, String uuid) throws SQLException {
        Object[] parametros = new Object[]{id, uuid};
        return obtenerElemento(SP_USUARIOS_SELECT_ONE, parametros);
    }

    public ArrayList<HashMap<String, Object>> obtenerUsuarios(String uuid) throws SQLException {
        Object[] parametros = new Object[]{uuid};
        return obtenerElementos(SP_USUARIOS_SELECT, parametros);
    }

    public int crear(String correo, String nombre, String apellido, String uuid) throws SQLException {
        Object[] parametros = new Object[]{correo, nombre, apellido, uuid};
        return ejecutarQuery(SP_USUARIOS_INSERT, parametros);
    }

    public int actualizar(String correo, String nombre, String apellido, String uuid, int id) throws SQLException {
        Object[] parametros = new Object[]{correo, nombre, apellido, uuid, id};
        return ejecutarQuery(SP_USUARIOS_UPDATE, parametros);
    }

    public int eliminar(int id, String uuid) throws SQLException {
        Object[] parametros = new Object[]{id, uuid};
        return ejecutarQuery(SP_USUARIOS_DELETE, parametros);
    }

}

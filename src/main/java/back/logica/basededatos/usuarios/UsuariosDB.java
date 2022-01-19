package back.logica.basededatos.usuarios;

import back.utiles.DBCoreService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static back.logica.basededatos.usuarios.Consultas.*;


public class UsuariosDB extends DBCoreService {

    public HashMap<String, Object> obtenerUsuario(int id) throws SQLException {
        Object[] parametros = new Object[]{id};
        return obtenerElemento(SP_USUARIOS_SELECT_ONE, parametros);
    }

    public ArrayList<HashMap<String, Object>> obtenerUsuarios() throws SQLException {
        return obtenerElementos(SP_USUARIOS_SELECT, new Object[]{});
    }

    public int crear(String correo, String nombre, String apellido, String telefono, int documento_tipo, String documento) throws SQLException {
        Object[] parametros = new Object[]{correo, nombre, apellido, telefono, documento_tipo, documento};
        return ejecutarQuery(SP_USUARIOS_INSERT, parametros);
    }

    public int actualizar(String correo, String nombre, String apellido, String telefono, int documento_tipo, String documento, int id) throws SQLException {
        Object[] parametros = new Object[]{correo, nombre, apellido, telefono, documento_tipo, documento, id};
        return ejecutarQuery(SP_USUARIOS_UPDATE, parametros);
    }

    public int eliminar(int id, String uuid) throws SQLException {
        Object[] parametros = new Object[]{id, uuid};
        return ejecutarQuery(SP_USUARIOS_DELETE, parametros);
    }

}

package back.logica.flujos.usuarios;

import back.logica.basededatos.usuarios.UsuariosDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class UsuariosFlujo {

    private final static Logger logger = LoggerFactory.getLogger(UsuariosFlujo.class);
    private final UsuariosDB usuariosDB;

    @Autowired
    public UsuariosFlujo(UsuariosDB usuariosDB) {
        this.usuariosDB = usuariosDB;
    }


    public HashMap<String, Object> obtenerUsuario(int id) throws SQLException {
        return usuariosDB.obtenerUsuario(id);
    }

    public ArrayList<HashMap<String, Object>> obtenerUsuarios() throws SQLException {
        return usuariosDB.obtenerUsuarios();
    }

    public int crear(String correo, String nombre, String apellido, String telefono, int documento_tipo, String documento) throws SQLException {
        return usuariosDB.crear(correo, nombre, apellido, telefono, documento_tipo, documento);
    }

    public int actualizar(String correo, String nombre, String apellido, String telefono, int documento_tipo, String documento, int id) throws SQLException {
        return usuariosDB.actualizar(correo, nombre, apellido, telefono, documento_tipo, documento, id);
    }

    public int eliminar(int id, String uuid) throws SQLException {
        return usuariosDB.eliminar(id, uuid);
    }
}

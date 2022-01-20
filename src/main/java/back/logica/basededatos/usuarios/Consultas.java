package back.logica.basededatos.usuarios;

public class Consultas {


    /*==================================================================================================================
     *   MySQL
     *================================================================================================================*/


    // usuarios
    public static final String SP_USUARIOS_SELECT_ONE = "CALL sp_usuarios_select_one(?, ?)";
    public static final String SP_USUARIOS_SELECT = "CALL sp_usuarios_select(?)";
    public static final String SP_USUARIOS_INSERT = "CALL sp_usuarios_insert(?, ?, ?, ?)";
    public static final String SP_USUARIOS_UPDATE = "CALL sp_usuarios_update(?, ?, ?, ?, ?)";
    public static final String SP_USUARIOS_DELETE = "CALL sp_usuarios_delete(?, ?)";

    // permisos
    public static final String SP_PERMISOS_SELECT = "CALL sp_permisos_select()";
    public static final String SP_PERMISOS_SELECT_USUARIO = "CALL sp_permisos_select_usuario(?)";
    public static final String SP_PERMISOS_UPSERT = "CALL sp_permisos_upsert(?, ?, ?, ?)";
    public static final String SP_PERMISOS_DELETE = "CALL sp_permisos_delete(?, ?)";
}

package back.logica.basededatos.seguridad;

public class Consultas {


    /*==================================================================================================================
     *   MySQL
     *================================================================================================================*/


    // CREDENCIALES
    public static final String SP_USUARIOS_INSERT_OTP = "call autenticacion.sp_usuarios_insert_otp(?, ?, ?, ?)";
    public static final String SP_USUARIOS_SELECT_X_UUID_OTP = "call autenticacion.sp_usuarios_select_x_uuid_otp(?, ?)";
    public static final String SP_TOKEN_INSERT = "call autenticacion.sp_token_insert(?, ?, ?)";
    public static final String SP_TOKEN_EXIST = "call autenticacion.sp_token_exist(?, ?, ?)";
    public static final String SP_TOKEN_DELETE = "call autenticacion.sp_token_delete(?)";
}

package back.logica.basededatos.iam;

public class Consultas {


    /*==================================================================================================================
     *   MySQL
     *================================================================================================================*/


    // IAM
    public static final String SP_PERMISOS_SELECT_PROPIOS = "CALL autenticacion.sp_permisos_select_propios(?, ?)";
    public static final String SP_PERMISOS_SELECT = "CALL autenticacion.sp_permisos_select(?, ?)";

}

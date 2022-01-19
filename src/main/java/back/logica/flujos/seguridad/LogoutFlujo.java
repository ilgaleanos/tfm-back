package back.logica.flujos.seguridad;

import back.logica.basededatos.seguridad.LogoutDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;


@Component
public class LogoutFlujo {

    private final static Logger logger = LoggerFactory.getLogger(LogoutFlujo.class);
    private final LogoutDB logoutDB;

    @Autowired
    public LogoutFlujo(
            LogoutDB logoutDB
    ) {
        this.logoutDB = logoutDB;
    }


    public int eliminar(String uuid) throws SQLException {
        return logoutDB.eliminar(uuid);
    }
}

package back.controladores;

import back.utiles.FrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Clase de presentación del api
 */
@RestController
public class IndexController {

    private final FrameworkService fw;


    @Autowired
    public IndexController(FrameworkService fw) {
        this.fw = fw;
    }


    @RequestMapping(
            value = "/",
            method = RequestMethod.GET
    )
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap<String, Object> response = new HashMap<>(1);
        response.put("alive", 1);
        fw.sendJSON(resp, response);
    }
}

package back.controladores.seguridad;

import back.utiles.Captcha;
import back.utiles.Env;
import back.utiles.FrameworkService;
import io.undertow.util.StatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RestController
public class ReCaptchaCtl {

    private final Captcha captchaService;
    private final FrameworkService fw;

    @Autowired
    public ReCaptchaCtl(FrameworkService fw, Captcha captchaService) {
        this.fw = fw;
        this.captchaService = captchaService;
    }

    @CrossOrigin
    @RequestMapping(
            value = "/v1/recaptcha",
            method = RequestMethod.GET
    )
    public void doget(HttpServletRequest req, HttpServletResponse resp,
                      @RequestParam String captcha,
                      @RequestParam double umbral
    ) throws IOException {
        HashMap<String, Object> response = new HashMap<>(1);
        try {
            response.put("valido", !captchaService.verificarInvalido(captcha, umbral));
        } catch (Exception throwables) {
            throwables.printStackTrace();
            resp.setStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            if (Env.AUTH_ENVIRONMENT.equals("DEBUG")) response.put("error", throwables.getMessage());
        }

        fw.sendJSON(resp, response);
    }
}

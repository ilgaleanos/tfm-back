/**
 * Copyright 2022 Leito. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package back.utiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

@Service
public class CorreoSMTPService {

    private final static Logger logger = LoggerFactory.getLogger(CorreoSMTPService.class);
    private final Properties props;


    public CorreoSMTPService() {
        this.props = new Properties();
        props.put("mail.smtp.host", "smtp.mailgun.org");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");
    }


    public boolean enviarEmailNoreply(String asunto, String cuerpo, Address[] to, Address[] bcc) {
        Session session = Session.getDefaultInstance(this.props);
        MimeMessage msg = new MimeMessage(session);

        try {
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(Env.AUTH_SMTP_USUARIO, "Sun Eyes"));
            msg.setSubject(asunto, "UTF-8");
            msg.setContent(cuerpo, "text/html; charset=UTF-8");
            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, to);
            msg.addRecipients(Message.RecipientType.BCC, bcc);

        } catch (MessagingException | UnsupportedEncodingException err) {
            err.printStackTrace();
            logger.error(err.getMessage());
            return false;
        }

        try {
            Transport transport = session.getTransport();
            transport.connect(Env.AUTH_SMTP_USUARIO, Env.AUTH_SMTP_CLAVE);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch (MessagingException err) {
            err.printStackTrace();
            logger.error(err.getMessage());
            return false;
        }

        logger.info("correo enviado correctamente: " + asunto);
        return true;
    }
}

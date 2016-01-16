package com.github.butterbrother.ews.redirector.service;

import com.github.butterbrother.ews.redirector.graphics.TrayControl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;

import javax.swing.*;
import java.awt.*;

/**
 * Непосредственно управляет потоками-обработчиками.
 * Устанавливает соединение с EWS
 */
public class ServiceController {
    public ServiceController(
            String domain,
            String email,
            String login,
            String password,
            String url,
            boolean enableAuto,
            TrayControl.TrayPopup popup,
            JTextField urlField,
            JButton startStopButton
    ) {
        try {
            ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
            ExchangeCredentials credentials;
            if (domain.isEmpty()) {
                credentials = new WebCredentials(login, password);
            } else {
                credentials = new WebCredentials(login, password, domain);
            }
            if (enableAuto) {
                service.autodiscoverUrl(email, new RedirectionUrlCallback());
                urlField.setText(service.getUrl().toString());
            }
        } catch (Exception startException) {
            popup.show("Connection error", startException.getMessage(), TrayIcon.MessageType.ERROR);
        }
    }
}

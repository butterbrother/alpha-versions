package com.github.butterbrother.ews.redirector.service;

import com.github.butterbrother.ews.redirector.graphics.TrayControl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;

import javax.swing.*;
import java.net.URI;

/**
 * Непосредственно управляет потоками-обработчиками.
 * Устанавливает соединение с EWS
 */
public class ServiceController extends SafeStopService {
    private ExchangeService service = null;
    private PullEventsService pullEvents = null;
    private NewMessagesSearchService newMessages = null;
    private SendService send = null;

    private JButton startStopButton;
    private JButton applyButton;
    private String recipient;
    private boolean deleteRedirected;
    private TrayControl.TrayPopup popup;

    public ServiceController(
            String domain,
            String email,
            String recipient,
            String login,
            String password,
            String url,
            boolean enableAuto,
            TrayControl.TrayPopup popup,
            JTextField urlField,
            JButton startStopButton,
            JButton applyButton,
            boolean deleteRedirected
    ) {
        this.startStopButton = startStopButton;
        this.applyButton = applyButton;
        this.recipient = recipient;
        this.deleteRedirected = deleteRedirected;
        this.popup = popup;

        try {
            // TODO: переделать. ExchangeService однопоточный и однопроцессный (в одном окне). Для каждого сервиса - свой service и рассылка
            // + синхронизация по id, что бы одновременно не обрабатывали один и тот же объект
            service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
            ExchangeCredentials credentials;
            if (domain.isEmpty()) {
                credentials = new WebCredentials(login, password);
            } else {
                credentials = new WebCredentials(login, password, domain);
            }
            service.setCredentials(credentials);
            if (enableAuto) {
                service.autodiscoverUrl(email, new RedirectionUrlCallback());
                urlField.setText(service.getUrl().toString());
            } else {
                service.setUrl(new URI(url));
            }

            new Thread(this).start();
        } catch (Exception startException) {
            popup.error("Connection error", startException.getMessage());
            safeStop();
        }
    }

    /**
     * Запуск обработчиков и ожидание
     */
    @Override
    public void run() {
        if (service != null) {
            send = new SendService(deleteRedirected, recipient, popup);
            //pullEvents = new PullEventsService(service, send.getQueue(), popup);
            newMessages = new NewMessagesSearchService(service, popup, send.getQueue());
        }

        while (super.isActive()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ie) {
                safeStop();
            }
        }
        super.wellDone();
    }

    /**
     * Плавная остановка с ожиданием завершения работы
     * всех сервисов.
     */
    @Override
    public synchronized void safeStop() {
        super.safeStop();

        startStopButton.setEnabled(false);
        applyButton.setEnabled(false);

        if (pullEvents != null) {
            pullEvents.safeStop();
            while (! pullEvents.isDone()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignore) {}
            }
        }

        if (newMessages != null) {
            newMessages.safeStop();
            while (! newMessages.isDone()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignore) {}
            }
        }

        if (send != null) {
            send.safeStop();
            while (! send.isDone()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignore) {}
            }
        }

        startStopButton.setText("Start");
        startStopButton.setEnabled(true);
        applyButton.setEnabled(true);
    }
}

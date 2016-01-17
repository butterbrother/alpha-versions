package com.github.butterbrother.ews.redirector.service;

import com.github.butterbrother.ews.redirector.graphics.TrayControl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.notification.GetEventsResults;
import microsoft.exchange.webservices.data.notification.ItemEvent;
import microsoft.exchange.webservices.data.notification.PullSubscription;
import microsoft.exchange.webservices.data.property.complex.FolderId;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Сервис обработки входящих сообщений.
 * Выполняет поиск новых сообщений по событиям, полученным
 * от Exchange-сервера.
 * Соответственно обрабатывает только те письма, события о
 * прибытии которых появились уже после запуска сервиса.
 * Предпочтительно, что бы запускался первым
 */
public class PullEventsService extends SafeStopService {
    private ExchangeService service;
    private TrayControl.TrayPopup popup;
    private ConcurrentSkipListSet<EmailMessage> messages;

    /**
     * Инициализация
     * @param service   Exchange
     * @param messages  очередь сообщений
     * @param popup     трей для передачи аварийных сообщений
     */
    public PullEventsService(ExchangeService service,
                             ConcurrentSkipListSet<EmailMessage> messages,
                             TrayControl.TrayPopup popup) {
        super();
        this.service = service;
        this.messages = messages;
        this.popup = popup;

        new Thread(this).start();
    }

    public void run() {
        List<FolderId> folders = new ArrayList<>();
        folders.add(new FolderId(WellKnownFolderName.Inbox));
        GetEventsResults eventsResults;

        long extIteration = 0;
        long intIteration = 0;
        while (super.isActive()) {
            ++extIteration;
            try {
                PullSubscription ps = service.subscribeToPullNotifications(folders, 5, null, EventType.NewMail);
                intIteration = 0;
                while (super.isActive()) {
                    ++intIteration;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ie) {
                        super.safeStop();
                    }
                    eventsResults = ps.getEvents();
                    for (ItemEvent event : eventsResults.getItemEvents()) {
                        if (! super.isActive()) break;
                        messages.add(EmailMessage.bind(service, event.getItemId()));
                    }
                }
            } catch (Exception e) {
                popup.error("Exchange error (Notify reader module)", e.getMessage());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    super.safeStop();
                }
            }
        }

        super.wellDone();
    }
}

package com.github.butterbrother.ews.redirector.service;

import com.github.butterbrother.ews.redirector.graphics.TrayControl;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.FolderId;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Осуществляет отправку переадресованных сообщений.
 * Опционально - удаляет переадресованные сообщения.
 * Если не установлено правило удаления - помечает
 * сообщение прочитанным.
 * Для этого периодически считывает очередь, содержащую
 * id данных сообщений. Очередь создаётся этим классом.
 */
public class SendService extends SafeStopService {
    private ConcurrentSkipListSet<EmailMessage> messages = new ConcurrentSkipListSet<>();
    private boolean deleteRedirected;
    private EmailAddress recipientEmail;
    private TrayControl.TrayPopup popup;

    /**
     * Инициализация
     * @param deleteRedirected  удалять перенаправленные
     * @param recipientEmail    e-mail получателя
     * @param popup             трей для передачи аварийных сообщений
     */
    public SendService(boolean deleteRedirected, String recipientEmail, TrayControl.TrayPopup popup) {
        super();
        this.deleteRedirected = deleteRedirected;
        this.recipientEmail = new EmailAddress(recipientEmail);
        this.popup = popup;

        new Thread(this).start();
    }

    /**
     * Получение очереди сообщений на перенаправление.
     * @return  очередь обработки
     */
    public ConcurrentSkipListSet<EmailMessage> getQueue() {
        return this.messages;
    }

    /**
     * Поток перенаправления сообщений
     */
    public void run() {
        FolderId deletedItems = new FolderId(WellKnownFolderName.DeletedItems);
        while (super.isActive()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                super.safeStop();
            }
            for (EmailMessage message : messages) {
                // TODO: переделать, нужно удалять из очереди
                if (! super.isActive()) break;
                try {
                    // Обрабатываем только непрочитанные из входящих
                    if ((! message.getParentFolderId().equals(deletedItems)) && (! message.getIsRead())) {
                        try {
                            message.forward(null, recipientEmail);
                            if (deleteRedirected)
                                message.move(deletedItems);
                            else
                                message.setIsRead(true);
                        } catch (Exception forwardError) {
                            popup.error("Email forwarding error", forwardError.getMessage());
                        }
                    }
                } catch (ServiceLocalException e) {
                    popup.error("Exchange error", e.getMessage());
                }
            }
        }

        super.wellDone();
    }
}

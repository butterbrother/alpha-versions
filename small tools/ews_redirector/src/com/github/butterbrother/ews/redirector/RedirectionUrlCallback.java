package com.github.butterbrother.ews.redirector;

import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.autodiscover.exception.AutodiscoverLocalException;

/**
 * <EMPTY HEADER, PLEASE EDIT>
 * Created by user on 16.01.16.
 */
public class RedirectionUrlCallback implements IAutodiscoverRedirectionUrl {
    @Override
    public boolean autodiscoverRedirectionUrlValidationCallback(String s) throws AutodiscoverLocalException {
        return s.toLowerCase().startsWith("https://");
    }
}

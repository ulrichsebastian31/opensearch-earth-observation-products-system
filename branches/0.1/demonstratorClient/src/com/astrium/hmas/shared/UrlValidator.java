package com.astrium.hmas.shared;

import com.google.gwt.regexp.shared.RegExp;

public class UrlValidator {
	
	private RegExp urlValidator;
	private RegExp urlPlusTldValidator;
	public boolean isValidUrl(String url, boolean topLevelDomainRequired) {
	    if (urlValidator == null || urlPlusTldValidator == null) {
	        urlValidator = RegExp.compile("^((ftp|http|https)://[\\w@.\\-\\_]+(:\\d{1,5})?(/[\\w#!:.?+=&%@!\\_\\-/]+)*){1}$");
	        urlPlusTldValidator = RegExp.compile("^((ftp|http|https)://[\\w@.\\-\\_]+\\.[a-zA-Z]{2,}(:\\d{1,5})?(/[\\w#!:.?+=&%@!\\_\\-/]+)*){1}$");
	    }
	    return (topLevelDomainRequired ? urlPlusTldValidator : urlValidator).exec(url) != null;

	}

}

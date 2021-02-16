package com.derin.damiano.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

public class ServletHandler {

	public static String getParameter(HttpServletRequest request, String paramName) {
		String parameter = StringEscapeUtils.escapeJava(request.getParameter(paramName));
		if (parameter == null || parameter.isEmpty()) {
			return null;
		} else {
			return parameter;
		}
	}

}

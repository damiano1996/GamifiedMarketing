package com.derin.damiano.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.derin.damiano.entities.User;
import com.derin.damiano.exceptions.UsernameInUseException;
import com.derin.damiano.services.UserService;
import com.derin.damiano.utils.ServletHandler;

@WebServlet("/signup")
public class SignUpController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "com.derin.damiano.services/UserService")
	private UserService usrService;

	public SignUpController() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

		String username = ServletHandler.getParameter(request, "username");
		String email = ServletHandler.getParameter(request, "email");
		String password = ServletHandler.getParameter(request, "password");
		String name = ServletHandler.getParameter(request, "name");
		String surname = ServletHandler.getParameter(request, "surname");

		String path = null;
		if (username == null || email == null || password == null || name == null || surname == null) {
//			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or incorrect values.");
//			return;

			ctx.setVariable("message", "Missing or incorrect values.");
			path = "/WEB-INF/message.html";
		} else {
			try {
				usrService.addUser(username, email, password, name, surname);
				ctx.setVariable("message", "Registration completed successfully!");
				path = "/WEB-INF/message.html";

			} catch (UsernameInUseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ctx.setVariable("message", "Username already in use.");

				path = "/WEB-INF/message.html";
			}

		}

		templateEngine.process(path, ctx, response.getWriter());

	}

	public void destroy() {
	}
}
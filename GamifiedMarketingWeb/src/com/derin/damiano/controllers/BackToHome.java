package com.derin.damiano.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.derin.damiano.entities.Answer;
import com.derin.damiano.entities.Product;
import com.derin.damiano.entities.Question;
import com.derin.damiano.entities.User;
import com.derin.damiano.services.ProductService;
import com.derin.damiano.services.QuestionnaireService;
import com.derin.damiano.services.UserService;
import com.derin.damiano.utils.ImageUtils;
import com.derin.damiano.utils.ServletHandler;

/**
 * Servlet implementation class CreationController
 */
@WebServlet("/backtohome")
@MultipartConfig
public class BackToHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	@EJB(name = "com.derin.damiano.services/UserService")
	private UserService userService;

	@EJB(name = "com.derin.damiano.services/ProductService")
	private ProductService productService;

	@EJB(name = "com.derin.damiano.services/QuestionnaireService")
	private QuestionnaireService questionnaireService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BackToHome() {
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		templateEngine.process("/WEB-INF/home.html", ctx, response.getWriter());

	}

	public void destroy() {
	}

}

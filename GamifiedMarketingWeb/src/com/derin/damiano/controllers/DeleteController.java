package com.derin.damiano.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.derin.damiano.entities.Statisticaldata;
import com.derin.damiano.entities.User;
import com.derin.damiano.services.ProductService;
import com.derin.damiano.services.QuestionnaireService;
import com.derin.damiano.services.UserService;
import com.derin.damiano.utils.ImageUtils;
import com.derin.damiano.utils.ServletHandler;

/**
 * Servlet implementation class CreationController
 */
@WebServlet("/deletion")
@MultipartConfig
public class DeleteController extends HttpServlet {
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
	public DeleteController() {
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

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

		String path = null;

		if (user.isAdmin()) {

			session.setAttribute("availableDatesByString",
					questionnaireService.getAvailableDatesByString(CreationController.DATE_FORMAT));
			path = "/WEB-INF/deletion.html";

		} else {

			ctx.setVariable("message", "Only administrators can perform this action!");
			path = "/WEB-INF/message.html";
		}

		templateEngine.process(path, ctx, response.getWriter());

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

		String path;
		if (user.isAdmin()) {

			String operation = request.getParameter("submit");
//			System.out.println("operation: " + operation);

			if (operation.contains("Questionnaire")) {

				String requestedDate = operation.replace("Questionnaire: ", "");
				session.setAttribute("requestedDate", requestedDate);
//				System.out.println("Requested date: " + requestedDate);

				HashMap<String, Date> availableDatesByString = (HashMap<String, Date>) session
						.getAttribute("availableDatesByString");

				Date questionnaireDateToDelete = availableDatesByString.get(requestedDate);

				// Deleting product and cascading all questions/answers/etc. that are related.
				productService.deleteProduct(questionnaireDateToDelete);

				// updating the available dates.
				session.setAttribute("availableDatesByString",
						questionnaireService.getAvailableDatesByString(CreationController.DATE_FORMAT));

			}

			path = "/WEB-INF/deletion.html";

		} else {

			ctx.setVariable("message", "Only administrators can perform this action!");
			path = "/WEB-INF/message.html";
		}

		templateEngine.process(path, ctx, response.getWriter());

	}

	public void destroy() {
	}

}

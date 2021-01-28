package com.derin.damiano.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.derin.damiano.entities.User;
import com.derin.damiano.services.ProductService;
import com.derin.damiano.services.UserService;

import utils.ImageUtils;

/**
 * Servlet implementation class CreationController
 */
@WebServlet("/creation")
@MultipartConfig
public class CreationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	@EJB(name = "com.derin.damiano.services/UserService")
	private UserService userService;

	@EJB(name = "com.derin.damiano.services/ProductService")
	private ProductService productService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreationController() {
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

		if (user.isAdmin()) {

			// RequestDispatcher dispatcher =
			// getServletContext().getRequestDispatcher("/WEB-INF/creation.html");
			// dispatcher.forward(request, response);

			String path = "/WEB-INF/creation.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

			ctx.setVariable("date", new SimpleDateFormat("YYYY-MM-DD").format(new Date()));

			templateEngine.process(path, ctx, response.getWriter());

		} else {

			ServletContext servletContext = getServletContext();
			WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Only administrators can perform this action!");
			templateEngine.process("/index.html", ctx, response.getWriter());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		String path;
		if (user.isAdmin()) {

			String productName = StringEscapeUtils.escapeJava(request.getParameter("product_name"));
			String dateString = request.getParameter("date");
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-mm-dd").parse(dateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Part imgFile = request.getPart("picture");
			InputStream imgContent = imgFile.getInputStream();
			byte[] imgByteArray = ImageUtils.readImage(imgContent);

			int numQuestions = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("num_questions")));

			String questions[] = new String[numQuestions];
			for (int i = 0; i < numQuestions; i++) {
				questions[i] = StringEscapeUtils.escapeJava(request.getParameter("question_" + i));
			}

			productService.addProduct(date, imgByteArray, productName, questions);

			// redirection to adminhome
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/adminhome.html");
			dispatcher.forward(request, response);

		} else {

			ServletContext servletContext = getServletContext();
			WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Only administrators can perform this action!");
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
		}

	}

	public void destroy() {
	}

}

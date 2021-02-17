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
import com.derin.damiano.utils.ImageUtils;
import com.derin.damiano.utils.ServletHandler;

/**
 * Servlet implementation class CreationController
 */
@WebServlet("/creation")
@MultipartConfig
public class CreationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	private final String DATE_FORMAT = "yyyy-MM-dd";

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

		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

		String path = null;

		if (user.isAdmin()) {

			ctx.setVariable("date", new SimpleDateFormat(DATE_FORMAT).format(new Date()));
			path = "/WEB-INF/creation.html";

		} else {

			ctx.setVariable("message", "Only administrators can perform this action!");
			path = "/WEB-INF/message.html";
		}

		templateEngine.process(path, ctx, response.getWriter());

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

		String path;
		if (user.isAdmin()) {

			String productName = ServletHandler.getParameter(request, "product_name");
			String dateString = ServletHandler.getParameter(request, "date");
			Date date = null;
			try {
				date = new SimpleDateFormat(DATE_FORMAT).parse(dateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (productName != null && dateString != null) {

				Part imgFile = request.getPart("picture");
				InputStream imgContent = imgFile.getInputStream();
				byte[] imgByteArray = ImageUtils.readImage(imgContent);

				int numQuestions = Integer.parseInt(ServletHandler.getParameter(request, "num_questions"));

				String questions[] = new String[numQuestions];
				for (int i = 0; i < numQuestions; i++) {
					questions[i] = ServletHandler.getParameter(request, "question_" + i);
				}

				productService.addProduct(date, imgByteArray, productName, questions);

				path = "/WEB-INF/adminhome.html";
			} else {
				ctx.setVariable("message", "Something went wrong...");
				path = "/WEB-INF/message.html";
			}

		} else {

			ctx.setVariable("message", "Only administrators can perform this action!");
			path = "/WEB-INF/message.html";
		}

		templateEngine.process(path, ctx, response.getWriter());

	}

	public void destroy() {
	}

}

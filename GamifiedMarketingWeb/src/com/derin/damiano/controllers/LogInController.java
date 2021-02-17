package com.derin.damiano.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
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

import com.derin.damiano.entities.Product;
import com.derin.damiano.entities.Review;
import com.derin.damiano.entities.User;
import com.derin.damiano.services.ProductService;
import com.derin.damiano.services.QuestionnaireService;
import com.derin.damiano.services.ReviewService;
import com.derin.damiano.services.UserService;
import com.derin.damiano.utils.ServletHandler;

@WebServlet("/login")
public class LogInController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	@EJB(name = "com.derin.damiano.services/UsertableService")
	private UserService userService;

	@EJB(name = "com.derin.damiano.services/ProductService")
	private ProductService productService;

	@EJB(name = "com.derin.damiano.services/QuestionnaireService")
	private QuestionnaireService questionnaireService;

	@EJB(name = "com.derin.damiano.services/ReviewService")
	private ReviewService reviewService;

	public LogInController() {
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
		String password = ServletHandler.getParameter(request, "password");
		if (username == null || password == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value.");
			return;
		}

		User user;
		try {
			user = userService.checkCredentials(username, password);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials.");
			return;
		}

		String path;

		if (user == null) {

			ctx.setVariable("message", "Incorrect username or password");
			path = "/WEB-INF/message.html";

		} else {

			request.getSession().setAttribute("user", user);

			if (user.isAdmin()) {
				path = "/WEB-INF/adminhome.html";
			} else {

				Product product = productService.getProductOfTheDay();

				if (product != null) {

					request.getSession().setAttribute("product", product);
					request.getSession().setAttribute("hideQuestionnaireButton",
							questionnaireService.isQuestionnaireSubmitted(user, product));
					request.getSession().setAttribute("hideReviewButton",
							reviewService.isReviewSubmitted(user, product));

					path = "/WEB-INF/home.html";

				} else {
					path = "/WEB-INF/no_product_today.html";
				}

			}

		}

		templateEngine.process(path, ctx, response.getWriter());

	}

	public void destroy() {
	}
}
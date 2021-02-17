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

import com.derin.damiano.entities.Product;
import com.derin.damiano.entities.Review;
import com.derin.damiano.entities.User;
import com.derin.damiano.services.ProductService;
import com.derin.damiano.services.QuestionnaireService;
import com.derin.damiano.services.ReviewService;
import com.derin.damiano.services.UserService;
import com.derin.damiano.utils.ImageUtils;
import com.derin.damiano.utils.ServletHandler;

/**
 * Servlet implementation class CreationController
 */
@WebServlet("/review")
@MultipartConfig
public class ReviewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	@EJB(name = "com.derin.damiano.services/UserService")
	private UserService userService;

	@EJB(name = "com.derin.damiano.services/ProductService")
	private ProductService productService;

	@EJB(name = "com.derin.damiano.services/ReviewService")
	private ReviewService reviewService;

	@EJB(name = "com.derin.damiano.services/QuestionnaireService")
	private QuestionnaireService questionnaireService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReviewController() {
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

		HttpSession session = request.getSession();
		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

		String reviewContent = ServletHandler.getParameter(request, "review_content");
		System.out.println("New review: " + reviewContent);

		String path = null;
		if (reviewContent != null) {

			Product product = (Product) session.getAttribute("product");

			try {
				reviewService.addReview(product, (User) session.getAttribute("user"), reviewContent);

				boolean hide = reviewService.isReviewSubmitted((User) session.getAttribute("user"),
						(Product) session.getAttribute("product"));
				System.out.println("isReviewSubmitted: " + hide);

				session.setAttribute("hideReviewButton", hide);

				path = "/WEB-INF/home.html";

			} catch (Exception e) {
				e.printStackTrace();
//				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only one comment per product!");
				ctx.setVariable("message", "Only one comment per product!");
				path = "/WEB-INF/message.html";
//				return;
			}

		} else {

			ctx.setVariable("message", "Something went wrong with your comment...");
			path = "/WEB-INF/message.html";
		}

		templateEngine.process(path, ctx, response.getWriter());

	}

	public void destroy() {
	}

}

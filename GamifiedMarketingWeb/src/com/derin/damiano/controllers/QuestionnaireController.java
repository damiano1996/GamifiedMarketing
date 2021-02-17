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
@WebServlet("/questionnaire")
@MultipartConfig
public class QuestionnaireController extends HttpServlet {
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
	public QuestionnaireController() {
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
		ServletContext servletContext = getServletContext();
		WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

		User user = (User) session.getAttribute("user");
		Product product = (Product) session.getAttribute("product");

		List answers = (ArrayList<Answer>) session.getAttribute("answers");
		if (answers == null) {
			answers = new ArrayList<Answer>();
			for (Question question : product.getQuestions()) {
				Answer answer = new Answer("", question, user);
				answers.add(answer);
			}
			session.setAttribute("answers", answers);
		}

		templateEngine.process("/WEB-INF/marketing_questionnaire.html", ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

//		try {

		if (request.getParameter("submit").equals("Next")) {

			for (Answer answer : (ArrayList<Answer>) session.getAttribute("answers")) {
				String content = StringEscapeUtils
						.escapeJava(request.getParameter("" + answer.getId().getQuestionId()));
				answer.setContent(content);
			}

			templateEngine.process("/WEB-INF/statistical_questionnaire.html", ctx, response.getWriter());

		} else if (request.getParameter("submit").equals("Submit")) {

			int age = Integer.parseInt(ServletHandler.getParameter(request, "age"));
			String sex = StringEscapeUtils.escapeJava(ServletHandler.getParameter(request, "sex"));
			String expertiseLevel = StringEscapeUtils
					.escapeJava(ServletHandler.getParameter(request, "expertise_level"));

			questionnaireService.addQuestionnaire(user, (ArrayList<Answer>) session.getAttribute("answers"), age, sex,
					expertiseLevel);

			session.setAttribute("hideQuestionnaireButton",
					questionnaireService.isQuestionnaireSubmitted(user, (Product) session.getAttribute("product")));

			templateEngine.process("/WEB-INF/thanks.html", ctx, response.getWriter());

		} else if (request.getParameter("submit").equals("Previous")) {

			session.setAttribute("age", ServletHandler.getParameter(request, "age"));
			session.setAttribute("sex", ServletHandler.getParameter(request, "sex"));
			session.setAttribute("expertiseLevel", ServletHandler.getParameter(request, "expertise_level"));

			templateEngine.process("/WEB-INF/marketing_questionnaire.html", ctx, response.getWriter());

		} else if (request.getParameter("submit").equals("Cancel")) {
			templateEngine.process("/WEB-INF/home.html", ctx, response.getWriter());

		}

//		} catch (Exception e) {
//			ctx.setVariable("message", "Something went wrong with your questionnaire...");
//			templateEngine.process("/WEB-INF/message.html", ctx, response.getWriter());
//
//		}

	}

	public void destroy() {
	}

}

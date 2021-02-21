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
import com.derin.damiano.exceptions.BlockedException;
import com.derin.damiano.exceptions.EmptyQuestionnaireException;
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
			answers = questionnaireService.getEmptyAnswers(product.getDate(), user.getId());
			session.setAttribute("answers", answers);
		}

		templateEngine.process("/WEB-INF/marketing_questionnaire.html", ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Product product = (Product) session.getAttribute("product");

		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

//		try {

		String operation = request.getParameter("submit");

		if (operation.equals("Next")) {

			doNext(request, response, session, ctx);

		} else if (operation.equals("Submit")) {

			doSubmit(request, response, session, ctx);

		} else if (operation.equals("Previous")) {

			doPrevious(request, response, session, ctx);

		} else if (operation.equals("Cancel")) {

			doCancel(request, response, session, ctx);

		}

//		} catch (Exception e) {
//			ctx.setVariable("message", "Something went wrong with your questionnaire...");
//			templateEngine.process("/WEB-INF/message.html", ctx, response.getWriter());
//
//		}

	}

	private void doNext(HttpServletRequest request, HttpServletResponse response, HttpSession session, WebContext ctx)
			throws IOException {

		for (Answer answer : (ArrayList<Answer>) session.getAttribute("answers")) {
			System.out.println("Ans question id: " + answer.getId().getQuestionId());
			String content = ServletHandler.getParameter(request, "" + answer.getId().getQuestionId());
			System.out.println("Ans content: " + content);
			answer.setContent(content);
		}

		templateEngine.process("/WEB-INF/statistical_questionnaire.html", ctx, response.getWriter());
	}

	private void doSubmit(HttpServletRequest request, HttpServletResponse response, HttpSession session, WebContext ctx)
			throws IOException {
		User user = (User) session.getAttribute("user");
		Product product = (Product) session.getAttribute("product");

		String ageString = ServletHandler.getParameter(request, "age");
		int age = -1;
		if (ageString != null) {
			age = Integer.parseInt(ageString);
		}
		String sex = ServletHandler.getParameter(request, "sex");
		String expertiseLevel = ServletHandler.getParameter(request, "expertise_level");

		try {
			questionnaireService.addQuestionnaire(product.getDate(), user.getId(),
					(ArrayList<Answer>) session.getAttribute("answers"), age, sex, expertiseLevel);

			session.setAttribute("hideQuestionnaireButton",
					questionnaireService.isQuestionnaireSubmitted(product.getDate(), user.getId()));

			templateEngine.process("/WEB-INF/thanks.html", ctx, response.getWriter());

		} catch (BlockedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.setAttribute("hideQuestionnaireButton", user.isBlocked());
			ctx.setVariable("message", "Bad words are NOT admitted. You won't be able to submit new questionnaires!");
			templateEngine.process("/WEB-INF/message.html", ctx, response.getWriter());

		} catch (EmptyQuestionnaireException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ctx.setVariable("message", "Marketing questions must be filled!");
			templateEngine.process("/WEB-INF/message.html", ctx, response.getWriter());
		}
	}

	private void doPrevious(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			WebContext ctx) throws IOException {

		session.setAttribute("age", ServletHandler.getParameter(request, "age"));
		session.setAttribute("sex", ServletHandler.getParameter(request, "sex"));
		session.setAttribute("expertiseLevel", ServletHandler.getParameter(request, "expertise_level"));

		templateEngine.process("/WEB-INF/marketing_questionnaire.html", ctx, response.getWriter());
	}

	private void doCancel(HttpServletRequest request, HttpServletResponse response, HttpSession session, WebContext ctx)
			throws IOException {

		User user = (User) session.getAttribute("user");
		Product product = (Product) session.getAttribute("product");

		questionnaireService.cancelQuestionnaire(product.getDate(), user.getId());

		templateEngine.process("/WEB-INF/home.html", ctx, response.getWriter());

	}

	public void destroy() {
	}

}

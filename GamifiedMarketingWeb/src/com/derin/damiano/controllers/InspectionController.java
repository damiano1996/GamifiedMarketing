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
@WebServlet("/inspection")
@MultipartConfig
public class InspectionController extends HttpServlet {
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
	public InspectionController() {
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

			session.setAttribute("availableDatesByString", getAvailableDatesByString());
			path = "/WEB-INF/inspection.html";

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
			System.out.println("operation: " + operation);

			if (operation.contains("Questionnaire")) {

				String requestedDate = operation.replace("Questionnaire: ", "");
				session.setAttribute("requestedDate", requestedDate);
				System.out.println("Requested date: " + requestedDate);

				HashMap<String, Date> availableDatesByString = (HashMap<String, Date>) session
						.getAttribute("availableDatesByString");

				Date date = availableDatesByString.get(requestedDate);
				ArrayList<User> usersWhoSubmitted = questionnaireService.getUsersWhoSubmitted(date);
				session.setAttribute("usersWhoSubmitted", usersWhoSubmitted);

				ArrayList<User> usersWhoCancelled = questionnaireService.getUserWhoCancelledQuestionnaire(date);
				session.setAttribute("usersWhoCancelled", usersWhoCancelled);
				for (User u : usersWhoCancelled) {
					System.out.println("user who cancelled: " + u.getUsername());
				}

				// resetting other attributes
				session.setAttribute("marketingAnswers", null);
				session.setAttribute("statisticaldata", null);

			} else if (operation.contains("User")) {

				String[] parts = operation.split(" ");
				String userIdString = parts[1];
				int userId = Integer.parseInt(userIdString);
				System.out.println("Requested userId: " + userId);

				Date date = null;
				try {
					date = new SimpleDateFormat(CreationController.DATE_FORMAT)
							.parse((String) session.getAttribute("requestedDate"));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				ArrayList<Answer> marketingAnswers = questionnaireService.getMarketingAnswers(date, userId);
				session.setAttribute("marketingAnswers", marketingAnswers);

				Statisticaldata statisticaldata = questionnaireService.getStatisticalAnswers(date, userId);
				session.setAttribute("statisticaldata", statisticaldata);
			}

			path = "/WEB-INF/inspection.html";

		} else {

			ctx.setVariable("message", "Only administrators can perform this action!");
			path = "/WEB-INF/message.html";
		}

		templateEngine.process(path, ctx, response.getWriter());

	}

	private HashMap<String, Date> getAvailableDatesByString() {
		ArrayList<Date> availableDates = questionnaireService.getAvailableQuestionnaires();
		HashMap<String, Date> availableDatesByString = new HashMap<>();

		for (Date date : availableDates) {
			String dateString = new SimpleDateFormat(CreationController.DATE_FORMAT).format(date);
			availableDatesByString.put(dateString, date);
			System.out.println(date + " : " + dateString);
		}
		return availableDatesByString;
	}

	public void destroy() {
	}

}

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.constant.BookStoreConstants;
import com.bittercode.constant.ResponseCode;
import com.bittercode.constant.db.UsersDBConstants;
import com.bittercode.model.User;
import com.bittercode.model.UserRole;
import com.bittercode.service.UserService;
import com.bittercode.service.impl.UserServiceImpl;

public class CustomerRegisterServlet extends HttpServlet {

    private static final String PROPERTIES_FILE = "/WEB-INF/application.properties";  // Path to properties file
    private UserService userService = new UserServiceImpl();
    
    // Load properties from application.properties
    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = getServletContext().getResourceAsStream(PROPERTIES_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new IOException("Property file not found: " + PROPERTIES_FILE);
            }
        }
        return properties;
    }

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(BookStoreConstants.CONTENT_TYPE_TEXT_HTML);

        // Load DB credentials from the properties file
        private static final String db_host = System.getenv("DB_URL"); // Change to your DB URL
     private static final String db_username = System.getenv("DB_USERNAME");
     private static final String db_password = System.getenv("DB_PASSWORD");
     private static final String db_name = System.getenv("DB_NAME");  // Change to your DB password
     private static final String DB_DRIVER = "org.postgresql.Driver";

        // Now use the above credentials to connect to your database or configure your ORM (e.g., Hibernate)
        String jdbcUrl = "jdbc:postgresql://postgres.database.svc.cluster.local:5432/db"

        String pWord = req.getParameter(UsersDBConstants.COLUMN_PASSWORD);
        String fName = req.getParameter(UsersDBConstants.COLUMN_FIRSTNAME);
        String lName = req.getParameter(UsersDBConstants.COLUMN_LASTNAME);
        String addr = req.getParameter(UsersDBConstants.COLUMN_ADDRESS);
        String phNo = req.getParameter(UsersDBConstants.COLUMN_PHONE);
        String mailId = req.getParameter(UsersDBConstants.COLUMN_MAILID);
        User user = new User();
        user.setEmailId(mailId);
        user.setFirstName(fName);
        user.setLastName(lName);
        user.setPassword(pWord);
        user.setPhone(Long.parseLong(phNo));
        user.setAddress(addr);

        try {
            String respCode = userService.register(UserRole.CUSTOMER, user);
            System.out.println(respCode);
            if (ResponseCode.SUCCESS.name().equalsIgnoreCase(respCode)) {
                RequestDispatcher rd = req.getRequestDispatcher("CustomerLogin.html");
                rd.include(req, res);
                pw.println("<table class=\"tab\"><tr><td>User Registered Successfully</td></tr></table>");
            } else {
                RequestDispatcher rd = req.getRequestDispatcher("CustomerRegister.html");
                rd.include(req, res);
                pw.println("<table class=\"tab\"><tr><td>" + respCode + "</td></tr></table>");
                pw.println("Sorry for interruption! Try again");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

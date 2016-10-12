package controller;

import app.Session;
import transactions.SessionTransactions.GetSession;
import transactions.SessionTransactions.PostSession;
import transactions.Transaction;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class SessionController {
  
  public static class Login {
    public String email;
    public String password;
    public Login(String email, String password) {
      this.email = email;
      this.password = password;
    }
  }

  @RequestMapping(value = "/snss", method = RequestMethod.POST)
  public static void postSession(@RequestBody Login login, HttpServletRequest req, HttpServletResponse res) {
    PostSession post = new PostSession(login.email, login.password);
    String id = post.run(req, res);
    if (post.getResponseCode() == Transaction.Status.UNAUTHORIZED) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
    else {
      Cookie c = new Cookie(Session.COOKIE_NAME,id);
      c.setHttpOnly(true);
      res.addCookie(c);
    }
  }
  
  @RequestMapping(value = "/snss/curssn", method = RequestMethod.GET)
  public static Session getSession(HttpServletRequest req, HttpServletResponse res) {
    return new GetSession().run(req, res);
  }
  
}

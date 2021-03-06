package com.hly.sirius.controller;
import com.hly.sirius.domain.User;

import com.hly.sirius.service.UserService;
import com.hly.sirius.util.CryptographyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;

/**
 * @author hly
 * @blog: https://github.com/huangliangyun
 * @github: https://github.com/huangliangyun
 * @date 2018年9月13日 下午6:22:04
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	UserService userService;

	/**
	 * 登录界面
	 * @param session
	 * @return
	 */
	@RequestMapping("/login")
	public String login(HttpSession session){
		if(session.getAttribute("username")!=null&&session.getAttribute("username")!=""){
			return "/admin/admin_writing";
		}
		return "/user/login";
	}

	/**
	 * 注销
	 * @param session
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		if(session.getAttribute("username")!=null&&session.getAttribute("username")!=""){
			//SecurityUtils.getSubject().getSession().removeAttribute("username");
			SecurityUtils.getSubject().logout();
			System.err.println("注销成功!");
		}
		return "redirect:/index";
	}

	/**
	 * 登录验证
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping("/blogger")
   public String blogger(@RequestParam(value="username",required=false) String username, @RequestParam(value="password",required=false)String password, HttpSession session){

		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, CryptographyUtil.md5(password,username));
		try {
			subject.login(token);
			//System.err.println("验证成功");
			//session.setAttribute("username",username);
			return "redirect:/admin/writing/edition";
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return "redirect:/admin/login";
		}
	}

	//@RequestMapping("/register")
	public ModelAndView register(@RequestParam(value="username",required=false)String username,@RequestParam(value="password",required=false)String password){
		ModelAndView mView = new ModelAndView();
			try {
				User user = new User(username, CryptographyUtil.md5(password,username));
				System.err.println(user.toString());
				userService.insertUser(user);
				mView.setViewName("/login");
				return mView;
			} catch (Exception e) {
				mView.setViewName("/register");
				return mView;
			}
	}
}

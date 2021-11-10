package com.springboot.ecommerce.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.ecommerce.dto.ChangePasswordDTO;
import com.springboot.ecommerce.dto.LoginDTO;
import com.springboot.ecommerce.dto.ProfileDTO;
import com.springboot.ecommerce.dto.RegisterDTO;
import com.springboot.ecommerce.dto.ResetPasswordDTO;
import com.springboot.ecommerce.entity.Account;
import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.OrderDetail;
import com.springboot.ecommerce.service.IAccountService;
import com.springboot.ecommerce.service.ICategoryService;
import com.springboot.ecommerce.service.IOrderDetailService;
import com.springboot.ecommerce.util.CloudService;
import com.springboot.ecommerce.util.UploadService;

@Controller
public class AccountController {

	@Autowired
	private IAccountService accountService;

	@Autowired
	private CloudService cloud;

	@Autowired
	private IOrderDetailService orderDetailService;

	@Autowired
	private ICategoryService cService;

	@GetMapping("/login")
	public String showLogin(Model model) {
		model.addAttribute("account", new LoginDTO());
		return "page/site/account/login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("account") LoginDTO login, Model model, RedirectAttributes redirectAttributes) {
		Account account = accountService.login(login);
		System.out.println(BCrypt.checkpw("123321", "$2a$10$t3Wfc0JbxTLN4.GaalobaOj8cjjqaWVlnt1IbJB7uRg0.LfadqKZi"));
		System.out.println(BCrypt.hashpw("123321", BCrypt.gensalt()));
		if (account == null) {
			model.addAttribute("errorMess", "Invalid email or password!");
			return "page/site/account/login";
		} else {
			return account.getRole() == 1 ? "redirect:/admin" : "redirect:/";
		}
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		accountService.logout();
		return "redirect:/login";
	}

	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("account", new RegisterDTO());
		return "page/site/account/register";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("account") RegisterDTO account, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("confirmError", "Not the same as the password!");
			return "page/site/account/register";
		}
		accountService.register(account);
		return "redirect:/login";
	}

	@GetMapping("/accounts")
	public String profile(@RequestParam("id") Long id, Model model,
			@RequestParam(value = "suMess", required = false) String suMess) {
		ProfileDTO account = accountService.findByIdSite(id);
		model.addAttribute("account", account);
		model.addAttribute("suMess", suMess);
		return "page/site/account/profile";
	}

	@PostMapping("/account/update")
	public String update(@Valid @ModelAttribute("account") ProfileDTO profile, BindingResult result, Model model,
			@RequestPart(value = "fileUpload", required = false) MultipartFile fileUpload,
			RedirectAttributes attributes) throws IllegalStateException, IOException {
		if (result.hasErrors()) {
			System.out.println("asddsa");
			return "page/site/account/profile";
		} else {
			if (!fileUpload.isEmpty()) {
				System.out.println("CO upload");
				profile.setAvatar(cloud.uploadSingleFile(fileUpload));
			}
			profile = accountService.updateProfile(profile);
			attributes.addAttribute("suMess", "Update profile successfully!");
			return "redirect:/accounts?id=" + profile.getId();
		}

	}

	@GetMapping("/changepass")
	public String changePassword(@RequestParam("id") Long id, Model model) {
		ChangePasswordDTO cp = new ChangePasswordDTO(id);
		model.addAttribute("account", cp);
		return "page/site/account/changepass";
	}

	@PostMapping("/changepass")
	public String handleChangePassword(@Valid @ModelAttribute("account") ChangePasswordDTO cp, BindingResult result,
			Model model) {
		System.out.println(cp.toString());
		if (!result.hasErrors() && accountService.isExistAcountHasPassword(cp.getId(), cp.getPassword())
				&& cp.getNewPassword().equals(cp.getConfirmPassword())) {
			cp = accountService.changePassword(cp);
			model.addAttribute("succesMess", "Password has been changed successfully!");
			return "page/site/account/changepass";
		} else {
			if (!accountService.isExistAcountHasPassword(cp.getId(), cp.getPassword())) {
				model.addAttribute("errorPass", "Invalid password!");
			}
			if (!cp.getNewPassword().equals(cp.getConfirmPassword())) {
				model.addAttribute("errorNew", "Invalid New password and confirm!");
			}
			return "page/site/account/changepass";
		}
	}

	@GetMapping("/deactivate")
	public String deactivate(@RequestParam("id") Long id, Model model) {
		model.addAttribute("id", id);
		return "page/site/account/deactivate";
	}

	@PostMapping("/deactivate")
	public String handleDeactivate(@RequestParam("id") Long id) throws Exception {
		Account account = accountService.findById(id);
		account.setStatus(0);
		accountService.save(account);
		accountService.logout();
		return "redirect:/";
	}

	@GetMapping("/forgot")
	public String forgot() {
		return "page/site/account/forgot";
	}

	@PostMapping("/forgot")
	public String handleForgot(@RequestParam("email") String email, Model model) {
		Account account = accountService.findByEmail(email);
		if (account == null) {
			model.addAttribute("erMess", "Invalid Email!");
			return "page/site/account/forgot";
		} else {
			accountService.sendOtp(account, email);
			return "redirect:/reset";
		}
	}

	@GetMapping("/reset")
	public String reset(Model model) {
		model.addAttribute("account", new ResetPasswordDTO());
		return "page/site/account/resetpass";
	}

	@PostMapping("/reset")
	public String handleReset(@Valid @ModelAttribute("account") ResetPasswordDTO rp, BindingResult result, Model model) {
		Account account = accountService.findByOtpAvailable(rp.getOtp());
		if (account == null) {
			model.addAttribute("otpMess", "Invalid OTP!");
		} else {
			if (rp.getPassword().equalsIgnoreCase(rp.getConfirmPassword())) {
				model.addAttribute("suMess", "Reset Password succcessfully!");
				accountService.resetPass(account.getId(), rp);
			} else {
				model.addAttribute("erMess", "Invalid Password and Confirm Password!");
			}
		}
		return "page/site/account/resetpass";
	}

	@ModelAttribute("orderDetails")
	public List<OrderDetail> getOrderDetails() {
		return orderDetailService.findByAccountId();
	}

	@ModelAttribute("cartTotals")
	public Double getCartTotals() {
		return orderDetailService.getCartTotals();
	}

	@ModelAttribute("cartItems")
	public Integer getCartItems() {
		return orderDetailService.getCartItems();
	}

	@ModelAttribute("categories")
	public List<Category> getCategories() {
		return cService.getAll();
	}
}

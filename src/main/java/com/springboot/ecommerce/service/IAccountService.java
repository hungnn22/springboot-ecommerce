package com.springboot.ecommerce.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;

import com.springboot.ecommerce.dto.ChangePasswordDTO;
import com.springboot.ecommerce.dto.LoginDTO;
import com.springboot.ecommerce.dto.ProfileDTO;
import com.springboot.ecommerce.dto.RegisterDTO;
import com.springboot.ecommerce.dto.ResetPasswordDTO;
import com.springboot.ecommerce.entity.Account;
import com.springboot.ecommerce.entity.Order;

public interface IAccountService {

	Account login(LoginDTO login);

	Account findById(Long id);

	void logout();

	RegisterDTO register(RegisterDTO account);

	ProfileDTO findByIdSite(Long id);

	ProfileDTO updateProfile(ProfileDTO account);

	boolean isExistAcountHasPassword(Long id, String password);

	ChangePasswordDTO changePassword(ChangePasswordDTO cp);

	Account save(Account account) throws Exception;

	Account findByEmail(String email);

	void sendOtp(Account account, String email);

	Account findByOtpAvailable(String otp);

	void resetPass(Long id, @Valid ResetPasswordDTO rp);

	Page<Account> paginated(Integer page, Integer limit, String sort, String order, Integer role, String search);

	boolean validateAccount(Account account);

	void deleteById(Long id);

}

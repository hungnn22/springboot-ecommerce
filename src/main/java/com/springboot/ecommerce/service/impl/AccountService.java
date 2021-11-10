package com.springboot.ecommerce.service.impl;

import java.util.List;

import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.springboot.ecommerce.config.ModelMapperConfig;
import com.springboot.ecommerce.dto.ChangePasswordDTO;
import com.springboot.ecommerce.dto.LoginDTO;
import com.springboot.ecommerce.dto.ProfileDTO;
import com.springboot.ecommerce.dto.RegisterDTO;
import com.springboot.ecommerce.dto.ResetPasswordDTO;
import com.springboot.ecommerce.entity.Account;
import com.springboot.ecommerce.entity.Verify;
import com.springboot.ecommerce.repository.AccountRepository;
import com.springboot.ecommerce.service.IAccountService;
import com.springboot.ecommerce.service.IMailService;
import com.springboot.ecommerce.service.IVerifyService;
import com.springboot.ecommerce.util.CookieService;
import com.springboot.ecommerce.util.GeneratorService;
import com.springboot.ecommerce.util.SessionService;

@Service
public class AccountService implements IAccountService {

	@Autowired
	private AccountRepository repo;

	@Autowired
	private SessionService session;

	@Autowired
	private CookieService cookie;

	@Autowired
	private ModelMapperConfig mapper;

	@Autowired
	private IMailService mailService;

	@Autowired
	private GeneratorService generator;

	@Autowired
	private IVerifyService verifyService;

	@Override
	public Account login(LoginDTO login) {
		Account account = repo.findByEmail(login.getEmail());
		if (account != null && BCrypt.checkpw(login.getPassword(), account.getPassword())) {
			if (account.getStatus() == 0) {
				account.setStatus(1);
				repo.save(account);
			}
			session.set("account", account);
			if (login.isRemember()) {
				cookie.add("account", login.getEmail(), 10 * 24);
			} else {
				cookie.remove("account");
			}
			return account;
		}
		return null;
	}

	@Override
	public Account findById(Long id) {
		return repo.getById(id);
	}

	@Override
	public ProfileDTO findByIdSite(Long id) {
		Account account = repo.getById(id);
		ProfileDTO profile = mapper.getModelMapper().map(account, ProfileDTO.class);
		return profile;
	}

	@Override
	public void logout() {
		session.remove("account");
	}

	@Override
	public RegisterDTO register(RegisterDTO register) {
		Account account = mapper.getModelMapper().map(register, Account.class);
		account.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
		account = repo.save(account);
		register = mapper.getModelMapper().map(account, RegisterDTO.class);
		try {
			mailService.pusth(register.getEmail(), "Regiter",
					"You have successfully registered your account! Sign in and start shopping now");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return register;
	}

	@Override
	public ProfileDTO updateProfile(ProfileDTO profile) {
		System.out.println(profile.toString());
		Account account = repo.getById(profile.getId());
		BeanUtils.copyProperties(profile, account);
		System.out.println(account.toString());
		account = repo.save(account);
		profile = mapper.getModelMapper().map(account, ProfileDTO.class);
		return profile;
	}

	@Override
	public boolean isExistAcountHasPassword(Long id, String password) {
		Account account = repo.getById(id);
		System.out.println(account.getPassword());
		if (account != null) {
			System.out.println("KHAC NULL");
			if (BCrypt.checkpw(password, account.getPassword()))
				return true;
		}
		System.out.println("FALSE");
		return false;
	}

	@Override
	public ChangePasswordDTO changePassword(ChangePasswordDTO cp) {
		Account account = repo.getById(cp.getId());
		account.setPassword(BCrypt.hashpw(cp.getNewPassword(), BCrypt.gensalt()));
		account = repo.save(account);
		cp = mapper.getModelMapper().map(account, ChangePasswordDTO.class);
		return cp;

	}

	@Override
	public Account save(Account account) throws Exception {
		account.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
		return repo.save(account);
	}

	@Override
	public Account findByEmail(String email) {
		return repo.findByEmail(email);
	}

	@Override
	public void sendOtp(Account account, String email) {
		try {
			String otp = generator.getRandomPassword(6);
			verifyService.save(new Verify(otp, account));
			mailService.pusth(email, "Reset Account",
					"Hi, we received a password reset request for your account. Your OPT is: " + otp
							+ ". Please do not share this email to others!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Account findByOtpAvailable(String otp) {
		Account account = repo.findByOtp(otp);
		return account != null ? account : null;
	}

	@Override
	public void resetPass(Long id, @Valid ResetPasswordDTO rp) {
		Account account = repo.getById(id);
		account.setPassword(BCrypt.hashpw(rp.getPassword(), BCrypt.gensalt()));
		repo.save(account);
	}

	@Override
	public Page<Account> paginated(Integer page, Integer limit, String sort, String order, Integer role,
			String search) {
		Sort filters = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sort).ascending()
				: Sort.by(sort).descending();
		Pageable pageable = PageRequest.of(page - 1, limit, filters);
		if (role != -1) {
			return repo.findByRole(role, pageable);
		}
		return repo.findByNameContainingOrEmailContaining(search, search, pageable);
	}

	@Override
	public boolean validateAccount(Account account) {
		Account result = repo.findByEmail(account.getEmail());
		return result == null;
	}

	@Override
	public void deleteById(Long id) {
		repo.deleteById(id);

	}

}

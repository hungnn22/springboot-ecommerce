package com.springboot.ecommerce.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.ecommerce.config.ModelMapperConfig;
import com.springboot.ecommerce.dto.CheckoutDTO;
import com.springboot.ecommerce.entity.Account;
import com.springboot.ecommerce.entity.Order;
import com.springboot.ecommerce.entity.OrderDetail;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.entity.Status;
import com.springboot.ecommerce.repository.OrderRepository;
import com.springboot.ecommerce.service.IAccountService;
import com.springboot.ecommerce.service.IMailService;
import com.springboot.ecommerce.service.IOrderService;
import com.springboot.ecommerce.service.IOrderStatusService;
import com.springboot.ecommerce.service.IProductService;
import com.springboot.ecommerce.util.SessionService;

@Service
public class OrderService implements IOrderService {
	@Autowired
	private OrderRepository repository;

	@Autowired
	private SessionService session;

	@Autowired
	private IAccountService accountService;

	@Autowired
	private ModelMapperConfig mapper;

	@Autowired
	private IMailService mailService;

	@Autowired
	private IProductService productService;

	@Autowired
	private IOrderStatusService statusService;

	private Product product = null;

	@Override
	public Order save(Order detail) {
		return repository.save(detail);
	}

	@Override
	public Order findOrderNotConfirmedByAccountId(Long id) {
		return repository.findOrderNotConfirmedByAccountId(id);
	}

	@Override
	public void checkout(CheckoutDTO checkout) {
		System.out.println(checkout.toString());
		Account account = session.get("account");
		if (account != null) {
			Order order = this.findOrderNotConfirmedByAccountId(account.getId());
			BeanUtils.copyProperties(checkout, order);
			order.setCreateAt(new Date());
			order.setStatus(statusService.findById(2L));
			try {
				order = repository.save(order);
				this.updateProductQty(order, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(order.toString());
		}
	}

	// update product qty when checkout or reject
	private void updateProductQty(Order order, boolean state) {
		order.getDetails().forEach(detail -> {
			product = detail.getProduct();
			System.out.println("Before: " + product.getQty());
			product.setQty(state ? product.getQty() - detail.getQty() : product.getQty() + detail.getQty());
			productService.save(product);
			System.out.println("After: " + product.getQty());
		});

	}

	@Override
	public List<Order> findByAccount(Account account) {
		if (account != null)
			return repository.findByAccount(account);
		return null;
	}

	@Override
	public void reject(Long orderId) {
		Order order = repository.getById(orderId);
		order.setStatus(statusService.findById(4L));
		this.updateProductQty(order, false);
		repository.save(order);

	}

	public void reset(Long orderId) {
		Order order = repository.getById(orderId);
		order.setStatus(statusService.findById(2L));
		this.updateProductQty(order, false);
		repository.save(order);

	}

	@Override
	public List<Order> findAll() {
		return repository.findAll();
	}

	@Override
	public List<Order> findTop4() {
		Pageable pageable = PageRequest.of(0, 4, Sort.by("createAt").descending());
		Page<Order> page = repository.findAll(pageable);
		return page.getContent();
	}

	@Override
	public void confirm(Long orderId) {
		Order order = repository.getById(orderId);
		order.setStatus(statusService.findById(3L));
		order.setCreateAt(new Date());

		Account account = accountService.findById(order.getAccount().getId());

		List<OrderDetail> details = order.getDetails();

		StringBuilder detailsMess = new StringBuilder();

		details.forEach(item -> {
			String str = item.getProduct().getName() + "(" + item.getQty() + " x $" + item.getPrice() + " = $"
					+ item.getQty() * item.getPrice();
			detailsMess.append(str).append(" || ");
		});

		try {
			mailService.pusth(account.getEmail(), "Confirm order!",
					"Hi " + account.getName() + ". Your order has been confirmed by us. Order details include: "
							+ detailsMess.toString() + "Total: $" + order.getTotal() + " Thank you!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		repository.save(order);

	}

	@Override
	public Page<Order> paginated(Integer page, Integer limit, String sort, String order, Long accountId, Long statusId,
			String search) {
		Sort filters = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sort).ascending()
				: Sort.by(sort).descending();
		Pageable pageable = PageRequest.of(page - 1, limit, filters);
		if (statusId == -1) {
			if (accountId == -1)
				return repository.findBySearchKey(search, pageable);
			else {
				if (search == "")
					return repository.findByAccountId(accountId, pageable);
				else
					return repository.findByAccountInfo(accountId, search, pageable);
			}
		} else {
			Status status = statusService.findById(statusId);
			return repository.findByStatus(status, pageable);
		}
	}

	@Override
	public Page<Order> paginated(Integer page, Long accountId) {
		Account account = session.get("account");
		Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("createAt").descending());
		Status status = statusService.findById(1L);
		return repository.findByAccountAndStatusNot(account, status, pageable);
	}

	@Override
	public Long getPendingCount() {
		return repository.getPendingCount();
	}

}

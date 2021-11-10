package com.springboot.ecommerce.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String address;
	private String phone;
	private String note;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt = new Date();

	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status status = new Status(1L);

	@OneToMany(mappedBy = "order")
	private List<OrderDetail> details = new ArrayList<OrderDetail>();

	public Order(Account account) {
		super();
		this.account = account;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", address=" + address + ", phone=" + phone + ", note=" + note + ", status="
				+ status.getValue() + ", createAt=" + createAt + "]";
	}

	public Double getTotal() {
		Double total = details.stream().mapToDouble(item -> item.getQty() * item.getPrice()).reduce(0, (a, b) -> a + b);
		return total;
	}

}

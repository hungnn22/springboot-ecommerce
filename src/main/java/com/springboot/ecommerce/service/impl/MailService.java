package com.springboot.ecommerce.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.springboot.ecommerce.dto.MailModelDTO;
import com.springboot.ecommerce.service.IMailService;

@Service
public class MailService implements IMailService {

	@Autowired
	private JavaMailSender sender;

	List<MimeMessage> queue = new ArrayList<MimeMessage>();

	@Override
	public void pusth(String to, String sub, String body) throws Exception {
		MailModelDTO mailModel = new MailModelDTO(to, sub, body);
		this.push(mailModel);
	}

	@Override
	public void push(MailModelDTO mail) throws Exception {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
		helper.setFrom(new InternetAddress(mail.getFrom(), "Pro"));
		helper.setTo(mail.getTo());
		helper.setSubject(mail.getSub());
		helper.setText(mail.getBody());
		helper.setReplyTo(mail.getFrom());
		for (String email : mail.getCc()) {
			helper.addCc(email);
		}
		for (String email : mail.getBcc()) {
			helper.addBcc(email);
		}
		for (File file : mail.getFiles()) {
			helper.addAttachment(file.getName(), file);
		}
		queue.add(message);
	}

	@Override
	@Scheduled(fixedDelay = 1000)
	public void run() {
		int success = 0, error = 0;
		while (!queue.isEmpty()) {
			MimeMessage message = queue.remove(0);
			try {
				sender.send(message);
				success++;
			} catch (Exception e) {
				error++;
				e.printStackTrace();
			}
		}
		System.out.println("SUCCESS: " + success + "- ERROR: " + error);
	}
}

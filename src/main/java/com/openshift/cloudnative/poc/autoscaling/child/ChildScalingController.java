package com.openshift.cloudnative.poc.autoscaling.child;

import java.security.SecureRandom;
import java.util.Collection;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChildScalingController {

	@Autowired
	private MyRepository repository;

	@GetMapping(path = "/")
	public String status() {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child ready on host " + hostname + " \n";

		System.out.println(message);

		return message;
	}

	@GetMapping(path = "/childlightdataload")
	public String childlightdataload() {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child on host " + hostname + " - light data load ";

		Iterable<MyEntity> entities = repository.findAll();

		message += " - " + ((Collection<?>) entities).size() + " entities loaded";
		
		System.out.println(message);

		return message;
	}

	@GetMapping(path = "/childdelayeddataload")
	public String childdelayeddataload() {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child on host " + hostname + " - delayed data load ";
		for (int i = 0; i < 1000000; i++) {
			try {
				byte[] iv = new byte[16];
				new SecureRandom().nextBytes(iv);

				// IV
				IvParameterSpec ivSpec = new IvParameterSpec(iv);

				// Key
				KeyGenerator generator = KeyGenerator.getInstance("AES");
				generator.init(128);
				SecretKey secretKey = generator.generateKey();

				// Encrypt
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
				cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
				cipher.update("0123456789012345".getBytes());

				byte[] data = cipher.doFinal();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Iterable<MyEntity> entities = repository.findAll();

		message += " - " + ((Collection<?>) entities).size() + " entities loaded";
		
		System.out.println(message);

		return message;
	}

	@PostMapping(path = "/create", consumes="application/json")
	public String create(@RequestBody MyEntity entity) {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child on host " + hostname + " - light data load ";

		repository.save(entity);

		message += " - Entity:" + entity.getId() + " saved";
		
		System.out.println(message);

		return message;
	}
}

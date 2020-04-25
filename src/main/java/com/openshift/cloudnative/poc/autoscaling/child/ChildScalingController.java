package com.openshift.cloudnative.poc.autoscaling.child;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

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

	@GetMapping(path = "/childNoCPULoadAll")
	public String childNoCPULoadAll() {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child on host " + hostname + " - data loadAll ";

		long timer = System.currentTimeMillis();
		List<MyEntity> entities = repository.findAll();
		
		message += " - " + entities.size() + " entities in " + (System.currentTimeMillis() - timer) + "[ms]";
		
		System.out.println(message);

		return message;
	}
	
//	@GetMapping(path = "/childNoCPULoad")
//	public String childNoCPULoad(@RequestParam(value = "resultSetSize", defaultValue = "10") Integer resultSetSize) {
//		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
//		String message = "Child on host " + hostname + " - data load ";
//		
//		long timer = System.currentTimeMillis();
//		Pageable request = PageRequest.of(0, 1);
//		Iterable<MyEntity> entities = repository.findAll(request);
//
//		message += " - " + ((Collection<?>) entities).size() + " entities in " + (System.currentTimeMillis() - timer) + "[ms]";
//		
//		System.out.println(message);
//
//		return message;
//	}

	@GetMapping(path = "/childHighCPULoadAll", produces = "text/html")
	@ApiOperation(value="Heavy CPU API call", notes="Generate CPU by looping on cipher.update(), default value for the number of loops is 1000.")
	public String childHighCPULoadAll(@RequestParam(value="childLoopNumber", defaultValue = "1000") Optional<Integer> childLoopNumber) {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child on host " + hostname + " - high CPU + data loadAll ";
		
		long timer = System.currentTimeMillis();
		
		generateCPU(childLoopNumber);

		List<MyEntity> entities = repository.findAll();

		message += " - " + entities.size() + " entities in " + (System.currentTimeMillis() - timer) + "[ms]";
		
		System.out.println(message);

		return message;
	}
	
//	@GetMapping(path = "/childHighCPULoad", produces = "text/html")
//	@ApiOperation(value="Heavy CPU API + data load", notes="Generate CPU by looping on cipher.update(), default value for the number of loops is 1000.")
//	public String childHighCPULoad(@RequestParam(value="childLoopNumber", defaultValue = "1000") Optional<Integer> childLoopNumber, @RequestParam(value="resultSetSize", defaultValue = "10") Integer resultSetSize) {
//		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
//		String message = "Child on host " + hostname + " - high CPU + data load ";
//		
//		long timer = System.currentTimeMillis();
//		
//		generateCPU(childLoopNumber);
//
//		//Iterable<MyEntity> entities = repository.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id")));
//		Pageable request = PageRequest.of(0, 1);
//		Iterable<MyEntity> entities = repository.findAll(request);
//		
//		message += " - " + ((Collection<?>) entities).size() + " entities in " + (System.currentTimeMillis() - timer) + "[ms]";
//		
//		System.out.println(message);
//
//		return message;
//	}

	@PostMapping(path = "/save", consumes="application/json")
	public String save(@RequestBody MyEntity entity) {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child on host " + hostname + " - light data load ";

		MyEntity savedEntity = repository.save(entity);

		message += " - Entity:" + savedEntity.getId() + " saved";
		
		System.out.println(message);

		return message;
	}
	
//	@PostMapping(path = "/saveAll", consumes="application/json")
//	public String saveAll(@RequestBody List<MyEntity> entities) {
//		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
//		String message = "Child on host " + hostname + " - light data load ";
//
//		List<MyEntity> savedEntities = repository.saveAll(entities);
//
//		message += " - " + savedEntities.size() + " saved ";
//		
//		System.out.println(message);
//
//		return message;
//	}
	
	private void generateCPU(Optional<Integer> loopNumber) {
		int defaultLoopNumber = 1000;
		if (loopNumber.isPresent()) defaultLoopNumber = loopNumber.get().intValue();
		for (int i = 0; i < defaultLoopNumber; i++) {
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
				cipher.doFinal();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

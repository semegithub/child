package com.openshift.cloudnative.poc.autoscaling.child;

import java.security.SecureRandom;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping(path = "/", produces = "text/html")
	public String status() {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child ready on host " + hostname + " \n";

		System.out.println(message);

		return message;
	}
	
	@GetMapping(path = "/healthz")
	public String healthz() {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child health check probe on host " + hostname + " \n";

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
	@ApiOperation(value = "High CPU + DB loadAll")
	public String childHighCPULoadAll(
			@RequestParam(value = "childstressCounter", defaultValue = "1000") Integer childstressCounter) {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child on host " + hostname + " - childHighCPULoadAll - CPU stress counter:" + childstressCounter;
		try {
			
			long timer = System.currentTimeMillis();

			generateCPU(childstressCounter.intValue());

			List<MyEntity> entities = repository.findAll();

			message += " - loaded " + entities.size() + " entities in " + (System.currentTimeMillis() - timer) + "[ms]";

		} catch (Exception e) {
			message += " - " + e.getMessage();
		} finally {
			System.out.println(message);
		}

		return message;
	}
	
	@GetMapping(path = "/childHighCPUCall", produces = "text/html")
	@ApiOperation(value = "High CPU")
	public String childHighCPUCall(
			@RequestParam(value = "childstressCounter", defaultValue = "1000") Integer childstressCounter) {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child on host " + hostname + " - childHighCPULoadAll - CPU stress counter:" + childstressCounter;
		try {
			
			long timer = System.currentTimeMillis();

			generateCPU(childstressCounter.intValue());

			message += " - done in " + (System.currentTimeMillis() - timer) + "[ms]";

		} catch (Exception e) {
			message += " - " + e.getMessage();
		} finally {
			System.out.println(message);
		}

		return message;
	}

	@GetMapping(path = "/childLoadAll", produces = "text/html")
	@ApiOperation(value = "DB loadAll")
	public String childLoadAll() {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child on host " + hostname + " - ";
		try {
			
			long timer = System.currentTimeMillis();

			List<MyEntity> entities = repository.findAll();

			message += " - loaded " + entities.size() + " entities in " + (System.currentTimeMillis() - timer) + "[ms]";

		} catch (Exception e) {
			message += " - " + e.getMessage();
		} finally {
			System.out.println(message);
		}

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

	@GetMapping(path = "/findAll", produces = "text/html")
	public String findAll() {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		String message = "Child on host " + hostname + " - findAll ";
		
		long timer = System.currentTimeMillis();
		List<MyEntity> entities = repository.findAll();

		message += " - loaded " + entities.size() + " entities in " + (System.currentTimeMillis() - timer) + "[ms]";

		System.out.println(message);

		return message;
	}

	@PostMapping(path = "/save", consumes = "application/json")
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

	private void generateCPU(Integer counter) {
		int loopNumber = 1000;
		loopNumber = counter.intValue();
		for (int i = 0; i < loopNumber; i++) {
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

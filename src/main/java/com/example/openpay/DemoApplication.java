package com.example.openpay;

import java.math.BigDecimal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.openpay.client.Address;
import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

@SpringBootApplication
@RestController
@RequestMapping("openpay")
public class DemoApplication {
	
	OpenpayAPI api;
	
	Customer customer;
	
	Card card;
	
	int min = 1;
    int max = 1000000;
		
	public DemoApplication() {
		api = new OpenpayAPI("https://sandbox-api.openpay.mx", "sk_54ad15f7a21241f29a2c48701c0e932a", "mthqdng5t4arlj8aj4o5");
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@GetMapping("/healthCheck")
	public String healtCheck() {
		return "service up";		
	}
	
	@GetMapping("/customer")
	public String customer() throws OpenpayServiceException, ServiceUnavailableException {
		Address address = new Address()
				.line1("Calle Morelos #12 - 11")
				.line2("Colonia Centro")             // Optional
				.line3("Cuauhtémoc")                 // Optional
				.city("Distrito Federal")
				.postalCode("12345")	
				.state("Queretaro")
				.countryCode("MX");                  // ISO 3166-1 two-letter code
				    
		customer = api.customers().create(new Customer()
		        .name("John")
		        .lastName("Doe")
		        .email("johndoe@example.com")
		        .phoneNumber("554-170-3567")
		        .address(address));

		customer.toString();
		
		return "OK "+ customer.toString();
	}
	
	@GetMapping("/createCreditCard")
	public String createCreditCard() throws OpenpayServiceException, ServiceUnavailableException {
		card = new Card();
		card.holderName("Juan Perez Ramirez");
		card.cardNumber("4111111111111111");
		card.cvv2("110");
		card.expirationMonth(12);
		card.expirationYear(24);
		card.setDeviceSessionId("kR1MiQhz2otdIuUlQkbEyitIqVMiI16f");
		Address address = new Address();
		address.city("Queretaro");
		address.countryCode("MX");
		address.state("Queretaro");
		address.postalCode("79125");
		address.line1("Av. Pie de la cuesta #12");
		address.line2("Desarrollo San Pablo");
		address.line3("Qro. Qro.");
		card.address(address);

		card = api.cards().create(customer.getId(), card);
		return "credit card created "+ card.getId();
	}
	
	@GetMapping("/chargingCreditCard")
	public String chargingCreditCard() throws OpenpayServiceException, ServiceUnavailableException {
		
		CreateCardChargeParams request = new CreateCardChargeParams();
		
		request.cardId(card.getId()); // =source_id
		request.amount(new BigDecimal("100.00"));
		request.currency("MXN");
		request.description("Cargo inicial a mi merchant");
		int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
		request.orderId("oid-00"+String.valueOf(random_int));
		request.deviceSessionId("kR1MiQhz2otdIuUlQkbEyitIqVMiI16f");														
		
		Charge charge = api.charges().createCharge(customer.getId(), request);
		

		
		return "OK "+ charge.toString();

	}

}

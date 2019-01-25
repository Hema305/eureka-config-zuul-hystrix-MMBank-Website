package com.moneymoney.web.controller;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.moneymoney.web.entity.CurrentDataSet;
import com.moneymoney.web.entity.Transaction;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@EnableDiscoveryClient
@Controller
@Service
public class BankAppController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/DepositForm")
	public String deposit() {
		return "DepositForm";
	}

	@RequestMapping("/FundTransferForm")
	public String FundTransferForm() {
		return "FundTransferForm";
	}

	@RequestMapping("/WithdrawForm")
	public String withdraw() {
		return "WithdrawForm";
	}
	 @HystrixCommand(fallbackMethod = "errorpage")
	@RequestMapping("/deposit")
	public String deposit(@ModelAttribute Transaction transaction,
			Model model) {
		restTemplate.postForEntity("http://transactions/banktransactions/deposits", 
				transaction, null);
		model.addAttribute("message","Success!");
		return "DepositForm";
	}
	 
	@RequestMapping("/withdraw")
	@HystrixCommand(fallbackMethod = "errorpage")
	public String withdraw(@ModelAttribute Transaction transaction,
			Model model) {
		restTemplate.postForEntity("http://transactions/banktransactions/withdraws", 
				transaction, null);
		model.addAttribute("message","Success!");
		return "WithdrawForm";
	}
	
	@RequestMapping("/FundTransfer")
	@HystrixCommand(fallbackMethod = "errorpage")
	public String fundtransfer(@RequestParam("sender") int senderAccountNumber,@RequestParam("reciever") int reciverAccountNumber,@ModelAttribute Transaction transaction, 
			Model model) {
		transaction.setAccountNumber(senderAccountNumber);
		restTemplate.postForEntity("http://transactions/banktransactions/withdraws", 
				transaction, null);
		transaction.setAccountNumber(reciverAccountNumber);
		restTemplate.postForEntity("http://transactions/banktransactions/deposits", 
				transaction, null);
		model.addAttribute("message","Success!");
		return "FundTransferForm";
	}

	@RequestMapping("/statementDeposit")
	//@HystrixCommand(fallbackMethod = "errorpage")
	public ModelAndView getStatementDeposit(@RequestParam("offset") int offset, @RequestParam("size") int size) {
		CurrentDataSet currentDataSet = restTemplate.getForObject("http://transactions/banktransactions/statements", CurrentDataSet.class);
		int currentSize=size==0?5:size;
		int currentOffset=offset==0?1:offset;
		Link next=linkTo(methodOn(BankAppController.class).getStatementDeposit(currentOffset+currentSize,currentSize)).withRel("next");
		Link previous=linkTo(methodOn(BankAppController.class).getStatementDeposit(currentOffset-currentSize, currentSize)).withRel("previous");
		List<Transaction> transactions = currentDataSet.getTransactions();
		List<Transaction> currentDataSetList = new ArrayList<Transaction>();
		
		for (int i = currentOffset - 1; i < currentSize + currentOffset - 1; i++) { 
			  if((transactions.size()<=i && i>0) || currentOffset<1) 
				  break;
			Transaction transaction = transactions.get(i);
			currentDataSetList.add(transaction);
			
		}
		CurrentDataSet dataSet = new CurrentDataSet(currentDataSetList, next, previous);
		/*
		 * currentDataSet.setNextLink(next); currentDataSet.setPreviousLink(previous);
		 */
		return new ModelAndView("DepositForm","currentDataSet",dataSet);
	}
	
	@RequestMapping("/statementWithdraw")
	//@HystrixCommand(fallbackMethod = "errorpage")
	public ModelAndView getStatementWithdraw(@RequestParam("offset") int offset, @RequestParam("size") int size) {
		CurrentDataSet currentDataSet = restTemplate.getForObject("http://transactions/banktransactions/statements", CurrentDataSet.class);
		int currentSize=size==0?5:size;
		int currentOffset=offset==0?1:offset;
		Link next=linkTo(methodOn(BankAppController.class).getStatementDeposit(currentOffset+currentSize,currentSize)).withRel("next");
		Link previous=linkTo(methodOn(BankAppController.class).getStatementDeposit(currentOffset-currentSize, currentSize)).withRel("previous");
		List<Transaction> transactions = currentDataSet.getTransactions();
		List<Transaction> currentDataSetList = new ArrayList<Transaction>();
		
		for (int i = currentOffset - 1; i < currentSize + currentOffset - 1; i++) { 
			  if((transactions.size()<=i && i>0) || currentOffset<1) 
				  break;
			Transaction transaction = transactions.get(i);
			currentDataSetList.add(transaction);
			
		}
		CurrentDataSet dataSet = new CurrentDataSet(currentDataSetList, next, previous);
		/*
		 * currentDataSet.setNextLink(next); currentDataSet.setPreviousLink(previous);
		 */
		return new ModelAndView("DepositForm","currentDataSet",dataSet);
	}
	
	@RequestMapping("/statementFundTransfer")
	//@HystrixCommand(fallbackMethod = "errorpage")
	public ModelAndView getStatementFundTransfer(@RequestParam("offset") int offset, @RequestParam("size") int size) {
		CurrentDataSet currentDataSet = restTemplate.getForObject("http://transactions/banktransactions/statements", CurrentDataSet.class);
		int currentSize=size==0?5:size;
		int currentOffset=offset==0?1:offset;
		Link next=linkTo(methodOn(BankAppController.class).getStatementDeposit(currentOffset+currentSize,currentSize)).withRel("next");
		Link previous=linkTo(methodOn(BankAppController.class).getStatementDeposit(currentOffset-currentSize, currentSize)).withRel("previous");
		List<Transaction> transactions = currentDataSet.getTransactions();
		List<Transaction> currentDataSetList = new ArrayList<Transaction>();
		
		for (int i = currentOffset - 1; i < currentSize + currentOffset - 1; i++) { 
			  if((transactions.size()<=i && i>0) || currentOffset<1) 
				  break;
			Transaction transaction = transactions.get(i);
			currentDataSetList.add(transaction);
			
		}
		CurrentDataSet dataSet = new CurrentDataSet(currentDataSetList, next, previous);
		/*
		 * currentDataSet.setNextLink(next); currentDataSet.setPreviousLink(previous);
		 */
		return new ModelAndView("DepositForm","currentDataSet",dataSet);
	}
	 public String errorpage(@ModelAttribute Transaction transaction,
				Model model) {
		    return "errorpage";
		  }
	 public String errorpage(@RequestParam("sender") int senderAccountNumber,@RequestParam("reciever") int reciverAccountNumber,@ModelAttribute Transaction transaction, 
		Model model){
		    return "errorpage";
		  }
	/*
	 * public String errorpage(@RequestParam("offset") int
	 * offset, @RequestParam("size") int size){ return "errorpage"; }
	 */
	
	
}

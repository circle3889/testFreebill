package kr.co.tworld.freebill.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.tworld.freebill.service.FreeBillService;

@RestController
public class FreeBillController {
	
    @Autowired
    private FreeBillService freeBillService;
    
    @RequestMapping(value="/freebill/detail")
    public HashMap getFreeBillDetail(@RequestParam("tokenId") String token){
    	HashMap result = freeBillService.getFreeBillDetail(token);
    	return result;
    } 

	@RequestMapping("/freebill/main")
	public HashMap getFreeBillMain(@RequestParam("tokenId") String token){
		HashMap result = freeBillService.freeBillMain(token);
		return result;
	} 
   
	@RequestMapping("/freebill/hystrixtest")
	public ResponseEntity<String> getHystrixTest(@RequestParam("tokenId") String token){
		ResponseEntity<String> result = freeBillService.getHystrixTest(token);
		return result;
	}
}

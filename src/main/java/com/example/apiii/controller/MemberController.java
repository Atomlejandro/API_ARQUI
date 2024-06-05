package com.example.apiii.controller;

import com.example.apiii.Entity.Member;
import com.example.apiii.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/recharge/{cardNumber}")
    public ResponseEntity<String> rechargeCard(@PathVariable String cardNumber, @RequestParam Double amount) {
        try {
            if (amount < 50000 || amount > 200000) {
                return new ResponseEntity<>("Recharge amount must be between 50000 and 200000", HttpStatus.BAD_REQUEST);
            }
            boolean success = memberService.rechargeCard(cardNumber, amount);
            if (success) {
                return new ResponseEntity<>("Recharge successful", HttpStatus.OK);
            }
            return new ResponseEntity<>("Card not found", HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{cardNumber}")
    public ResponseEntity<String> updateMember(@PathVariable String cardNumber, @RequestBody Member updatedMember) {
        try {
            boolean success = memberService.updateMember(cardNumber, updatedMember);
            if (success) {
                return new ResponseEntity<>("Member updated successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>("Member not found", HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

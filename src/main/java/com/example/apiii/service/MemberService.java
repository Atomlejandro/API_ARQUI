package com.example.apiii.service;

import com.example.apiii.Entity.Member;
import com.example.apiii.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public boolean rechargeCard(String cardNumber, Double amount) {
        Optional<Member> memberOpt = memberRepository.findById(cardNumber);
        if (!memberOpt.isPresent()) {
            throw new IllegalArgumentException("No member found with card number: " + cardNumber);
        }

        Member member = memberOpt.get();
        member.setBalance(member.getBalance() + amount);
        memberRepository.save(member);
        return true;
    }

    public boolean updateMember(String cardNumber, Member updatedMember) {
        Optional<Member> memberOpt = memberRepository.findById(cardNumber);
        if (!memberOpt.isPresent()) {
            throw new IllegalArgumentException("No member found with card number: " + cardNumber);
        }

        Member member = memberOpt.get();
        member.setName(updatedMember.getName());
        member.setCity(updatedMember.getCity());
        member.setCountry(updatedMember.getCountry());
        member.setAge(updatedMember.getAge());
        member.setGender(updatedMember.getGender());
        member.setProfession(updatedMember.getProfession());
        //member.setBalance(updatedMember.getBalance());
        memberRepository.save(member);
        return true;
    }
}
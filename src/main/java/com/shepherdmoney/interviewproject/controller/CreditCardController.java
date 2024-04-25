package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class CreditCardController {

    // TODO: wire in CreditCard repository here (~1 line)
    private final CreditCardRepository creditCardRepository;
    private final UserRepository userRepository;

    public CreditCardController(CreditCardRepository creditCardRepository, UserRepository userRepository) {
        this.creditCardRepository = creditCardRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        // TODO: Create a credit card entity, and then associate that credit card with user with given userId
        //       Return 200 OK with the credit card id if the user exists and credit card is successfully associated with the user
        //       Return other appropriate response code for other exception cases
        //       Do not worry about validating the card number, assume card number could be any arbitrary format and length

        Optional<User> findUser = userRepository.findById(payload.getUserId());
        if (findUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CreditCard newCard = new CreditCard();
        newCard.setNumber(payload.getCardNumber());
        newCard.setIssuanceBank(payload.getCardIssuanceBank());
        newCard.setOwner(findUser.get());
        creditCardRepository.save(newCard);
        findUser.get().getCreditCards().add(newCard);
        userRepository.save(findUser.get());
        return ResponseEntity.ok(newCard.getId());
    }


    @GetMapping("/credit-cards")
    public List<CreditCard> getAllCards() {
        return creditCardRepository.findAll();
    }


    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        // TODO: return a list of all credit card associated with the given userId, using CreditCardView class
        //       if the user has no credit card, return empty list, never return null
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if(findUser.get().getCreditCards().isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        List<CreditCardView> cards = new ArrayList<>();
        findUser.get().getCreditCards().forEach(card -> {
            CreditCardView view = new CreditCardView(card.getNumber(), card.getIssuanceBank());
            cards.add(view);
        });
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        // TODO: Given a credit card number, efficiently find whether there is a user associated with the credit card
        //       If so, return the user id in a 200 OK response. If no such user exists, return 400 Bad Request
        Optional<User> findUser = creditCardRepository.findByNumber(creditCardNumber);
        if (findUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        return ResponseEntity.ok(findUser.get().getId());
    }

    @PostMapping("/credit-card:update-balance")
//    public SomeEnityData postMethodName(@RequestBody UpdateBalancePayload[] payload) {
    public ResponseEntity<String> postMethodName(@RequestBody UpdateBalancePayload[] payload) {
        //TODO: Given a list of transactions, update credit cards' balance history.
        //      1. For the balance history in the credit card
        //      2. If there are gaps between two balance dates, fill the empty date with the balance of the previous date
        //      3. Given the payload `payload`, calculate the balance different between the payload and the actual balance stored in the database
        //      4. If the different is not 0, update all the following budget with the difference
        //      For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
        //      Given a balance amount of {date: 4/11, amount: 110}, the new balanceHistory is
        //      [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 100}]
        //      Return 200 OK if update is done and successful, 400 Bad Request if the given card number
        //        is not associated with a card.
        for(UpdateBalancePayload p : payload) {
            Optional<User> findUser = creditCardRepository.findByNumber(p.getCreditCardNumber());
            Optional<CreditCard> findCard = creditCardRepository.findCardByNumber(p.getCreditCardNumber());
            if (findUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            BalanceHistory newBalance = new BalanceHistory();
            newBalance.setDate(p.getBalanceDate());
            newBalance.setBalance(p.getBalanceAmount());
            findCard.get().getBalanceHistory().add(newBalance);
            creditCardRepository.save(findCard.get());

        }

        return ResponseEntity.ok("Update is done and successfully!");
    }
    
}

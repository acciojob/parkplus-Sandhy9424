package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.SpotType;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation=reservationRepository2.findById(reservationId).get();
        int totalAmount=reservation.getNumberOfHours()*reservation.getSpot().getPricePerHour();
        if(amountSent<totalAmount){
            throw new Exception("Insufficient Amount");
        }
        String modecheck=mode.toUpperCase();
        String upiMode= PaymentMode.UPI.toString().toUpperCase();
        String cashMode=PaymentMode.CASH.toString().toUpperCase();
        String cardMode=PaymentMode.CARD.toString().toUpperCase();
        boolean upi=false;
        boolean card=false;
        boolean cash=false;
        if(modecheck.equals(upiMode)){
            upi=true;
        }
        if(modecheck.equals(cardMode)){
            card=true;
        }
        if(modecheck.equals(cashMode)){
            cash=true;
        }
                if(upi==false&&card==false&&cash==false) {
                     throw new Exception("Payment mode not detected");
                }
                    Payment payment=new Payment();
                   if(upi) {
                       payment.setPaymentMode(PaymentMode.UPI);
                   } else if (cash) {
                       payment.setPaymentMode(PaymentMode.CASH);
                   }
                   else {
                       payment.setPaymentMode(PaymentMode.CARD);
                   }
                   payment.setPaymentCompleted(true);
                   payment.setReservation(reservation);
                   reservation.setPayment(payment);
                   reservationRepository2.save(reservation);
                   return payment;
    }
}

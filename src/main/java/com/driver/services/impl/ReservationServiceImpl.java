package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        Reservation reservation=new Reservation();
                     reservation.setNumberOfHours(timeInHours);
        ParkingLot parkingLot;
        User user;
                     try {
                          parkingLot = parkingLotRepository3.findById(parkingLotId).get();
                         user = userRepository3.findById(userId).get();
                     }catch (Exception e){
                         throw new Exception("Cannot make reservation");
                     }
                     List<Spot>spotList=parkingLot.getSpotList();
                     Spot minSpot=null;
                     int minPrice=Integer.MIN_VALUE;
                     for(Spot spot:spotList){
                         if(spot.isOccupied()==false){
                             int noOfwheels;
                             if(spot.getSpotType().compareTo(SpotType.TWO_WHEELER)==0){
                                 noOfwheels=2;
                             } else if (spot.getSpotType().compareTo(SpotType.FOUR_WHEELER)==0) {
                                 noOfwheels=4;
                             }
                             else{
                                noOfwheels=50;
                             }
                             if(spot.getPricePerHour()<minPrice&&noOfwheels>=numberOfWheels){
                                 minPrice= spot.getPricePerHour();
                                 minSpot=spot;
                             }
                         }
                     }
                     if(minSpot==null){
                         throw new Exception("Cannot make reservation");
                     }
                     minSpot.setOccupied(true);
                     reservation.setSpot(minSpot);
                     reservation.setUser(user);
        List<Reservation>reservationList=user.getReservationList();
        reservationList.add(reservation);
        user.setReservationList(reservationList);
        List<Reservation>reservationList1=minSpot.getReservationList();
        reservationList1.add(reservation);
        minSpot.setReservationList(reservationList1);
        userRepository3.save(user);
        spotRepository3.save(minSpot);
        return reservation;
    }
}

package com.care.boot.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.care.boot.member.MemberDTO;

import java.time.LocalDateTime;

@Service
public class TicketService {

    @Autowired
    private TicketMapper ticketMapper;

    // ✅ 콘서트 ID 고정 (예: 첫 콘서트 ID = 1)
    private final int DEFAULT_CONCERT_ID = 1;

    // 콘서트당 현재 티켓 수 조회
    public int getCurrentTicketCount(int concertId) {
        return ticketMapper.countTicketsByConcertId(concertId);
    }

    // ✅ MemberService에서 호출할 수 있도록 concertId 없이 호출되는 메서드 추가
    public boolean reserve(MemberDTO member) {
        return bookTicket(member, DEFAULT_CONCERT_ID);
    }

    // 예매 처리
    public boolean bookTicket(MemberDTO member, int concertId) {
        int currentCount = getCurrentTicketCount(concertId);

        // 티켓 번호 할당
        int ticketNumber;
        if ("VIP".equalsIgnoreCase(member.getMembership())) {
            ticketNumber = member.getVipNumber(); // VIP는 고정 번호
        } else if (currentCount >= 101 && currentCount <= 5000) {
            ticketNumber = currentCount;
        } else {
            return false; // 티켓 없음
        }

        // TicketHolderDTO 구성
        TicketHolderDTO ticket = new TicketHolderDTO();
        ticket.setConcertId(concertId);
        ticket.setId(member.getId());
        ticket.setUserName(member.getUserName());
        ticket.setMobile(member.getMobile());
        ticket.setMembership(member.getMembership());
        ticket.setTicketNumber(ticketNumber);
        ticket.setReservedAt(LocalDateTime.now()); // ✅ 예약 시간 추가

        // 예약 로그용 DTO
        ReservationDTO reservation = new ReservationDTO();
        reservation.setConcertId(concertId);
        reservation.setId(member.getId());
        reservation.setEventType("booking");
        reservation.setReservedAt(LocalDateTime.now());

        // INSERT 실행
        ticketMapper.insertTicket(ticket);
        ticketMapper.insertReservation(reservation);

        return true;
    }
}

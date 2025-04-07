package com.care.boot.ticket;

import com.care.boot.member.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketMapper ticketMapper;

    private final int DEFAULT_CONCERT_ID = 1;

    // ✅ 콘서트 전체 목록 조회 (티케팅 페이지용)
    public List<ConcertDTO> getAllConcerts() {
        return ticketMapper.selectAllConcerts();
    }

    // ✅ 현재 콘서트 예매 인원 수 조회
    public int getCurrentTicketCount(int concertId) {
        return ticketMapper.countTicketsByConcertId(concertId);
    }

    // ✅ 외부에서 MemberDTO만으로 호출할 수 있는 기본 예매 로직
    public boolean reserve(MemberDTO member) {
        return reserveTicket(member, DEFAULT_CONCERT_ID);
    }

    // ✅ 컨트롤러에서 호출하는 용도 (id로 MemberDTO 조회해서 처리)
    public boolean reserveTicket(int concertId, String userId) {
        MemberDTO member = ticketMapper.findRegularMemberById(userId);

        if (member == null) {
            member = ticketMapper.findVipMemberById(userId);
        }

        if (member == null) {
            return false;
        }

        return reserveTicket(member, concertId);
    }

    // ✅ 실제 예매 처리 로직
    public boolean reserveTicket(MemberDTO member, int concertId) {
        int currentCount = getCurrentTicketCount(concertId);
        if (currentCount >= 5000) {
            return false; // 좌석 초과
        }

        int ticketNumber;

        if ("VIP".equalsIgnoreCase(member.getMembership())) {
            if (member.getVipNumber() > 0 && member.getVipNumber() <= 100) {
                ticketNumber = member.getVipNumber(); // VIP는 고정 번호 사용
            } else {
                return false; // 유효하지 않은 VIP 번호
            }
        } else {
            Integer lastNumber = ticketMapper.getLastTicketNumber(concertId);
            ticketNumber = (lastNumber == null || lastNumber < 100) ? 101 : lastNumber + 1;
        }

        // 🎯 concertName 조회
        ConcertDTO concert = ticketMapper.selectAllConcerts();
        ConcertDTO selectConcertById(int concertId);

      
        String concertName = (concert != null) ? concert.getName() : "알 수 없음";

        // ✅ 예매 객체 생성
        TicketHolderDTO ticket = new TicketHolderDTO();
        ticket.setConcertId(concertId);
        ticket.setConcertName(concertName);                   // 추가된 필드
        ticket.setId(member.getId());
        ticket.setUserName(member.getUserName());
        ticket.setMobile(member.getMobile());
        ticket.setEmail(member.getEmail());                   // 추가된 필드
        ticket.setMembership(member.getMembership());
        ticket.setTicketNumber(ticketNumber);
        ticket.setReservedAt(LocalDateTime.now());

        // ✅ 예매 로그 객체 생성
        ReservationDTO reservation = new ReservationDTO();
        reservation.setReservationId(System.currentTimeMillis());
        reservation.setConcertId(concertId);
        reservation.setId(member.getId());
        reservation.setEventType("booking");
        reservation.setReservedAt(LocalDateTime.now());

        // ✅ DB 등록
        ticketMapper.insertTicket(ticket);
        ticketMapper.insertReservation(reservation);

        return true;
    }
}


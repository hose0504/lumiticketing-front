package com.care.boot.ticket;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface TicketMapper {
    // 티켓 예매 등록 (TicketHolder)
    void insertTicket(TicketHolderDTO ticket);

    // 예매 로그 기록 (Reservation)
    void insertReservation(ReservationDTO reservation);

    // 특정 콘서트의 현재 예매 수 조회
    int countTicketsByConcertId(int concertId);

    // 콘서트별 마지막 티켓 번호 조회 (예: 일반회원 번호 할당 시 참고)
    Integer getLastTicketNumber(int concertId);

    // 유저별 예매 내역 조회
    List<TicketHolderDTO> getTicketsByUserId(String id);
}

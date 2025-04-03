package com.care.boot.ticket;

import com.care.boot.member.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TicketMapper {
    // 🎟 티켓 예매 등록
    void insertTicket(TicketHolderDTO ticket);

    // 🗓 예매 로그 기록
    void insertReservation(ReservationDTO reservation);

    // 🔍 특정 콘서트의 예매 수 조회
    int countTicketsByConcertId(int concertId);

    // 📌 콘서트별 마지막 티켓 번호 조회
    Integer getLastTicketNumber(int concertId);

    // 🔎 유저별 예매 내역 조회
    List<TicketHolderDTO> getTicketsByUserId(String id);

    // ✅ 콘서트 전체 목록 조회 (드롭다운용)
    List<ConcertDTO> selectAllConcerts();

    // ✅ 유저 ID로 Regular/VIP 통합 조회 (예매 시 MemberDTO 확보)
    MemberDTO findMemberById(String id);
}

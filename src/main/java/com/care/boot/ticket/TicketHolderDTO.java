package com.care.boot.ticket;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TicketHolderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long reservationId;
    private int concertId;
    private String id;              // 회원 ID
    private String userName;
    private String mobile;
    private String membership;      // "VIP" or "Regular"
    private LocalDateTime reservedAt;
    private int ticketNumber;

    // 기본 생성자
    public TicketHolderDTO() {}

    // 전체 필드 생성자
    public TicketHolderDTO(Long reservationId, int concertId, String id, String userName, String mobile,
                           String membership, LocalDateTime reservedAt, int ticketNumber) {
        this.reservationId = reservationId;
        this.concertId = concertId;
        this.id = id;
        this.userName = userName;
        this.mobile = mobile;
        this.membership = membership;
        this.reservedAt = reservedAt;
        this.ticketNumber = ticketNumber;
    }

    // Getter & Setter
    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public int getConcertId() { return concertId; }
    public void setConcertId(int concertId) { this.concertId = concertId; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getMembership() { return membership; }
    public void setMembership(String membership) { this.membership = membership; }

    public LocalDateTime getReservedAt() { return reservedAt; }
    public void setReservedAt(LocalDateTime reservedAt) { this.reservedAt = reservedAt; }

    public int getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(int ticketNumber) { this.ticketNumber = ticketNumber; }
}

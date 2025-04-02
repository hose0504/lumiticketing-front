package com.care.boot.member;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMemberMapper {

    ArrayList<MemberDTO> memberInfo(@Param("begin") int begin, @Param("end") int end,
                                    @Param("select") String select, @Param("search") String search);

    int countVIPMembers();

    int registProc(MemberDTO member);

    MemberDTO login(@Param("id") String id);

    int updateProc(MemberDTO member);

    int deleteProc(@Param("id") String id);

    List<Integer> getAllVIPNumbers();

    int promoteToVIP(@Param("id") String id, @Param("vipNumber") int vipNumber);

    void removeFromRegular(@Param("id") String id);

    int upgradeToVIP(@Param("id") String id, @Param("vipNumber") int vipNumber);

    MemberDTO getMemberById(@Param("id") String id);

    int getNextTicketNumber();

    int insertTicketHolder(@Param("id") String id, @Param("userName") String userName,
                           @Param("mobile") String mobile, @Param("membership") String membership,
                           @Param("ticketNumber") int ticketNumber);

    int getNextVipTicketNumber();

    int getNextRegularTicketNumber();

    List<MemberDTO> getRegularMembers();
}

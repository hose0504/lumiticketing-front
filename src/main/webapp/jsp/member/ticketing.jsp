<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:import url="/header" />

<c:if test="${empty sessionScope.id}">
    <script>
        alert("로그인을 해주세요!");
        location.href = "${pageContext.request.contextPath}/login";
    </script>
</c:if>

<div align="center">
    <h2>🎫 콘서트 예매</h2>

    <form id="ticketForm">
        <table>
            <tr>
                <td>
                    <c:if test="${not empty concertList}">
                        <label for="concert">콘서트 선택:</label>
                        <select name="concertId" id="concert" required>
                            <option value="" disabled selected>콘서트를 선택하세요</option>
                            <c:forEach var="concert" items="${concertList}">
                                <option value="${concert.concertId}">
                                    ${concert.name} (${concert.date})
                                </option>
                            </c:forEach>
                        </select>
                    </c:if>
                    <c:if test="${empty concertList}">
                        <p style="color: red;">콘서트 목록을 불러올 수 없습니다. 관리자에게 문의하세요.</p>
                    </c:if>
                </td>
            </tr>

            <tr>
                <td align="center">
                    <br>
                    <input type="button" value="예매하기" onclick="goPayment()">
                    <input type="button" value="메인으로가기" onclick="location.href='index'">
                </td>
            </tr>

            <c:if test="${not empty msg}">
                <tr>
                    <td align="center" style="color: green;">${fn:escapeXml(msg)}</td>
                </tr>
            </c:if>
        </table>
    </form>
</div>

<script>
    function goPayment() {
        const concertId = document.getElementById("concert").value;
        if (!concertId) {
            alert("콘서트를 선택해주세요.");
            return;
        }
        location.href = "${pageContext.request.contextPath}/goPayment?concertId=" + concertId;
    }
</script>

<c:import url="/footer" />


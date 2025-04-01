<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>index</title>
<script>
    // 회원가입 성공 메시지가 존재하면 팝업을 띄움
   window.onload = function() {
        var logoutMsg = "${logoutMessage}";
        var vipUpgradeMsg = "${vipUpgradeMessage}";

        if (logoutMsg) {
            alert("✅ 로그아웃되었습니다!");
        }
        
        if (vipUpgradeMsg) {
            alert("🎉 VIP로 등업되었습니다!");
        }
    };
</script>
</head>
<body>
	<c:import url="/header"/>
	<c:import url="/main"/>
	<div align="center">
	</div>
	<c:import url="/footer"/>
</body>
</html>










<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change Password</title>
</head>
<body>
	<jsp:include page="header.jsp"/>
	<jsp:include page="footer.jsp"/>

	<div class="container">
		<div class="row">
			<div class="col-xs-6">
				<div style="padding: 10px">
					<h2>Change Password</h2>
					<form id="loginForm" method="post" class="loginForm form-horizontal" action="ResetPassword" name="changePass">
						<div class="form-group">
							<input class="form-control" type="password" name="oldPassword"
								placeholder="Old Password"></input><br>
							<input class="form-control" type="password" name="newPassword"
								placeholder="New Password"></input><br>
							<input class="form-control" type="password" name="reNewPassword"
								placeholder="New Password Again"></input><br>
							<button id="loginButton" type="submit" class="btn btn-default">Reset</button>
						</div>
						<input type="hidden" name="func" value="resetPassword" />
					</form>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade bs-example-modal-sm" id="errorPop" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel"></h4>
				</div>
				<div class="modal-body">
					<%=
					request.getAttribute("message")
					%>
				</div>
				<div class="modal-footer">

					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" name="error" value="<%=request.getAttribute("error")%>" />						
</body>
</html>
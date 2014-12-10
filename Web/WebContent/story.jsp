<%@ page language="java" contentType="text/html; charset=ISO-8859-9"
	pageEncoding="ISO-8859-9"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dutluk</title>

</head>
<body>
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />
	<%@ page import="Dutluk.*"%>
	<%@ page import="java.sql.*, Dutluk.DatabaseService"%>
	<%
	HttpSession newSession = request.getSession();
	String storyId = request.getParameter("storyId");
	if(newSession == null)
	{
		request.getRequestDispatcher("loginRegister.jsp").forward(request,response);
	}else if(storyId == null || storyId.equals(""))
		request.getRequestDispatcher("index.jsp").forward(request, response);
	else if(newSession.getAttribute("email") == null)	
	{
		newSession.setAttribute("redirect", "story.jsp");
		newSession.setAttribute("var", "storyId");
		newSession.setAttribute("Id", storyId);
		request.getRequestDispatcher("loginRegister.jsp").forward(request, response);
	}
		DatabaseService db = new DatabaseService();
		
		Story story = db.findStorybyStoryId(Integer.parseInt(storyId));
		User user = db.findUserByUserId(story.getUserId());
		User currentUser = db.findUserByEmail(request.getSession().getAttribute("email").toString());
		int rate = db.getRate(currentUser.getUserID(), story.getStoryId());
		newSession.setAttribute("StoryID", storyId);
		newSession.setAttribute("UserID", currentUser.getUserID());
        	%>

	<div id="nav">
		recommendation<br> falan<br> filan<br>
	</div>
	<div id="story">
		<center>
			<h2>Story Name</h2>
			<h4>
				Written by <a href='profile.jsp?id=<%= story.getUserId() %>'> <%= user.getName() %>
				</a>
			</h4>
		</center>
		<p>
			<%=story.getContent()%>
			<br>
			<br>
			Your current rate is: <%=rate %>
			<%
				if(rate == 0)
				{
				%>
				<br>
			Rate Story:
		<form method="post" action="RateStory">
			<select name="rate">
				<option value="1">Terrible</option>
				<option value="2">Not good</option>
				<option value="3">It's OK</option>
				<option value="4">Good</option>
				<option value="5">Great!</option>
			</select> <input type="submit" value="Rate">
		</form>

	</div><br>

	<%
				}
				%><br>Average Rate for this story is: <%= story.getAvgRate()%><br>
	<%
	
	        
	
		Boolean isRemembered = db.isRemembered(currentUser.getUserID(), Integer.parseInt(storyId));
		if(isRemembered)
		{
			%>
			<form id="rememberForm" method="post" action="RememberStory"
						class="form-horizontal">
			<input type="hidden" name="funct" value="dontRemember"/> 
			<button type="submit" class="btn btn-default">I don't Remember</button><br><br>
			</form>
			<%
		}
		else
		{
			%>
			<form id="rememberForm" method="post" action="RememberStory"
						class="form-horizontal">
			<input type="hidden" name="funct" value="remember"/> 
			<button type="submit" class="btn btn-default">I Remember That!</button><br><br>
			</form>
			<%
		}
	%>
	
	
	<table style="width: 100%" border="1">
			<col style="width: 5%">
			<col style="width: 1%">
			<col style="width: 1%">
	<tr>
    	<th>Comment</th>
    	<th>Commenter</th>
    	<th>Date</th>
   	</tr>
	<% 
	db = new DatabaseService();
	ResultSet rs = null;
	ResultSet rs2 = null;
	int commenterId = 0;
	String commenterName = null;
	try
	{
		Connection connection = db.getConnection();
        Statement statement = connection.createStatement() ;
        rs =statement.executeQuery("SELECT * FROM Comments WHERE StoryID = '"+storyId+"' AND IsDeleted = 0");
        while(rs.next())
        {
        	commenterId = rs.getInt(3);
        	Statement statement2 = connection.createStatement();
        	rs2 = statement2.executeQuery("SELECT * FROM Users Where UserID = '"+commenterId+"'");
        	if(rs2.next())
        	{
        		commenterName = rs2.getString(2);
        	}
        	%>
        	
        	<tr>
        	<td><%= rs.getString(4) %></td>
        	<td><%= commenterName %></td>
        	<td><%= rs.getDate(7) %></td>
        	</tr>

        	<%
        }
	}
	catch(Exception e)
	{
		out.print(e);
	}
	%>
	</table><br>
	
	<form method="post" action="Comment" class="loginform form-horizontal">
		<div class="form-group">
			<input class="form-control" name="comment" type="text" placeholder="Your Comment:"/>
			<button type="submit" class="btn btn-default">Submit Comment</button>
		</div>
	</form>
</body>
</html>
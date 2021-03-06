<%@ page language="java" contentType="text/html; charset=ISO-8859-9"
	pageEncoding="ISO-8859-9"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dutluk</title>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&key=AIzaSyCWz0T_U1zwxdI3CKepXVdlSS5iHFJste4"></script>
<script>

	var map;
	function initialize() {
		var lat = document.getElementById("latValue").value;
		var lon = document.getElementById("lonValue").value;
		lat = lat.replace("\/","");
		lon = lon.replace("\/","");
	  var mapOptions = {
	    zoom: 13,
	    center: new google.maps.LatLng(parseFloat(lat), parseFloat(lon))
	  };
	  map = new google.maps.Map(document.getElementById('map-canvas'),
	      mapOptions);
	  var marker = new google.maps.Marker({
	      position: new google.maps.LatLng(parseFloat(lat), parseFloat(lon)),
	      map: map
	  });
	}
	
	google.maps.event.addDomListener(window, 'load', initialize);

</script>
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
		int storyID = Integer.parseInt(storyId);
		Story story = db.findStorybyStoryId(storyID);
		User user = db.findUserByUserId(story.getUserId());
		User currentUser = db.findUserByEmail(request.getSession().getAttribute("email").toString());
		int rate = db.getRate(currentUser.getUserID(), story.getStoryId());
		newSession.setAttribute("StoryID", storyId);
		newSession.setAttribute("UserID", currentUser.getUserID());
		
		String storyDate="";
		if(story.getAbsoluteDate()==null)
			storyDate=story.getApproximateDate();
		else storyDate=story.getAbsoluteDate().toString();
		
		String placeName="";
		int placeID=0;
        String picPath="";
        String[] tags = new String[10];
        int rememberCount=0,numofVotes=0;
        double averageRate=0.0;
        

		Place place = null;
		try{
		
			//Get the place id and name
			ResultSet rs = null;
			Connection connection = db.getConnection();
	        Statement statement = connection.createStatement() ;
	        rs =statement.executeQuery("SELECT Places.PlaceID, Places.Name FROM Places,StoriesInPlaces WHERE Places.PlaceID=StoriesInPlaces.PlaceID AND StoriesInPlaces.StoryID='"+storyID+"';");
	        
	        while(rs.next())
	        {
	        	placeID = rs.getInt(1);
	        	placeName = rs.getString(2);
	        	
	        }
	        
	        place = db.findPlacebyPlaceId(placeID);
        
        
        //Get the picture of the story if exist
	        rs = statement.executeQuery("SELECT Path FROM Pictures,PicturesInStories WHERE StoryID='"+storyID+"' AND  Pictures.PicID=PicturesInStories.PicID LIMIT 1;");
	        
			if(rs.next()){
				picPath = rs.getString(1);
			}
        
		
		//Get tags of the story
			rs = statement.executeQuery("SELECT Name FROM Tags, TagsInStories, Stories WHERE Tags.TagID=TagsInStories.TagID AND TagsInStories.StoryID=Stories.StoryID AND Stories.StoryID='"+storyID+"' LIMIT 10;");
			
			for(int i=0;rs.next();i++){
				tags[i]=rs.getString(1);
			}
			
		//Get the number of people remembers the story
			rs = statement.executeQuery("SELECT COUNT(*) FROM IRememberThat WHERE StoryID='"+storyID+"';");
			if(rs.next())
				rememberCount=rs.getInt(1);
		
		//Get average rate and number of voters
			rs = statement.executeQuery("SELECT COUNT(*), ROUND(AVG(Rate),2) FROM Rate WHERE StoryID='"+storyID+"';");
			if(rs.next()){
				numofVotes = rs.getInt(1);
				averageRate = rs.getDouble(2);
			}
			
			statement.close();
			connection.close();
        
		}catch(Exception e)
	    {
	        out.println(e);
	        
	    }
      
	
	%>
	
	<div class="container">
		<div class="row">
			<div class="col-sm-7">
			<div id="story">
				<h4>
				
					A story for <a href='timeline.jsp?Id=<%= placeID %>'><%=placeName %></a> at <%=storyDate %>
				by <a href='profile.jsp?id=<%= story.getUserId() %>'> <%= user.getName() %> </a></h4>
				
				<% if(picPath!=""){ %>
					<img src="http://titan.cmpe.boun.edu.tr:8085/image/<%=picPath%>" width=300>
					 
				<% }
				 %><br>tags: <%
				for(int i=0;i<10&tags[i]!=null;i++)
					out.println(tags[i]+" ");
				%>
				<br>
				
				
				
			<p>
				<h3><%=story.getContent()%></h3>
				
			
				
				<br>
				
				<%
			if(currentUser.getUserID() != story.getUserId()){ //if someone else's story
				if(averageRate>0)
					out.println("Average Rate is "+averageRate+" by "+numofVotes+" people");
				else
					out.println("Be the first one to rate!");
				%>
				<br><br>
		<%
		
		    if(rememberCount>0)
		    	out.println(rememberCount+" people remembers!");
		    else
		    	out.println("Be the first one to remember!");
		
			Boolean isRemembered = db.isRemembered(currentUser.getUserID(), Integer.parseInt(storyId));
			if(isRemembered)
			{
				%>
				<br><br>
				<form id="rememberForm" method="post" action="RememberStory"
							class="form-horizontal">
				<input type="hidden" name="funct" value="dontRemember"/> 
				<button type="submit" class="btn btn-default">I don't Remember</button>
				</form>
				<%
			}
			else
			{
				%>
				<form id="rememberForm" method="post" action="RememberStory"
							class="form-horizontal">
				<input type="hidden" name="funct" value="remember"/> 
				<button type="submit" class="btn btn-default">I Remember That!</button>
				</form>
				<%
			}
			
			Boolean isReported = db.isReported(currentUser.getUserID(), Integer.parseInt(storyId));
			if(!isReported)
			{
				%>
				<br><br>
				<form id="reportForm" method="post" action="ReportStory"
							class="form-horizontal">
				<input type="hidden" name="funct" value="report"/> 
				<button type="submit" class="btn btn-default">Report Story</button>
				</form>
				<%
			}
		%>
		
		
				<%
					if(rate == 0)
					{
					%>
					<br>
			<form method="post" action="RateStory">
				<select name="rate">
					<option value="1">Terrible</option>
					<option value="2">Not good</option>
					<option value="3">It's OK</option>
					<option value="4">Good</option>
					<option value="5">Great!</option>
				</select> <input type="submit" class="btn btn-default" value="Rate">
			</form>
	
		
	
		<%
					}
				}else{ //if logged in user's story
					
					if(averageRate>0)
						out.println("Average Rate is "+averageRate+" by "+numofVotes+" people");
					else
						out.println("Nobody rated this story yet.");
					%>
					<br><br>
			<%
			
			    if(rememberCount>0)
			    	out.println(rememberCount+" people remembers!");
			    else
			    	out.println("Nobody remembered this story yet.");
			
				}
				
				
					%>
		</div>
			</div>
			<div class="col-sm-5">
				<div id="map-container">
					<div id="map-canvas"></div>
				</div>
				<input type="hidden" id="latValue" value=<% out.print(place.getLatitude()); %>/>
				<input type="hidden" id="lonValue" value=<% out.print(place.getLongtitude()); %>/>
			</div>
		
		</div>
		<div >
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
			        statement.close();
			        connection.close();
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
		</div>
	</div>
	<div class="modal fade bs-example-modal-sm editSuccess"
		tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
		aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">

				<div class="modal-body">Your comment is successfully
					added!</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Ok</button>
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
					<h4 class="modal-title" id="myModalLabel">Error</h4>
				</div>
				<div class="modal-body">Some error occurred.</div>
				<div class="modal-footer">

					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" name="success"
		value="<%=request.getAttribute("success")%>" />
	<input type="hidden" name="error"
		value="<%=request.getAttribute("error")%>" />
</body>
</html>
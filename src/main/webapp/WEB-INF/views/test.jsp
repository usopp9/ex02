<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
	<h1>Ajax Test Page</h1>
	<div>
		<div>
			bno <input type="text" name="bno" id="bno">
		</div>
		<div>
			replyer <input type="text" name="replyer" id="replyer">
		</div>
		<div>
			replytext <input type="text" name="replytext" id="replytext">
		</div>
		<button id="addReplyBtn">add Reply</button>
		<button id="getListBtn">get List All</button>
	</div>	
	
	<hr>
	<ul id="replies">
		
	</ul>
	
	
			 <!-- Modal -->
  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">수정</h4>
        </div>
        <div class="modal-body">
          <div class="form-group">
			  <label for="rno">게시번호</label>
			  <input type="text" class="form-control" id="rno">
		</div>
		<div class="form-group">
		  <label for="reply">내용:</label>
		  <textarea class="form-control" rows="5" id="reply"></textarea>
		</div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal" id="uBtn">수정</button>   
        </div>
      </div>    
    </div>
  </div>

	
	
	
	
	
	<script type="text/javascript">
		$("#addReplyBtn").click(function(){
			var bnoVal = $("#bno").val();
			var replyerVal = $("#replyer").val();
			var replytextVal = $("#replytext").val();
			
			var sendData = {bno:bnoVal, replyer:replyerVal, replytext:replytextVal};
			//이형태는 ?bno=bnoVal&replyer=........
							
			//@requestBody, JSON.stringify, headers-Content-Type
			$.ajax({
				type:"post",
				/* url:"/ex02/replies" */
				url:"${pageContext.request.contextPath}/replies",
				data: JSON.stringify(sendData), //json 형태로 바꿔줌
				dataType:"text",//xml,text,json
				headers:{"Content-Type":"application/json"},
				success:function(result){
					console.log(result);
				}
			})
		})
		
		$("#getListBtn").click(function(){
			var bnoVal = $("#bno").val();
			
			$.ajax({
				type:"get",
				url:"${pageContext.request.contextPath}/replies/all/"+bnoVal,
				dataType:"json",
				success:function(result){
					console.log(result);
					 $("#replies").empty();
					 $(result).each(function(i,obj){ 
	                        var $li = $("<li>");   
	                        var $upbtn = $("<button type='button' class='btn btn-default' data-toggle='modal' data-target='#myModal'>").html("수정");
	                        var $debtn = $("<button  type='button' class='btn btn-default' value='dBtn'>").html("삭제");      
	                          
	                        var $span = $("<span class='rno'>").append(obj.rno);          
	                        
	                        $li.append($span).append(", "+obj.replytext+", "+obj.replyer).append($upbtn).append($debtn); 
	                        $("#replies").append($li);    
	                   })			
				}
			})		
		})
		
		   
		 $(document).on("click","button[value='dBtn']",function(){   
			 var rno = $(this).parent().find(".rno").html();          
			 
			 var removeno = $(this).parent();
			$.ajax({
				type:"DELETE",
				url:"${pageContext.request.contextPath}/replies/"+rno,   
				dataType:"text",
				success:function(result){
					console.log(result);
					if(result=="success"){
						alert("삭제되었습니다.");
						removeno.remove();       
					}
				}	
			})
			
        })	
        
        
        $("#uBtn").click(function(){
        	var rno = $("#rno").val();    
        	var rcontext = $("#reply").val();      
        	var sendData = {replytext:rcontext};   
        	
        	$.ajax({
				type:"PUT",     
				url:"${pageContext.request.contextPath}/replies/"+rno,   
				data: JSON.stringify(sendData),  
				dataType:"text",  
				headers:{"Content-Type":"application/json"},     
				success:function(result){
					console.log(result);
					$("#getListBtn").trigger("click");   
					 $("#rno").val("");         
					 $("#reply").val("");     
				}	
			})
        	
        })
	</script>
	
</body>
</html>
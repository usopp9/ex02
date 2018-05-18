<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#previewBox{
		width: 400px;
		height: 300px;
		border: 1px dotted gray;
	}
	#previewBox img{
		width: 100px;
	}
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<body>
	<form action="previewUpload" method="post" enctype="multipart/form-data">
		<input type="text" name="writer" placeholder="작성자이름">
		<input type="file" name="file" id="file">
		<input type="submit">
	</form>	
	
	<div id="previewBox">
	
	</div>
	<script type="text/javascript">
		$("#file").change(function(){
			$("#previewBox").empty();   
			
			var reader = new FileReader();
			reader.onload = function(e){
				var imgObj = $("<img>").attr("src",e.target.result);
				$("#previewBox").html(imgObj);
			}  
			reader.readAsDataURL($(this)[0].files[0]); 
			
			/* $(this)[0] ==document.getElementById("fiel"); */
					
		})
	</script>
</body>
</html>
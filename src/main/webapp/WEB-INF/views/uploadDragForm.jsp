<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#dropBox{
		width: 400px;
		height: 300px;
		border: 1px dotted gray;
	}
	#dropBox img{
		width: 100px; 
	}
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<body>
	<form action="dragUpload" method="post" enctype="multipart/form-data" id="f1">
		<input type="text" name="writer" placeholder="작성자이름">
		<input type="submit">
	</form>	
	
	<div id="dropBox">
	
	</div>
	<script type="text/javascript">
		
		var formData = new FormData(); //ajax로 파일 데이터를 보낼때 주로 사용함. form안의 data를 보내는 형식
	
	$("#dropBox").on("dragenter dragover",function(event){
		event.preventDefault();           
	})
		//event안에 드래그 앤 드랍된 파일 정보가 들어있음
		$("#dropBox").on("drop",function(event){
			event.preventDefault();
			var files = event.originalEvent.dataTransfer.files;
			var file = files[0];
			console.log(file);
			
			var reader = new FileReader();
			reader.addEventListener("load",function(){
				var imgObj = $("<img>").attr("src",reader.result);
				$("#dropBox").append(imgObj);  
			})
			if(file){
				reader.readAsDataURL(file); //load실행  
			}
			
			formData.append("files",file);//여러개 추가를 위해 key이름은 files로 함
		})
		
		$("#f1").submit(function(e){
			e.preventDefault();
			
			var writer = $("input[name='writer']").val();
			formData.append("writer",writer);
			
			$.ajax({  
				url:"dragUpload",   
				type:"post",
				data:formData,
				processData:false,  //processData:false, contentType:false: file을 ajax로 업로드할때 처리필요
				contentType:false,
				success:function(data){
					console.log(data);
					if(data.result=="success"){
						$("#dropBox").empty(); 
						
						$(data.listFile).each(function(i,obj){  
							  
							$img= $("<img>").attr("src","displayFile?filename="+obj);  
							$("#dropBox").append($img); 
						})
					}
				}			      
			})
		});
	</script>
</body>
</html>
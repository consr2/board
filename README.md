<img src="https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=header&text=FreeBoard&fontSize=90" />


<div align="center">
  <img src="https://img.shields.io/badge/Spring-Green?style=flat&logo=Spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/Java-orange?style=flat&logo=Java&logoColor=white"/>
</div>

<div align="center">
  <img src="https://img.shields.io/badge/HTML5-orange?style=flat&logo=HTML5&logoColor=white"/>
  <img src="https://img.shields.io/badge/CSS3-blue?style=flat&logo=CSS&logoColor=white"/>
  <img src="https://img.shields.io/badge/JavaScript-yellow?style=flat&logo=JavaScript&logoColor=white"/>
  <img src="https://img.shields.io/badge/jQuery-blue?style=flat&logo=jQuery&logoColor=white"/>
</div>

<div align="center">
  <img src="https://img.shields.io/badge/MySQL-skyblue?style=flat&logo=MySQL&logoColor=white"/>
</div>

<div align="center">
  <img src="https://github-readme-stats.vercel.app/api/top-langs/?username=consr2&layout=compact"><br><br>
  <img src="https://github-readme-stats.vercel.app/api?username=consr2&show_icons=true">
</div>

--------------------------------------------------------------------------------------------
**1. 프로젝트 버전**    
spring-boot : 2.7.5  
java        : 17   
mysql       : 8.0.29  
lombok      : 1.18.24  


**2. 참고 출처**    
<점프 투 스프링부트>  
https://wikidocs.net/book/7601  


**3. 프로젝트 설명**   
토이 프로젝트의 일환으로 나만의 자료실을 만들어 보았습니다.  


이 프로젝트의 메인 페이지 입니다.
<div>
  <img src="https://user-images.githubusercontent.com/110438208/206104542-8e63bae4-2048-4060-80ef-5cfa8e7e119b.png" width="400" height="250">
</div>

  <br/><br/>

**회원가입** 기능이 있으며 **유저**와 **admin**으로 구분 됩니다.  
<div>
  <img src="https://user-images.githubusercontent.com/110438208/206104377-186df842-e2f7-47ab-898d-22d45654399c.png" width="150" height="300">
  <img src="https://user-images.githubusercontent.com/110438208/206104423-0bf21c09-817c-42de-bf83-f8b665449e40.png"width="150" height="300">
</div>

  <br/><br/>
  
**게시글 작성 페이지**  
**카테고리 선택,제목, 내용, 파일 첨부**칸이 있습니다. 내용부분은 **summernote**를 가져왔습니다.
<div>
  <img src="https://user-images.githubusercontent.com/110438208/206107985-c1169a6a-3920-4779-acfd-2272d1ddf091.png" width="400" height="250">
</div>


```  
<meta name="_csrf" th:content="${_csrf.token}"/>  
<meta name="_csrf_header" th:content="${_csrf.headerName}"/>  
    
    function uploadSummernoteImageFile(file, editor) {
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		console.log(token)
		data = new FormData();
		data.append("file", file);
		$.ajax({
			data : data,
			type : "POST",
			url : "/question/summernote",
			contentType : false,
			processData : false,
			beforeSend : function(xhr){
				xhr.setRequestHeader(header, token);
			},
			success : function(data) {
            	//항상 업로드된 파일의 url이 있어야 한다.
				$(editor).summernote('insertImage', data);
			}
		});
	}
```  


**시큐리티 사용 시 ajax로 파일을 보내려면 csrf토큰을 해더에 추가해 줘야 합니다.**  

  <br/><br/>

  게시글의 상세 페이지 입니다.  
  
<div>
	<img src="https://user-images.githubusercontent.com/110438208/206110439-99d6a4e7-e5a0-495b-a0d8-d57156e12040.png" width="400" height="250">
</div>

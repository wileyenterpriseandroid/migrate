<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" session="false"%>
<head>
<style type="text/css">
  .divTable
    {
        display:  table;
        width:auto;
        background-color:#eee;
        border:1px solid  #666666;
        border-spacing:5px;/*cellspacing:poor IE support for  this*/
       /* border-collapse:separate;*/
    }

    .divRow
    {
       display:table-row;
       width:auto;

    }
    .divCell
    {
        float:left;/*fix for  buggy browsers*/
        display:table-column;
        width:200px;
        background-color:#ccc;

    }

</style>

</head>
<body>
<div class="divTable">
             <div class="headRow">
                <div class="divCell" align="center">ID</div>
                <div class="divCell" align="center">Name</div>
                <div  class="divCell" align="center">Address</div>
                <div  class="divCell" align="center">Phone Number</div>
                <div  class="divCell" align="center">Modification Date</div>
                <div  class="divCell" align="center">Deleted</div>
             </div>
             <c:forEach var = "contact" items ="${contactsList}" >
	            <div class="divRow">
	             	<div class="divCell" align="center"><a href="${pageContext.request.contextPath}/contacts/${contact.id}">${contact.id}</a></div>
	                <div class="divCell" align="center">${contact.name }</div>
	                <div class="divCell" align="center">${contact.address}</div>
	                <div class="divCell" align="center">${contact.phoneNumber}</div>
	                <div class="divCell" align="center">${contact.modified}</div>
	                <div class="divCell" align="center">${contact.deleted}</div>
	                
	            </div>
            </c:forEach>
      </div>
</body>
</html>
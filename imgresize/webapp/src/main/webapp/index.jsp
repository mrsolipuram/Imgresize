<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload Your Photo</title>
<link href="<%=request.getContextPath()%>/css/jquery-ui.min.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery.fileupload-ui.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/imgresize.css">
</head>
<body>
<form id="file_upload" action="UploadServlet" method="POST" enctype="multipart/form-data">
    <div id="drop_zone_1">
        <input id="file_1" type="file" name="file_1[]" multiple="multiple">
        <div>Upload Media</div>
    </div>    
    <div style="display:none" id="doneButton"><p class="uploadDiv"><input type="button" value="Done" name="B1" onClick="funOnClosePopUp()" class="uploadButton" /></p></div>   
    <p><br><br></p>
    <div style="display:none"><input type="submit" value="Upload" /></div>  
    <div class="imageDiv">
    <table id="files_1" style="background:yellow;"></table>	
    </div>
</form>
<form name="verifyImg" id="verifyImg">
<div class="width80">
<ul id="imagesDivId"></ul>
</div>

<input type="button" value="Save" onclick="imageUtil.submitImageVerify()">
</form>
<script src="<%=request.getContextPath()%>/js/jquery-1.11.1.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js" ></script>
<script src="<%=request.getContextPath()%>/js/jquery.fileupload.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.fileupload-ui.js"></script>
<script src="<%=request.getContextPath()%>/js/image.util.js"></script>
<script>
imageUtil.getAllImages(null);
var images=new Array();
var videos=new Array();
var docs=new Array();

/*global $ */
$(function () {
    var initFileUpload = function (suffix) {
        $('#file_upload').fileUploadUI({
            namespace: 'file_upload_' + suffix,
            fileInputFilter: '#file_' + suffix,
            dropZone: $('#drop_zone_' + suffix),
            uploadTable: $('#files_' + suffix),
            downloadTable: $('#files_' + suffix),
            buildUploadRow: function (files, index) {            	
                return $('<tr><td>' + files[index].name + '<\/td>' +
                        '<td class="file_upload_progress"><div><\/div><\/td>' +
                        '<td class="file_upload_cancel">' +
                        '<button class="ui-state-default ui-corner-all" title="Cancel">' +
                        '<span class="ui-icon ui-icon-cancel">Cancel<\/span>' +
                        '<\/button><\/td><\/tr>');
            },
            buildDownloadRow: function (file) {            	
            	if(file.error_ext_img!=undefined && file.error_ext_img.length>0){            		
            		return $('<tr><td>Invalid File '+file.error_ext_img+'.Allowed are jpeg,jpg,png,gif<\/td><\/tr>');
            	}
            	if(file.error_ext_video!=undefined && file.error_ext_video.length>0){	
            		return $('<tr><td>Invalid File '+file.error_ext_video+'.Allowed are wmv,flv,mp4<\/td><\/tr>');
            	}
            	if(file.error_ext_doc!=undefined && file.error_ext_doc.length>0){            		
            		return $('<tr><td>Invalid File '+file.error_ext_doc+'.Allowed are pdf,txt and doc<\/td><\/tr>');
            	}
            	if(file.error_size!=undefined && file.error_size.length>0){            		
            		return $('<tr><td>Invalid File '+file.error_size+'.Maximum Length: 1MB<\/td><\/tr>');
            	}
            	if(file.storageType == "image"){
            		images.push(file.imagesInfo);
            	}else if(file.storageType == "video"){
            		videos.push(file.imagesInfo);
            	}else{
            		docs.push(file.imagesInfo);
            	}
            	$('#doneButton').show();
                return $('<tr><td><img class="img150X100" src=/webapp/UploadServlet/'+file['imgPath']+'/>' +file.name+'<\/td><\/tr>');
            }
        });
    };
    initFileUpload(1);    
});
</script> 

</body>
</html>
var imageUtil = {
	getAllImages : function(reqUrl) {
		imageUtil.jqueryGetJson('./action', {
			url : reqUrl
		}, function(data) {
			imageUtil.buildImages(data);
		});
	},
	submitImageVerify : function() {
		imageUtil.jqueryGetAjax('./action', '#verifyImg', function(data) {
			if (data.status) {
				imageUtil.buildImages(data.images);
			}
		});
	},
	buildImages : function(data) {
		if (data != null) {
			$('#imagesDivId').html('');
			$.each(data, function(index, item) {
				var tag = '<li class="liStyle"><input type="checkbox" name="imageIds" value='
						+ item._id + '><img src=/webapp/UploadServlet/'
						+ item.imagePath + '?width=150&height=150></li>';
				$('#imagesDivId').append(tag);
			});
		}
	},
	jqueryGetAjax : function(url, formId, callback) {
		$.ajax({
			url : url,
			data : $(formId).serialize(),
			type : 'POST',
			dataType : 'json',
			success : callback
		});
	},
	jqueryGetJson : function(url, params, callback) {
		$.getJSON(url, params, function(data) {
			if (callback)
				callback(data);
		});
	}

};
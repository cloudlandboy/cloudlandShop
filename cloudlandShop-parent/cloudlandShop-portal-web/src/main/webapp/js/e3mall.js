var E3MALL = {
	checkLogin : function(){
		var token = $.cookie("token");
		if(!token){
			return ;
		}
		$.ajax({
			url : "http://localhost:8088/user/token/" + token,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					var html = username + "，欢迎来到云梦购物网！<a href=\"http://www.cloudlandshop.cn/user/logout.html\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	E3MALL.checkLogin();
});
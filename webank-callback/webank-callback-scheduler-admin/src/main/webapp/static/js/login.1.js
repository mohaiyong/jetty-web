//跨域请求
function jsonpRequest(){
	$.ajax({  
	  type:"GET",  
	  dataType:"jsonp",/*-----------------------*/  
	  url: "http://www.tyl.com:7001/ArchiveWeb/userAction!JsonpLogin.action?function_name=login&username=wdgly&userpwd=11&callback=jsonp",  
	  crossDomain:true,/*-----------------------*/  
	  success: function(data){  
	  	alert("success");  
	  },  
	  beforeSend:function(){  
	  },  
	  complete:function(data,status){  
	  },
	  error:function(){
		  alert("服务器异常");
	  }
	});  
}
function jsonp(callJson){
	alert("jsonp回调:"+JSON.stringify(callJson) + "==" +"http://www.tyl.com:7001/ArchiveWeb/userAction!home.action");
	window.open("http://www.tyl.com:7001/ArchiveWeb/userAction!home.action"); 
}
$(function(){
	// 复选框
    $('input').iCheck({
      checkboxClass: 'icheckbox_square-blue',
      radioClass: 'iradio_square-blue',
      increaseArea: '20%' // optional
    });
    
	// 登录.规则校验
	var loginFormValid = $("#loginForm").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {  
        	userName : {  
        		required : true ,
                minlength: 5,
                maxlength: 18
            },  
            password : {  
            	required : true ,
                minlength: 5,
                maxlength: 18
            } 
        }, 
        messages : {  
        	userName : {  
                required :"请输入登录账号."  ,
                minlength:"登录账号不应低于5位",
                maxlength:"登录账号不应超过18位"
            },  
            password : {
            	required :"请输入登录密码."  ,
                minlength:"登录密码不应低于5位",
                maxlength:"登录密码不应超过18位"
            }
        }, 
		highlight : function(element) {  
            $(element).closest('.form-group').addClass('has-error');  
        },
        success : function(label) {  
            label.closest('.form-group').removeClass('has-error');  
            label.remove();  
        },
        errorPlacement : function(error, element) {  
            element.parent('div').append(error);  
        },
        submitHandler : function(form) {
			$.post(base_url + "/login", $("#loginForm").serialize(), function(data, status) {
				if (data.code == "200") {
					window.location.href = base_url;
//                    layer.open({
//                        title: '系统提示',
//                        content: '登录成功',
//                        icon: '1',
//                        end: function(layero, index){
//                            
//                        }
//                    });
				} else {
                    layer.open({
                        title: '系统提示',
                        content: (data.msg || "登录失败"),
                        icon: '2'
                    });
				}
			});
		}
	});
});
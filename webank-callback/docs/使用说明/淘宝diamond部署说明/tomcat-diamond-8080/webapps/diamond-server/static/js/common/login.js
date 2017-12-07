 //js获取项目根路径，如： http://localhost:8083/uimcardprj
var getRootPath = function(){
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath=window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return(localhostPaht+projectName);
}
require.config({
    //baseUrl: './',
    paths: {
        'jquery': '../lib/jquery/jquery-1.8.3.min',
        'validate': '../lib/jquery-validation/jquery.validate.min',
        'validator.addMethod':'../lib/jquery-validation/jQuery.validator.addMethod',
        'base64':'base64'
    },
    shim: {
        'jquery': {
            exports: '$'
        },
        'validate' :  ['jquery'],
        'validator.addMethod':['jquery','validate']
    }
});
require(['jquery','validate','validator.addMethod','base64'], function ($){
    $().ready(function() {
        $("#login-form").validate({
            /*自定义验证规则*/
                rules:{
                    userName:{ required:true,minlength:1 },
                    passWord:{ required:true,minlength:6 }
                    // ,
                    // verifiedCode:{required:true,verifiedCode:true,remote:{
                    // 	 url:"getCode.do",
                    //      type:"get",
                    //      dataType:"json"
                    //     }
                    // }
                },
                messages: {
                    userName: {
                        required: "请输入账号 ",
                        minlength: "请输入账号 ",
                        remote: "账号不存在 "
                    },
                    passWord: {
                        required: "请输入密码 ",
                        remote:"邮箱/手机号不存在 ",
                        minlength:"密码长度至少6个字符 "
                    }
                    // ,
                    // verifiedCode:{
                    // 	required: "请输入验证码 ",
                    //     verifiedCode: "验证码必须是四位 ",
                    // 	remote: "验证码错误 "
                    // }
                },
                /*错误提示位置*/
             
                errorPlacement:function(error,element){
                    $("label.msg-error").html("");
                    error.appendTo(".error-text");
                    //$(".error-text").html(error[0].innerText);
                    //error.appendTo(element.prev("span"));
                },
                success: function(label) {
                    $("label.msg-error").html("");
                   //$(".error-text").html("");
                 },
                submitHandler:function(form){
                    data = $("#login-form").serializeArray();
                    //data[1].value = data[1].value;
                    var url="/diamond-server/login.do?method=tshLogin";
                    $.ajax({
                        //dataType:'json',
                        url:url,
                        data: data,
                        type:'POST',
                        //jsonp:'callback',
                        async:false,
                        contentType: "application/x-www-form-urlencoded",
                        success:function(data) {
                        	data = eval("(" + data + ")");
                        	//window.location.href = data;
                            if(data.result == 1){
                            	url = data.url;
                            	window.location.href = url;
                            }else{
                                error='<label class="error msg-error" >'+data.message+'</label>';
                               // error.appendTo(".error-text");
                                 $(".error-text").html(error);
                                 //$(".error-text").html();
                            }
                         },
                         error: function(err) {
                        	 $(".error-text").html("登录失败");
                         }
                    });
                    return false;
                }
            });
        
    });
})

function reloadImage(){
	document.getElementById('verified').src='code.do?ts='+new Date().getTime();
}

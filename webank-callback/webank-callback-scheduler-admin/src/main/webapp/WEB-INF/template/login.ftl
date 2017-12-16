<!DOCTYPE html>
<html>
<head>
  	<title>调度中心</title>
  	<#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/blue.css">
    <style type="text/css">
*{
	margin: 0;
	padding: 0;
}

.bg-blur {
            float: left;
            width: 100%;
            background-repeat: no-repeat;
            background-position: center;
            background-size: cover;/*把背景图像扩展至足够大，以使背景图像完全覆盖背景区域，属于CSS3样式*/
            -webkit-filter: blur(15px);
            -moz-filter: blur(15px);
            -o-filter: blur(15px);
            -ms-filter: blur(15px);
            filter: blur(15px);/*实现背景图径向模糊的毛玻璃效果、且兼容主流浏览器，属于CSS3样式*/
        }

body{
	font-family: "微软雅黑";
	font-size: 14px;
	background: url(static/image/bg.jpg) fixed center center;
}
.logo_box{
	width: 280px;
	height: 290px;
	padding: 35px;
	color: #EEE;
	position: absolute;
	left: 50%;
	top:200px;
	margin-left: -175px;
	background-color: rgba(0,0,0,0.3);
	border-radius:6px;
}
.logo_box h3{
	text-align: center;
	height: 20px;
	font: 20px "microsoft yahei",Helvetica,Tahoma,Arial,"Microsoft jhengHei",sans-serif;
	color: #FFFFFF;
	height: 20px;
	line-height: 20px;
	padding:0 0 35px 0;
}
.form{
	width: 280px;
	height: 285px;
}
.input_outer{
	height: 46px;
	padding: 0 5px;
	margin-bottom:20px;
	border-radius:6px;
	position: relative;
	border: rgba(255,255,255,0.2) 1px solid !important;
}
.u_user{
	width: 25px;
	height: 25px;
	background: url(static/image/login_ico.png);
	background-position:  -125px 0;
	position: absolute;
	margin: 12px 13px;
}
.u_passwork{
	width: 25px;
	height: 25px;
	background-image: url(static/image/login_ico.png);
	background-position: -125px -34px;
	position: absolute;
	margin: 12px 13px;
}
.text{
	width: 220px;
	height: 46px;
	outline: none;
	display: inline-block;
	font: 14px "microsoft yahei",Helvetica,Tahoma,Arial,"Microsoft jhengHei";
	margin-left: 50px;
	border: none;
	background: none;
	line-height: 46px;
	color: #FFFFFF;
}
	
/*登录按钮样式*/
.DL{
	margin-bottom: 20px;
	color: #FFFFFF;
}
.DL a{
	text-decoration: none;
	outline: none;
	color: #FFFFFF
}
.act-but{
	height: 20px;
	line-height: 20px;
	text-align: center;
	font-size: 20px;
	border-radius:6px;
	background: #36a3ec;
}
.submit {
	padding: 15px;
	margin-top: 20px;
	display: block;
}
	
/*复选框样式*/
input[type="checkbox"]{
	vertical-align: middle;/*把此元素放置在父元素的中部*/
	margin: 0 5px 0 0;
	border:  1px solid #ccc;
	border-radius: 4px;
	width: 18px;
	height: 18px;
	color: #36a3ec;
	}
</style>
</head>
<body>
	<div class="login-box">
		<div class="login-logo">
			<a><b>分布式任务调度中心</b></a>
		</div>
		<form id="loginForm" method="post" >
			<div class="login-box-body">
				<p class="login-box-msg"></p>
				<div class="form-group has-feedback">
	            	<input type="text" name="userName" class="form-control" placeholder="请输入登录账号" value="admin" >
	            	<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
				</div>
	          	<div class="form-group has-feedback">
	            	<input type="password" name="password" class="form-control" placeholder="请输入登录密码" value="zaq12wsx" >
	            	<span class="glyphicon glyphicon-lock form-control-feedback"></span>
	          	</div>
				<div class="row">
					<div class="col-xs-8">
		              	<div class="checkbox icheck">
		                	<label>
		                  		<input type="checkbox" name="ifRemember" > Remember Me
		                	</label>
						</div>
		            </div><!-- /.col -->
		            <div class="col-xs-4">
						<button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
						<button type="button" onclick="alert();jsonpRequest();" class="btn btn-primary btn-block btn-flat">Jsonp登录</button>
					</div>
				</div>
			</div>
		</form>
	</div>
<@netCommon.commonScript />
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/js/login.1.js"></script>

</body>
</html>

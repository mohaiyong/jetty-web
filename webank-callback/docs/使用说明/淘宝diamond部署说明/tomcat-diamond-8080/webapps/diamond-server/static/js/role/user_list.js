//页面加载时，立即加载数据
$(function() {
	function rowformater(value,row,index){
		var temp = "<a  href='javascript:;' name='editPwdBtn'  class='btn btn-default btn-sm'  >修改密码</a>&emsp; " +
				"<a class='btn btn-default btn-sm' name='delBtn' href='javascript:;'>删除</a>&emsp;";
		return temp;
	}
	var editPassWord = function(userName){
		var password = $.trim($('#user-dialog input[name=password]').val());
		if(password == ''){
			$.messager.alert('操作提示', '用户密码不能为空!', 'error');
			return false;
		}
		$.messager.confirm("修改密码", "你确定要将"+userName+"的密码修改为"+password+"吗??", function (r) {  
	        if (r) { 
				var dataModel = {};
				dataModel.userName = decToHex(userName);
				dataModel.password = decToHex(password);
				var url = '../../tshUser.do?method=changePassword';
				$.ajax({
					url:url,
					traditional:true,
					type:'get',
					async:true,
					data:dataModel,
					cache:false,
					success:function(data){
						if(data.code == 1){
							$('#user-dialog').dialog('close');
							$('#user-dialog').dialog('destroy');
							doSearch();
						}
						$.messager.alert('提示', data.message, 'info');
					},error:function(data){
						$.messager.alert('提示', '操作失败!', 'error');
					}
				});
				return true;
	        }  
	    });
		 return false;  
	}
	var delUser = function(){
		var userName = $.trim($(this).parents('tr.datagrid-row').find('td[field=userName]').text());
		var password = $.trim($(this).parents('tr.datagrid-row').find('td[field=password]').text());
		var rowIndex = $(this).parents('tr.datagrid-row').attr('datagrid-row-index');
		$.messager.confirm("删除用户", "你确定要删除该用户吗??", function (r) {  
	        if (r) { 
	    		var dataModel = {};
	    		dataModel.userName = decToHex(userName);
	    		dataModel.password = decToHex(password);
	    		var url = '../../tshUser.do?method=deleteUser';
	    		$.ajax({
	    			url:url,
	    			traditional:true,
	    			type:'get',
	    			async:true,
	    			data:dataModel,
	    			cache:false,
	    			success:function(data){
	    				if(data.code == 1){
	    					$('#dataGrid').datagrid('deleteRow',rowIndex);
	    				}
	    				$.messager.alert('提示', data.message, 'info');
	    			},error:function(data){
	    				$.messager.alert('提示', '操作失败!', 'error');
	    			}
	    		});
	            return true;  
	        }  
	    });  
	    return false;  
	}
	var addUser = function(){
		var userName = $.trim($('#user-dialog input[name=userName]').val());
		var password = $.trim($('#user-dialog input[name=password]').val());
		if(userName == ''){
			$.messager.alert('操作提示', '用户名称不能为空!', 'error');
			return false;
		}
		if(password == ''){
			$.messager.alert('操作提示', '用户密码不能为空!', 'error');
			return false;
		}
		var dataModel = {};
		dataModel.userName = userName;
		dataModel.password = password;
		var url = '../../tshUser.do?method=addUser';
		$.ajax({
			url:url,
			traditional:true,
			type:'POST',
			async:true,
			data:dataModel,
			contentType: "application/x-www-form-urlencoded",
			cache:false,
			success:function(data){
				if(data.code == 1){
					$('#user-dialog').dialog('close');
					$('#user-dialog').dialog('destroy');
					doSearch();
				}
				$.messager.alert('提示', data.message, 'info');
			},error:function(data){
				$.messager.alert('提示', '操作失败!', 'error');
			}
		});
	}
	
	var showEditPwdDialog = function(){
		var userName = $.trim($(this).parents('tr.datagrid-row').find('td[field=userName]').text());
		var html = $('#userpassword-dialog-template').html();
		$(html).dialog({  
		    title: '修改用户密码',  
		    width: 450,  
		    height: 200,  
		    closed: false,  
		    cache: false,  
		    //href: 'get_content.php',  
		    modal: true,
		    buttons: [{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					editPassWord(userName);
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){
					$('#user-dialog').dialog('close');
					$('#user-dialog').dialog('destroy');
				}
			}],
			onClose:function(){
				$('#user-dialog').dialog('destroy');
		    }
		});  
	}
	var showUserDialog = function(){
		var html = $('#user-dialog-template').html();
		$(html).dialog({  
		    title: '新增用户信息',  
		    width: 450,  
		    height: 200,  
		    closed: false,  
		    cache: false,  
		    //href: 'get_content.php',  
		    modal: true,
		    buttons: [{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					addUser();
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){
					$('#user-dialog').dialog('close');
					$('#user-dialog').dialog('destroy');
				}
			}],
			onClose:function(){
				$('#user-dialog').dialog('destroy');
		    }
		});  
	}
	
	function fixWidth(percent)     
	{     
	    return (document.body.clientWidth - 5) * percent ;      
	}  
	function orderinit(url){
		var columns  = [[
		                 {field:'userName',title:'用户名',width:fixWidth(0.3),align:'center'},
		                 {field:'password',title:'密码',width:fixWidth(0.3),align:'center'},
		                 {field:'opt',title:'操作',width:fixWidth(0.38),align:'center', formatter:function(value, row, index) {
		                	 return rowformater(value,row,index);
		                 }}
		                ]];
		
		$('#dataGrid').datagrid({
			url: url,
			rownumbers: true, //行号
			singleSelect: true, //是否单选
			pagination: false, //分页控件
			pageList: [15],
			autoRowHeight: false,
			fit: true,
			striped: true, //设置为true将交替显示行背景
			fitColumns: false,
			nowrap: false,
			remotesort: false,
			checkOnSelect: false,
			method: "get", //请求数据的方法
			loadMsg: '数据加载中,请稍候......',
			idField:'id',
			rowStyler: function(index,row){
				return 'font-size: 20px;';
			},
			columns:columns,
			onLoadError: function() {
				$.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
			},
			onLoadSuccess:function(data){
				$('.datagrid').undelegate('a[name=editPwdBtn]','click',showEditPwdDialog);
				$('.datagrid').undelegate('a[name=delBtn]','click',delUser);
				
				$('.datagrid').delegate('a[name=editPwdBtn]','click',showEditPwdDialog);
				$('.datagrid').delegate('a[name=delBtn]','click',delUser);
			},
	        toolbar:'#tb'
		});
		$("#search").bind('keypress',function(e){
			e = e||event;  
			if(e.keyCode=='13'){
				doSearch();
			}
		});
		$("#tradeSelect").combobox({
			onChange:function(){
				doSearch();
			}
		});
	}

	//搜索
	var doSearch = function (){
		var data = $("#form").serialize();
		//$('#dataGrid').datagrid("load",data);
		console.info(data);
		orderinit("../../tshUser.do?method=listUser&" + data);
	}
	
	var initUI = function(){
		$('#form').delegate('#addUser','click',showUserDialog);
		$('#form').delegate('#searchUser','click',doSearch);
	}
	initUI();
	doSearch();
});

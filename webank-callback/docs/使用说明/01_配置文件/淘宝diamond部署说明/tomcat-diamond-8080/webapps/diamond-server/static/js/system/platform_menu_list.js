//页面加载时，立即加载数据
$(function() {
	function rowformater(value,row,index){
		var sysCode = $("#systemSelect").combobox('getValue');
		var temp = "";
		if(row.parentMenuId == sysCode){
			temp = "<a  href='javascript:;' name='addChilBtn'  class='btn btn-default btn-sm'  >添加子菜单</a>&emsp; ";
		}else if(row.level == '2'){
			temp = "<a  href='javascript:;' name='addFunBtn'  class='btn btn-default btn-sm'  >添加功能菜单</a>&emsp;";
		}
		temp += "<a  href='javascript:;' name='editBtn'  class='btn btn-default btn-sm'  >编辑</a>&emsp; ";
		temp += "<a  href='javascript:;' name='delBtn'  class='btn btn-default btn-sm'  >删除</a>&emsp; ";
		return temp;
	}
	var delMenu = function(){
		var menuId = $.trim($(this).parents('tr.datagrid-row').find('td[field=id]').text());
		var sysCode = $("#systemSelect").combobox('getValue');
		var rowIndex = $(this).parents('tr.datagrid-row').attr('datagrid-row-index');
		$.messager.confirm("删除菜单", "你确定要删除该菜单信息吗??", function (r) {  
	        if (r) { 
	    		var dataModel = {};
	    		dataModel.menuId = menuId;
	    		dataModel.sysCode = sysCode;
	    		var url = '../../systemMenu.do?method=delSystemMenu';
	    		$.ajax({
	    			url:url,
	    			traditional:true,
	    			type:'get',
	    			async:true,
	    			data:dataModel,
	    			cache:false,
	    			success:function(data){
	    				if(data.code == 1){
	    					//$('#dataGrid').datagrid('deleteRow',rowIndex);
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
	var openMenuItem = function(){
		var menuId = $.trim($(this).parents('tr.datagrid-row').find('td[field=id]').text());
		var sysCode = $("#systemSelect").combobox('getValue');
		var rowIndex = $(this).parents('tr.datagrid-row').attr('datagrid-row-index');
		$.messager.confirm("开启菜单", "你确定要开启该菜单信息吗??", function (r) {  
	        if (r) { 
	    		var dataModel = {};
	    		dataModel.menuId = menuId;
	    		dataModel.sysCode = sysCode;
	    		dataModel.menuStatus = "1";
	    		var url = '../../systemMenu.do?method=colOrOpenMenuItem';
	    		$.ajax({
	    			url:url,
	    			traditional:true,
	    			type:'get',
	    			async:true,
	    			data:dataModel,
	    			cache:false,
	    			success:function(data){
	    				if(data.code == 1){
	    					//$('#dataGrid').datagrid('deleteRow',rowIndex);
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
	
	var colMenuItem = function(){
		var menuId = $.trim($(this).parents('tr.datagrid-row').find('td[field=id]').text());
		var sysCode = $("#systemSelect").combobox('getValue');
		var rowIndex = $(this).parents('tr.datagrid-row').attr('datagrid-row-index');
		$.messager.confirm("关闭菜单", "你确定要关闭该菜单信息吗??", function (r) {  
	        if (r) { 
	    		var dataModel = {};
	    		dataModel.menuId = menuId;
	    		dataModel.sysCode = sysCode;
	    		dataModel.menuStatus  = "0";
	    		var url = '../../systemMenu.do?method=colOrOpenMenuItem';
	    		$.ajax({
	    			url:url,
	    			traditional:true,
	    			type:'get',
	    			async:true,
	    			data:dataModel,
	    			cache:false,
	    			success:function(data){
	    				if(data.code == 1){
	    					//$('#dataGrid').datagrid('deleteRow',rowIndex);
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
	
	var checkFunCode = function(url,type,menuType){
		var sysCode = $.trim($('#system-dialog #sysName-dialog').attr('sysCode'));
		var funCode = '';
		if($('#system-dialog input[name=funCode]').length == 1){
			funCode = $.trim($('#system-dialog input[name=funCode]').val());
			if(funCode == ''){
				$.messager.alert('操作提示', '功能编码不能为空!', 'error');
				return false;
			}
			funCode = decToHex(funCode);
		}
		var dataModel = {}
		if(type != 'add'){
			var menuId = $.trim($('#menuName').attr('menuId'));
			dataModel.menuId = menuId;
		}else{
			dataModel.menuId = null;
		}
		dataModel.sysCode = sysCode;
		dataModel.funCode  = funCode;
		dataModel.source = 'platform';
		$.ajax({
			url:'../../systemMenu.do?method=checkFunCode',
			traditional:true,
			type:'get',
			async:true,
			data:dataModel,
			contentType: "application/x-www-form-urlencoded",
			cache:false,
			success:function(data){
				if(data.hasCode == false){
					if(type == 'add'){
						addMenu(url,menuType);
					}else{
						editMenu(url);
					}
				}else{
					$.messager.alert('提示', '功能编码不能重复!', 'info');
				}
			},error:function(data){
				$.messager.alert('提示', '操作失败!', 'error');
			}
		});
	}
	var addMenu = function(url,menuType,level){
		var menuName = $.trim($('#system-dialog input[name=menuName]').val());
		var menuPath = $.trim($('#system-dialog input[name=menuPath]').val());
		var funCode	= '';
		var menuStatus = $.trim($('#system-dialog input[name=menuStatus]:checked').val());
		//var openType = $.trim($('#system-dialog input[name=openType]:checked').val());
		var openType='_self';
		var sysCode = $.trim($('#system-dialog #sysName-dialog').attr('sysCode'));
		var pMenuId = $.trim($('#pMenuName').attr('pMenuId'));
		
		if(menuName == ''){
			$.messager.alert('操作提示', '菜单名称不能为空!', 'error');
			return false;
		}
		if(menuPath == ''){
			$.messager.alert('操作提示', '菜单地址不能为空!', 'error');
			return false;
		}
		if($('#system-dialog input[name=funCode]').length == 1){
			funCode = $.trim($('#system-dialog input[name=funCode]').val());
			if(funCode == ''){
				$.messager.alert('操作提示', '功能编码不能为空!', 'error');
				return false;
			}
			funCode = decToHex(funCode);
		}
		var dataModel = {};
		dataModel.menuName = decToHex(menuName);
		dataModel.menuPath = decToHex(menuPath);
		dataModel.menuStatus = menuStatus;
		dataModel.sysCode = sysCode;
		dataModel.pMenuId = pMenuId;
		dataModel.openType = openType;
		dataModel.funCode = funCode;
		dataModel.menuType = menuType;
		dataModel.level = level;
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
					$('#system-dialog').dialog('close');
					$('#system-dialog').dialog('destroy');
					doSearch();
				}
				$.messager.alert('提示', data.message, 'info');
			},error:function(data){
				$.messager.alert('提示', '操作失败!', 'error');
			}
		});
	}
	
	
	var editMenu = function(url){
		var menuName = $.trim($('#system-dialog input[name=menuName]').val());
		var menuPath = $.trim($('#system-dialog input[name=menuPath]').val());
		var menuStatus = $.trim($('#system-dialog input[name=menuStatus]:checked').val());
		//var openType = $.trim($('#system-dialog input[name=openType]:checked').val());
		var openType='_self';
		var sysCode = $.trim($('#system-dialog #sysName-dialog').attr('sysCode'));
		var menuId = $.trim($('#menuName').attr('menuId'));
		var funCode	= '';
		if(menuName == ''){
			$.messager.alert('操作提示', '菜单名称不能为空!', 'error');
			return false;
		}
		if(menuPath == ''){
			$.messager.alert('操作提示', '菜单地址不能为空!', 'error');
			return false;
		}
		if($('#system-dialog input[name=funCode]').length == 1){
			funCode = $.trim($('#system-dialog input[name=funCode]').val());
			if(funCode == ''){
				$.messager.alert('操作提示', '功能编码不能为空!', 'error');
				return false;
			}
			funCode = decToHex(funCode);
		}
		var dataModel = {};
		dataModel.menuName = decToHex(menuName);
		dataModel.menuPath = decToHex(menuPath);
		dataModel.menuStatus = menuStatus;
		dataModel.sysCode = sysCode;
		dataModel.menuId = menuId;
		dataModel.openType = openType;
		dataModel.funCode = funCode;
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
					$('#system-dialog').dialog('close');
					$('#system-dialog').dialog('destroy');
					doSearch();
				}
				$.messager.alert('提示', data.message, 'info');
			},error:function(data){
				$.messager.alert('提示', '操作失败!', 'error');
			}
		});
	}
	
	var editSystemDialog = function(menuId){
		var sysName = $("#systemSelect").combobox('getText');
		var sysCode = $("#systemSelect").combobox('getValue');
		
		var menuNode = $('#dataGrid').treegrid('find',menuId);
		var html = $('#system-dialog-template').html();
		$(html).dialog({  
		    title: '编辑 '+menuNode.menuName+' 一级菜单',  
		    width: 450,  
		    height: 280,  
		    closed: false,  
		    cache: false,  
		    //href: 'get_content.php',  
		    modal: true,
		    buttons: [{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					var url = "../../systemMenu.do?method=editMenu";
					editMenu(url);
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){
					$('#system-dialog').dialog('close');
				}
			}],onOpen:function(){
				$('#sysName-dialog').val(sysName);
				$('#sysName-dialog').attr('sysCode',sysCode);
				$('#menuName').attr('menuId',menuNode.id);
				$('#menuName').val(menuNode.menuName);
				$('#menuPath').val(menuNode.menuPath);
				$('#funCode').val(menuNode.funCode);
				$('input[name=menuStatus][value='+menuNode.menuStatus+']').attr('checked','checked');
				if(menuNode.openType == null || "" == menuNode.openType){
					menuNode.openType = "_self"
				}
				$('#menuStatus-content').hide();
				//$('input[name=openType][value='+menuNode.openType+']').attr('checked','checked');
		    },
			onClose:function(){
				$('#system-dialog').dialog('destroy');
		    }
		});  
	}
	
	var editSystemChilDialog = function(menuId){
		
		var sysName = $("#systemSelect").combobox('getText');
		var sysCode = $("#systemSelect").combobox('getValue');
		var menuNode = $('#dataGrid').treegrid('find',menuId);
		var pMenuId = $('#dataGrid').treegrid('getParent',menuId);
		var html = $('#system-menu-dialog-template').html();
		
		$(html).dialog({  
		    title: '编辑 '+menuNode.menuName+' 二级菜单',  
		    width: 450,  
		    height: 280,  
		    closed: false,  
		    cache: false,  
		    //href: 'get_content.php',  
		    modal: true,
		    buttons: [{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					var url = "../../systemMenu.do?method=editMenu";
					editMenu(url);
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){
					$('#system-dialog').dialog('close');
				}
			}],onOpen:function(){
				$('#sysName-dialog').val(sysName);
				$('#sysName-dialog').attr('sysCode',sysCode);
				$('#menuName').attr('menuId',menuNode.id);
				$('#menuName').val(menuNode.menuName);
				$('#menuPath').val(menuNode.menuPath);
				$('#pMenuName').val(pMenuId.menuName);
				$('#funCode').val(menuNode.funCode);
				$('input[name=menuStatus][value='+menuNode.menuStatus+']').attr('checked','checked');
				if(menuNode.openType == null || "" == menuNode.openType){
					menuNode.openType = "_self"
				}
				$('#menuStatus-content').hide();
				//$('input[name=openType][value='+menuNode.openType+']').attr('checked','checked');
		    },
			onClose:function(){
				$('#system-dialog').dialog('destroy');
		    }
		});  
	}
	
	var editFunMenuDialog = function(menuId){
		var sysName = $("#systemSelect").combobox('getText');
		var sysCode = $("#systemSelect").combobox('getValue');
		var menuNode = $('#dataGrid').treegrid('find',menuId);
		var pMenuId = $('#dataGrid').treegrid('getParent',menuId);
		var html = $('#system-function-dialog-template').html();
		
		$(html).dialog({  
		    title: '编辑 '+menuNode.menuName+' 功能菜单',  
		    width: 450,  
		    height: 300,  
		    closed: false,  
		    cache: false,  
		    //href: 'get_content.php',  
		    modal: true,
		    buttons: [{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					var url = "../../systemMenu.do?method=editMenu";
					//checkFunCode(url,'edit','menu');
					editMenu(url);
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){
					$('#system-dialog').dialog('close');
				}
			}],onOpen:function(){
				$('#sysName-dialog').val(sysName);
				$('#sysName-dialog').attr('sysCode',sysCode);
				$('#menuName').attr('menuId',menuNode.id);
				$('#menuName').val(menuNode.menuName);
				$('#menuPath').val(menuNode.menuPath);
				$('#pMenuName').val(pMenuId.menuName);
				$('#funCode').val(menuNode.funCode);
				
				$('input[name=menuStatus][value='+menuNode.menuStatus+']').attr('checked','checked');
				if(menuNode.openType == null || "" == menuNode.openType){
					menuNode.openType = "_self"
				}
				$('#menuStatus-content').hide();
				//$('input[name=openType][value='+menuNode.openType+']').attr('checked','checked');
		    },
			onClose:function(){
				$('#system-dialog').dialog('destroy');
		    }
		}); 
	}
	
	var editMenuDialog = function(){
		var sysName = $("#systemSelect").combobox('getText');
		var sysCode = $("#systemSelect").combobox('getValue');
		var menuId = $.trim($(this).parents('tr.datagrid-row').find('td[field=id]').text());
		var pMenuId = $('#dataGrid').treegrid('getParent',menuId);
		var funCode = $.trim($(this).parents('tr.datagrid-row').find('td[field=funCode]').text());
		if(pMenuId == null){//一级菜单
			editSystemDialog(menuId);
		}else if(funCode == null || $.trim(funCode) == '' || typeof(funCode) == 'undefined'){//二级菜单
			editSystemChilDialog(menuId);
		}else{//三级菜单(功能按钮菜单)
			editFunMenuDialog(menuId)
		}
	}
	
	var showAddChilDialog = function(){
		var sysName = $("#systemSelect").combobox('getText');
		var sysCode = $("#systemSelect").combobox('getValue');
		var menuName = $.trim($(this).parents('tr.datagrid-row').find('td[field=menuName]').text());
		var menuId = $.trim($(this).parents('tr.datagrid-row').find('td[field=id]').text());
		var html = $('#system-menu-dialog-template').html();
		$(html).dialog({  
		    title: '新增 '+sysName+' 二级菜单',  
		    width: 450,  
		    height: 300,  
		    closed: false,  
		    cache: false,  
		    //href: 'get_content.php',  
		    modal: true,
		    buttons: [{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					var url = "../../systemMenu.do?method=addMenu";
					addMenu(url,'menu',2);
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){
					$('#system-dialog').dialog('close');
				}
			}],
			onOpen:function(){
				$('#sysName-dialog').val(sysName);
				$('#sysName-dialog').attr('sysCode',sysCode);
				
				$('#pMenuName').val(menuName);
				$('#pMenuName').attr('pMenuId',menuId);
		    },
			onClose:function(){
				$('#system-dialog').dialog('destroy');
		    }
		});  
	}
	var showSystemDialog = function(){
		var sysName = $("#systemSelect").combobox('getText');
		var sysCode = $("#systemSelect").combobox('getValue');
		var html = $('#system-dialog-template').html();
		$(html).dialog({  
		    title: '新增 '+sysName+' 一级菜单',  
		    width: 450,  
		    height: 280,  
		    closed: false,  
		    cache: false,  
		    //href: 'get_content.php',  
		    modal: true,
		    buttons: [{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					var url = "../../systemMenu.do?method=addMenu";
					addMenu(url,'menu',1);
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){
					$('#system-dialog').dialog('close');
				}
			}],onOpen:function(){
				$('#sysName-dialog').val(sysName);
				$('#sysName-dialog').attr('sysCode',sysCode);
		    },
			onClose:function(){
				$('#system-dialog').dialog('destroy');
		    }
		});  
	}
	
	function fixWidth(percent)     
	{     
	    return (document.body.clientWidth - 5) * percent ;      
	}  
	
	var showFunMenuItem = function(){
		var sysName = $("#systemSelect").combobox('getText');
		var sysCode = $("#systemSelect").combobox('getValue');
		var menuName = $.trim($(this).parents('tr.datagrid-row').find('td[field=menuName]').text());
		var menuId = $.trim($(this).parents('tr.datagrid-row').find('td[field=id]').text());
		var html = $('#system-function-dialog-template').html();
		$(html).dialog({  
		    title: '新增 '+sysName+' 功能菜单', 
		    width: 450,  
		    height: 380,  
		    closed: false,  
		    cache: false,  
		    modal: true,
		    buttons: [{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					var url = "../../systemMenu.do?method=addMenu";
					//checkFunCode(url,'add','url');
					addMenu(url,'url',3);
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){
					$('#system-dialog').dialog('close');
				}
			}],onOpen:function(){
				$('#sysName-dialog').val(sysName);
				$('#sysName-dialog').attr('sysCode',sysCode);
				
				$('#pMenuName').val(menuName);
				$('#pMenuName').attr('pMenuId',menuId);
		    },
			onClose:function(){
				$('#system-dialog').dialog('destroy');
		    }
		}); 
	}
	
	function orderinit(url){
		var columns  = [[
		                 {field:'id',title:'菜单名称',hidden:true,align:'center'},
		                 {field:'menuName',title:'菜单名称',width:fixWidth(0.2),align:'left'},
		                 {field:'menuPath',title:'链接地址',width:fixWidth(0.28),align:'center'},
		                 {field:'funCode',title:'功能编码',width:fixWidth(0.1),align:'center'},
		                 {field:'menuStatus',title:'状态',width:fixWidth(0.1),align:'center',formatter: function(value,row,index){
		     				if (row.menuStatus == '1'){
		    					return '开启';
		    				} else {
		    					return '关闭';
		    				}
		    			 }},
		                 {field:'opt',title:'操作',width:fixWidth(0.30),align:'center', formatter:function(value, row, index) {
		                	 return rowformater(value,row,index);
		                 }}
		                ]];
		
		$('#dataGrid').treegrid({
			url: url,
			rownumbers: true, //行号
			singleSelect: true, //是否单选
			pagination: false, //分页控件
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
			parentField:'parentMenuId',
			treeField:'menuName',
			rowStyler: function(index,row){
				return 'font-size: 20px;';
			},
			columns:columns,
			onLoadError: function() {
				$.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
			},
			onLoadSuccess:function(data){
				$('.datagrid').undelegate('a[name=addChilBtn]','click',showAddChilDialog);
				$('.datagrid').undelegate('a[name=editBtn]','click',editMenuDialog);
				$('.datagrid').undelegate('a[name=delBtn]','click',delMenu);
				$('.datagrid').undelegate('a[name=colBtn]','click',colMenuItem);
				$('.datagrid').undelegate('a[name=openBtn]','click',openMenuItem);
				$('.datagrid').undelegate('a[name=addFunBtn]','click',showFunMenuItem);
				
				$('.datagrid').delegate('a[name=addChilBtn]','click',showAddChilDialog);
				$('.datagrid').delegate('a[name=editBtn]','click',editMenuDialog);
				$('.datagrid').delegate('a[name=delBtn]','click',delMenu);
				$('.datagrid').delegate('a[name=colBtn]','click',colMenuItem);
				$('.datagrid').delegate('a[name=openBtn]','click',openMenuItem);
				$('.datagrid').delegate('a[name=addFunBtn]','click',showFunMenuItem);
			},
	        toolbar:'#tb'
		});
		$("#search").bind('keypress',function(e){
			e = e||event;  
			if(e.keyCode=='13'){
				doSearch();
			}
		});
	}
	
	
	var loadSystemList = function(){
		$.getJSON('../../system.do?method=systemPlatformList',function(json){
			var arr = [];
			//arr.push({'id':'','sysType':'-----请选择网点-----'});
			if(json){
				for(var i = 0;i <json.length;i++){
					arr.push({'id':json[i].sysCode,'sysName':json[i].sysName});
				}
			}
			$('#systemSelect').combobox({
	        	data : arr,
				method : 'get',
				valueField : 'id',
				textField : 'sysName',
				panelWidth : 170,
				required : false,
				editable : true,
				width : 170,
				panelHeight :200,
				onLoadSuccess : function(data) { //
	                var val = $(this).combobox("getData");  
	                for (var item in val[0]) {  
	                    if (item == "id") {  
	                        $(this).combobox("select", val[0][item]);  
	                    }  
	                }  
				}	
	        });
		});
	}
	//搜索
	var doSearch = function (){
		var sysCode = $("#systemSelect").combobox('getValue');
		orderinit("../../systemMenu.do?method=systemMenuList&sysCode="+sysCode);
	}
	
	var initUI = function(){
		$('#form').delegate('#addSystem','click',showSystemDialog);
		
		 $("#systemSelect").combobox({
 			onChange:function(){
 				doSearch();
 			}
 		});
	}
	initUI();
	loadSystemList();
});

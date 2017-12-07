//页面加载时，立即加载数据
$(function() {
	//搜索
	function doSearch(){
		var data = $("#form").serialize();
		//$('#dataGrid').datagrid("load",data);
		console.info(data); 
		orderinit("../../tshAdmin.do?method=listConfig&" + data);
	}
	function fixWidth(percent)     
	{     
	    return (document.body.clientWidth - 5) * percent ;      
	} 
	function rowformater(value,row,index){
		var temp = "<a  href='javascript:;' name='editBtn'  class='btn btn-default btn-sm'  >编辑</a>&emsp; " +
				"<a class='btn btn-default btn-sm' name='delBtn' href='javascript:;'  >删除</a>&emsp;" +
				"<a class='btn btn-default btn-sm' name='saveDiskBtn' href='javascript:;'  >保存磁盘</a>&emsp; " ;
				//"<a class='btn btn-default btn-sm' name='showBtn' href='javascript:;'  >预览</a>&emsp;";
		return temp;
	}
	function orderinit(url){
		var columns  = [[
		                 {field:'id',title:'id',hidden:true,align:'center'},
		                 {field:'dataId',title:'dataId',width:fixWidth(0.3),align:'center'},
		                 {field:'group',title:'组名',width:fixWidth(0.3),align:'center'},
		                 {field:'opt',title:'操作',width:fixWidth(0.38),align:'center', formatter:function(value, row, index) {
		                	 return rowformater(value,row,index);
		                 }}
		                ]];
		
		$('#dataGrid').datagrid({
			url: url,
			rownumbers: true, //行号
			singleSelect: true, //是否单选
			pagination: true, //分页控件
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
				$('.datagrid').undelegate('a[name=editBtn]','click',getConfig);
				$('.datagrid').undelegate('a[name=delBtn]','click',delConfig);
				$('.datagrid').undelegate('a[name=saveDiskBtn]','click',saveDiskConfig);
				$('.datagrid').undelegate('a[name=showBtn]','click',showConfig);
				
				$('.datagrid').delegate('a[name=editBtn]','click',getConfig);
				$('.datagrid').delegate('a[name=delBtn]','click',delConfig);
				$('.datagrid').delegate('a[name=saveDiskBtn]','click',saveDiskConfig);
				$('.datagrid').delegate('a[name=showBtn]','click',showConfig);
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
	
	var setEditDialogInfo = function(data){
		if(data == null){
			return false;
		}
		$('#config-dialog input[name=dataId]').val(data.dataId);
		$('#config-dialog input[name=group]').val(data.group);
		$('#config-dialog input[name=dataId]').attr('readonly','true');
		$('#config-dialog input[name=group]').attr('readonly','true');
		if(data.content != null && data.content != ''){
//			var configItems = data.content.split('\n');
//			var contentHtml = "";
//			$.each(configItems,function(i,v){
//				contentHtml += $('#config-item-template').html();
//			});
//			$('.form-group-content').html(contentHtml);
//			$.each(configItems,function(i,v){
//				if(v != ''){
//					var configItem = v.split('=');
//					var configValue = '';
//					$.each(configItem,function(i,v){
//						if(i != 0){
//							configValue+=v;
//						}
//					});
//					$('.form-group-content .form-group:eq('+i+')').find('.name').val(configItem[0]);
//					$('.form-group-content .form-group:eq('+i+')').find('.value').val(configValue);
//				}
//			});
			$('.form-group-content').html('<textarea id="editconfigs" class="conf-content" style="width:470px;height:320px;overflow:scroll;" rows="10" cols="10">'+data.content+'</textarea>');
			$('.form-group-content').css("width","470px").css("height","320px");
		}
		$('#config-dialog').delegate('a.clear','click',removeConfigItem);
		$('#config-dialog').delegate('a.addItem','click',addConfigItem);
	}
	
	var showEditDialog = function(data){
		var html = $('#config-dialog-template').html();
		$(html).dialog({  
		    title: '编辑配置信息',  
		    width: 820,  
		    height: 570,  
		    closed: false,  
		    cache: false,  
		    //href: 'get_content.php',  
		    modal: true,
		    buttons: [{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					var url = '../../tshAdmin.do?method=updateConfig';
					saveConfig(url);
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){
					$('#config-dialog').dialog('close');
					$('#config-dialog').dialog('destroy');
				}
			}],
			onOpen:function(){
				setEditDialogInfo(data);
			},
			onClose:function(){
				$('#config-dialog').dialog('destroy');
		    }
		});
		
	}
	
	var getConfig = function(){
		var dataId = $.trim($(this).parents('tr.datagrid-row').find('td[field=dataId]').text());
		var group = $.trim($(this).parents('tr.datagrid-row').find('td[field=group]').text());
		var dataModel = {};
		dataModel.dataId = dataId;
		dataModel.group = group;
		var url = '../../tshAdmin.do?method=detailConfig';
		$.ajax({
			url:url,
			traditional:true,
			type:'get',
			async:true,
			data:dataModel,
			cache:false,
			success:function(data){
				showEditDialog(data);
			},error:function(data){
				$.messager.alert('提示', '操作失败!', 'error');
			}
		});
	}
	var delConfig = function(){
		var id = $.trim($(this).parents('tr.datagrid-row').find('td[field=id]').text());
		var rowIndex = $(this).parents('tr.datagrid-row').attr('datagrid-row-index');
		$.messager.confirm("删除配置", "你确定要删除该配置信息吗??", function (r) {  
	        if (r) { 
	    		var dataModel = {};
	    		dataModel.id = id;
	    		var url = '../../tshAdmin.do?method=deleteConfig';
	    		$.ajax({
	    			url:url,
	    			traditional:true,
	    			type:'get',
	    			async:true,
	    			data:dataModel,
	    			cache:false,
	    			success:function(data){
	    				$('#dataGrid').datagrid('deleteRow',rowIndex);
	    				$.messager.alert('提示', '删除成功!', 'info');
	    			},error:function(data){
	    				$.messager.alert('提示', '操作失败!', 'error');
	    			}
	    		});
	            return true;  
	        }  
	    });  
	    return false;  
	}
	var saveDiskConfig = function(){
		var dataId = $.trim($(this).parents('tr.datagrid-row').find('td[field=dataId]').text());
		var group = $.trim($(this).parents('tr.datagrid-row').find('td[field=group]').text());
		var dataModel = {};
		dataModel.dataId = dataId;
		dataModel.group = group;
		var url = '../../tshNotify.do?method=notifyConfigInfo';
		$.ajax({
			url:url,
			traditional:true,
			type:'get',
			async:true,
			data:dataModel,
			cache:false,
			success:function(data){
				$.messager.alert('提示', '操作成功!', 'info');
			},error:function(data){
				$.messager.alert('提示', '操作失败!', 'error');
			}
		});
	}
	var showConfig = function(){
		$.messager.alert('提示', '等待开发中...', 'info');
	}
	
	//删除配置项
	var removeConfigItem = function (){
		$(this).parents('.form-group').remove();
	}
	
	var addConfigItem = function (){
		var itemHtml = $('#config-item-template').html();
		$('.form-group-content').append(itemHtml);
	}
	var setContent = function(){
		var content = $(".conf-content").val();
		
//		$.each($('.form-group-content .form-group'),function(i,v){
//			var name = $.trim($(this).find('.name').val());
//			var value = $.trim($(this).find('.value').val());
//			if(name.indexOf('=') != -1){
//				$.messager.alert('提示', '属性名: '+name +' ,不能含有=符号', 'error');
//				content = 'false';
//				return false;
//			}
//			if(name != '' && value != ''){
//				var item = name +"="+value;
//				content += item+'\n';
//			}
//		});
		return content;
	}
	
	var saveConfig = function(url){
		var dataId = $.trim($('#config-dialog input[name=dataId]').val());
		var group = $.trim($('#config-dialog input[name=group]').val());
		if(dataId == ''){
			$.messager.alert('提示', '请输入dataId!', 'error');
			return false;
		}
		if(group == ''){
			$.messager.alert('提示', '请输入group!', 'error');
			return false;
		}
		var content = setContent();
		if(content == ''){
			$.messager.alert('提示', '请输入content!', 'error');
			return false;
		}
		if(content == 'false'){
			return false;
		}
		var dataModel = {};
//		dataModel.dataId = decToHex(dataId);
//		dataModel.group = decToHex(group);
//		dataModel.content = decToHex(content);
		dataModel.dataId = dataId;
		dataModel.group = group;
		dataModel.content = content;
		$.ajax({
			url:url,
			traditional:true,
			type:'POST',
			contentType: "application/x-www-form-urlencoded",
			async:true,
			data:dataModel,
			cache:false,
			success:function(data){
				$('#config-dialog').dialog('close');
				$('#config-dialog').dialog('destroy');
				$.messager.alert('提示', '操作成功!', 'info');
			},error:function(data){
				$.messager.alert('提示', '操作失败!', 'error');
			}
		});
	}
	
	var showConfigDialog = function(url){
		var html = $('#config-dialog-template').html();
		$(html).dialog({  
		    title: '新增配置信息',  
		    width: 820,  
		    height: 570,  
		    closed: false,  
		    cache: false,  
		    modal: true,
		    buttons: [{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					var url = '../../tshAdmin.do?method=postConfig';
					saveConfig(url);
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){
					$('#config-dialog').dialog('close');
					$('#config-dialog').dialog('destroy');
				}
			}],
			onOpen:function(){
				$('#config-dialog').delegate('a.clear','click',removeConfigItem);
				$('#config-dialog').delegate('a.addItem','click',addConfigItem);
			},
			onClose:function(){
				$('#config-dialog').dialog('destroy');
		    }
		});  
	}
	var initUI = function(){searchConfig
		$('#form').delegate('#addConfig','click',showConfigDialog);
		$('#form').delegate('#searchConfig','click',doSearch);
	}
	initUI();
	doSearch();
});


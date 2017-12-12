//页面加载时，立即加载数据
$(function() {
	function rowformater(value,row,index){
		var temp = "<a  href='javascript:;' name='editBtn'  class='btn btn-default btn-sm'  >修改信息</a>&emsp; " +
				"<a class='btn btn-default btn-sm' name='delBtn' href='javascript:;'>删除</a>&emsp;";
		return temp;
	}
	var delSystem = function(){
		var sysName = $.trim($(this).parents('tr.datagrid-row').find('td[field=sysName]').text());
		var sysCode = $.trim($(this).parents('tr.datagrid-row').find('td[field=sysCode]').text());
		var domain = $.trim($(this).parents('tr.datagrid-row').find('td[field=domain]').text());
		var sysType = $.trim($(this).parents('tr.datagrid-row').find('td[field=sysType]').text());
		var rowIndex = $(this).parents('tr.datagrid-row').attr('datagrid-row-index');
		$.messager.confirm("删除系统信息", "你确定要删除该系统信息吗??", function (r) {  
	        if (r) { 
	    		var dataModel = {};
	    		dataModel.sysCode = decToHex(sysCode);
	    		dataModel.sysName = decToHex(sysName);
	    		dataModel.domain = decToHex(domain);
	    		dataModel.sysType = decToHex(sysType);
	    		var url = '../../system.do?method=delSystem';
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
	var addSystem = function(url){
		var sysName = $.trim($('#system-dialog input[name=sysName]').val());
		var sysCode = $.trim($('#system-dialog input[name=sysCode]').val());
		var domain = $.trim($('#system-dialog input[name=domain]').val());
		var sysType = $.trim($('#system-dialog #sysType').val());
		
		if(sysName == ''){
			$.messager.alert('操作提示', '系统名称不能为空!', 'error');
			return false;
		}
		if(sysCode == ''){
			$.messager.alert('操作提示', '系统编码不能为空!', 'error');
			return false;
		}
		var dataModel = {};
//		dataModel.sysName = decToHex(sysName);
//		dataModel.sysCode = decToHex(sysCode);
//		dataModel.domain = decToHex(domain);
//		dataModel.sysType = decToHex(sysType);
		dataModel.sysName =sysName;
		dataModel.sysCode = sysCode;
		dataModel.domain = domain;
		dataModel.sysType = sysType;
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
	
	var showEditDialog = function(){
		var sysName = $.trim($(this).parents('tr.datagrid-row').find('td[field=sysName]').text());
		var sysCode = $.trim($(this).parents('tr.datagrid-row').find('td[field=sysCode]').text());
		var domain = $.trim($(this).parents('tr.datagrid-row').find('td[field=domain]').text());
		var sysType = $.trim($(this).parents('tr.datagrid-row').find('td[field=sysType]').text());
		var html = $('#system-dialog-template').html();
		$(html).dialog({  
		    title: '修改系统名称',  
		    width: 450,  
		    height: 220,  
		    closed: false,  
		    cache: false,  
		    //href: 'get_content.php',  
		    modal: true,
		    buttons: [{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					var url = "../../system.do?method=updateSystem";
					addSystem(url);
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){
					$('#system-dialog').dialog('close');
				}
			}],
			onOpen:function(){
				$('#system-dialog input[name=sysCode]').attr('readOnly','true');
				$('#system-dialog input[name=sysName]').val(sysName);
				$('#system-dialog input[name=sysCode]').val(sysCode);
				$('#system-dialog input[name=domain]').val(domain);
				$('#system-dialog #sysType').val(sysType);
				$('#sysType-content').hide();
		    },
			onClose:function(){
				$('#system-dialog').dialog('destroy');
		    }
		});  
	}
	var showSystemDialog = function(){
		var html = $('#system-dialog-template').html();
		$(html).dialog({  
		    title: '新增系统信息',  
		    width: 450,  
		    height: 250,  
		    closed: false,  
		    cache: false,  
		    //href: 'get_content.php',  
		    modal: true,
		    buttons: [{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					var url = "../../system.do?method=addProject";
					addSystem(url);
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){
					$('#system-dialog').dialog('close');
				}
			}],
			onClose:function(){
				$('#system-dialog').dialog('destroy');
		    }
		});  
	}
	
	function fixWidth(percent)     
	{     
	    return (document.body.clientWidth - 5) * percent ;      
	}  
	
	function orderinit(url){
		var columns  = [[
		                 {field:'sysName',title:'系统名称',width:fixWidth(0.3),align:'center'},
		                 {field:'sysCode',title:'系统编码',width:fixWidth(0.13),align:'center'},
		                 {field:'domain',title:'系统域名',width:fixWidth(0.2),align:'center'},
		                 {field:'sysType',title:'系统类别',width:fixWidth(0.1),align:'center'},
		                 {field:'opt',title:'操作',width:fixWidth(0.25),align:'center', formatter:function(value, row, index) {
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
				$('.datagrid').undelegate('a[name=editBtn]','click',showEditDialog);
				$('.datagrid').undelegate('a[name=delBtn]','click',delSystem);
				
				$('.datagrid').delegate('a[name=editBtn]','click',showEditDialog);
				$('.datagrid').delegate('a[name=delBtn]','click',delSystem);
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
		orderinit("../../system.do?method=systemList");
	}
	
	var initUI = function(){
		$('#form').delegate('#addSystem','click',showSystemDialog);
	}
	initUI();
	doSearch();
});

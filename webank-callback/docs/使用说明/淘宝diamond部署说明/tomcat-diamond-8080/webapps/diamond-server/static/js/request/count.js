//页面加载时，立即加载数据
$(function() {
	var addRequestCount = function(){
		var count = $.trim($('#count').val());
		if(count == ''){
			$.messager.alert('操作提示', '请输入值!', 'error');
			return false;
		}
		var dataModel = {};
		dataModel.count = count;
		var url = '../../tshAdmin.do?method=setRefuseRequestCount';
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
					$('#count-view').val(count);
					$('#count').val('');
				}
				$.messager.alert('提示', data.message, 'info');
			},error:function(data){
				$.messager.alert('提示', '设置失败!', 'error');
			}
		});
	}
	var requestDialog = function(data){
		var html = $('#request-dialog-template').html();
		$(html).dialog({  
		    title: '设置拒绝请求参数',  
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
					addRequestCount();
				}
			}],onOpen:function(){
				$('#count-view').val(data.count);
			},
			onClose:function(){
				$('#request-dialog').dialog('destroy');
		    }
		});  
	}
	var getRequestCount = function(){
		var url = '../../tshAdmin.do?method=getRefuseRequestCount';
		$.ajax({
			url:url,
			traditional:true,
			async:true,
			cache:false,
			success:function(data){
				requestDialog(data);
			},error:function(data){
				$.messager.alert('提示', '获取信息失败!', 'error');
			}
		});
	}
	getRequestCount();
});

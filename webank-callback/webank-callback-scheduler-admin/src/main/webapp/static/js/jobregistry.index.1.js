$(function() {
	// init date tables
	window.jobTable = $("#job_list").dataTable({
		"deferRender": true,
		"processing" : true, 
	    "serverSide": true,
		"ajax": {
			url: base_url + "/jobregistry/pageList",
			type:"post",
	        data : function ( d ) {
	        	var obj = {};
	        	obj.executorClient = $('#executorClient').val();
	        	obj.clientName = $('#clientName').val();
	        	obj.start = d.start;
	        	obj.length = d.length;
                return obj;
            }
	    },
	    "searching": false,
	    "ordering": false,
	    //"scrollX": true,	// X轴滚动条，取消自适应
	    "columns": [
	                { "data": 'id', "bSortable": false, "visible" : false},
	                { 
	                	"data": 'registryGroup', 
	                	"visible" : true,
	                	"render": function ( data, type, row ) {
	            			return data;
	            		}
            		},
            		 { 
	                	"data": 'registryKey', "visible" : true},
            		 { 
	                	"data": 'registryValue', "visible" : true},
            		 { 
	                	"data": 'executorClient', "visible" : true},
            		 { 
	                	"data": 'clientName', "visible" : true},
            		 { 
	                	"data": 'ifGrant', 
	                	"visible" : true,
	                	"render": function ( data, type, row ) {
	            			return data== 0?'<span style="color:red;">未授权</span>':data== 1?'已授权':'<span style="color:red;">授权失败</span>';
	            		}
            		},
//            		 {  "data": 'aesKey', "visible" : true},
//            		 {  "data": 'accessToken', "visible" : true},
            		 { 
	                	"data": 'crtTime', 
	                	"visible" : true,
	                	"render": function ( data, type, row ) {
	                		return data?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
	            		}
            		},
            		 { 
	                	"data": 'updateTime', 
	                	"visible" : true,
	                	"render": function ( data, type, row ) {
	                		return data?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
	            		}
            		},
	                {
						"data": '操作' ,
						"width":'15%',
	                	"render": function ( data, type, row ) {
	                		row['grantMsg']= ((row.ifGrant== 0)?'授权': '取消授权');
	                		return function(){
	                			//未授权
	                			var ifGrant= row.ifGrant,html='',aesKey= row.aesKey,dataRow=JSON.stringify(row).replace(/\"/g,'\'');
	                			html+= '<button class="btn btn-primary btn-xs job_operate" data-id="'+row.id+'" data-row="'+dataRow+'" onclick="grantHandler(this);" _type="job_trigger" type="button">' +((ifGrant== 0)?'授权': '取消授权')+ '</button>';
	                			if(aesKey== '' || aesKey== null){
	                				html+= '<button class="btn btn-warning btn-xs job_operate" data-id="'+row.id+'" onclick="secretKeyHandler(this);" _type="job_trigger" type="button">' +'生成密钥与令牌'+ '</button>';
	                			}
	                			html+= '<button class="btn btn-warning btn-xs job_operate" data-id="'+row.id+'" onclick="secretKeyHandlerTest(this);" _type="job_trigger" type="button">' +'AES加密测试'+ '</button>';
	                			//html+= '<button class="btn btn-danger btn-xs job_operate" data-id="'+row.id+'" data-groupId="'+row.groupId+'" onclick="relationGroup(this);" _type="job_trigger" type="button">' +'关联分组'+ '</button>';
	                			return html;
							};
	                	}
	                }
	            ],
		"language" : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "每页 _MENU_ 条记录",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )",
			"sInfoEmpty" : "无记录",
			"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中数据为空",
			"sLoadingRecords" : "载入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首页",
				"sPrevious" : "上页",
				"sNext" : "下页",
				"sLast" : "末页"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}
	});
});
//关联分组
function relationGroup(elem){
	var dataId= $(elem).attr('data-id'),groupId=$(elem).attr('data-groupId');
	$("#id").val(dataId);
	if(groupId> -1){
		$('#relationGroup').find('option').each(function(){
			if($(this).val()== groupId){
				$(this).attr('selected',true);
			}
		});
	}
	$('#relationModal').modal({backdrop: false, keyboard: false}).modal('show');
}
//添加关联分组
function addRelationGroup(){
	$.post(base_url + '/jobregistry/addRelationGroup',{id: $('#id').val(),groupId: $('#relationGroup').find('option:selected').val()},function(data){
		$('#relationCancel').click();
		layer.msg(data.msg, {
			icon : 1,
			shade : [ 0.3, 'rgb(0, 0, 0)' ]
		})
		if(data.code== 200){
			$('#job_list').dataTable().fnClearTable();  //列表刷新
		}
	},'json');
}
//授权
function grantHandler(elem){
	var dataRow= $(elem).attr('data-row'),row=eval('(' + dataRow.toString() + ')');
	//判断是否已生成密钥
	if(row.aesKey== '' || row.aesKey== null || row.aesKey== 'null'){
		layer.msg('未生成密钥与授权令牌', {
			icon : 2,
			shade : [ 0.3, 'rgb(0, 0, 0)' ]
		})
		return;
	}
	layer.confirm('确定'+row['grantMsg'] + '?',
			{
				btn : [ '确定', '取消' ], //按钮
				title : '授权确认',
				area : [ '300px', '150px' ],
			},
			function() {
				layer.closeAll();
				//确定操作
				$.ajax({
					url : base_url + '/jobregistry/updateRegistGrant',
					data : {id: row.id,ifGrant: row.ifGrant== 0?1:0},
					async : false,
					type : 'post',
					dataType : 'json',
					success : function(data) {
						if(data.code== 200){
							$('#job_list').dataTable().fnClearTable();  //列表刷新
							layer.msg(row['grantMsg']+'成功', {
								icon : 1,
								shade : [ 0.3, 'rgb(0, 0, 0)' ]
							})
						}else{
							layer.msg(row['grantMsg']+'失败', {
								icon : 2,
								shade : [ 0.3, 'rgb(0, 0, 0)' ]
							})
						}
					},
					error : function() {
						layer.msg('服务器异常,请联系管理员', {
							icon : 1,
							shade : [ 0.3, 'rgb(0, 0, 0)' ]
						})
					}
				});
			}, function() {
				//取消操作
			});
}
//生成密钥与令牌
function secretKeyHandler(elem){
	var dataId= $(elem).attr('data-id');
	$.post(base_url + '/jobregistry/addSecretKey',{id: dataId},function(data){
		$('#job_list').dataTable().fnClearTable();  //列表刷新
		if(data.code == 200){
			layer.msg('生成密钥与授权令牌成功', {
				icon : 1,
				shade : [ 0.3, 'rgb(0, 0, 0)' ]
			})
		}else{
			layer.msg('生成密钥与授权令牌失败', {
				icon : 2,
				shade : [ 0.3, 'rgb(0, 0, 0)' ]
			})
		}
	},'json');
}
function secretKeyHandlerTest(){
	$.post(base_url + '/jobregistry/secretKeyTest',{},function(data){
		$('#job_list').dataTable().fnClearTable();  //列表刷新
		if(data.code == 200){
			layer.msg('AES加密测试成功', {
				icon : 1,
				shade : [ 0.3, 'rgb(0, 0, 0)' ]
			})
		}else{
			layer.msg('AES加密测试失败', {
				icon : 2,
				shade : [ 0.3, 'rgb(0, 0, 0)' ]
			})
		}
	},'json');
}











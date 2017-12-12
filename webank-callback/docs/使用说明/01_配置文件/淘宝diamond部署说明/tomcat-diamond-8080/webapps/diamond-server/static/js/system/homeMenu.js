//页面加载时，立即加载数据
$(function() {
	var initMenu = function(data){
		var menu = "";
		$.each(data,function(i,v){
			menu += '<div title="'+k.menuPath+'"  >';
			$.each(data.childNode,function(j,k){
				menu += '<a href="javascript:void(0);" src="'+k.menuPath+'" class="cs-navi-tab" title="'+k.menuName+'" url="'+k.menuPath+'" >'+k.menuName+'</a>';
			});
			menu += '</div>';
		});
		$('#accordion').html(menu);
	}
	var initUI = function(){
		var url = "../../diamondMenu.do?method=getWebPlatMenu";
		$.ajax({
			url:url,
			traditional:true,
			type:'get',
			async:true,
			data:dataModel,
			cache:false,
			success:function(data){
				initMenu(data);
			},error:function(data){
				$.messager.alert('提示', '操作失败!', 'error');
			}
		});
	}
	initUI();
});

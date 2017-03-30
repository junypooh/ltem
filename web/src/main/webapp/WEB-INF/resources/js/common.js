// gnb
function gnb(){
	$('.btn_gnb').click(function(){
		$(this).toggleClass('on');
		$('#gnb').toggleClass('on');
	});
}

$(function(){

	clickView();
	inputFile();
	tabview();
	gnb();

	//login

	$('.box_login input[type="text"]').click(function(){
		$(this).attr("value","");
		$(this).css("background","none");
	});


	$('.box_login input[type="password"]').click(function(){
		$(this).css("background","none");
	});

	$(window).on('resize', function(){
		var tx = ($(window).width()-$(".wrap_layerpop>.layerpop").width())/2;
		var ty = ($(window).height()-$(".wrap_layerpop>.layerpop").height())/2;
		$(".wrap_layerpop>.layerpop").css({left:tx+"px",top:ty+"px"});
	})
});


function layerPopDimmOn(){
	$('body').css({'overflow':'hidden', 'position': 'fixed', 'width':'100%', 'height':'100%'});
}
function layerPopDimmOff(){
	$('body').css({'overflow':'auto', 'position': 'relative'});
	$('.wrap_layerpop').css('overflow','initial');
}

function layerOpen(el){
	$("#"+el).css("display","block");
	var tx = ($(window).width()-$("#"+el).find('>div').width())/2;
	var ty = ($(window).height()-$("#"+el).find('>div').height())/2;
	//console.log($(window).width()+", "+$(window).height()+", "+$("#"+el).find('>div').width()+", "+$("#"+el).find('>div').height())
	//console.log(tx+", "+ty)
	$("#"+el).find('>div').css({left:tx+"px",top:ty+"px"});
	layerPopDimmOn();
	// if ($(window).height() <= $("#"+el).find('>div').height()){
	// 	$('.wrap_layerpop').css('overflow','auto');
	// } else {
	// 	$('.wrap_layerpop').css('overflow','initial');
	// }
}

function clickView(){
	$('.area_sup .btn_allview').click(function(){
		$(this).toggleClass('on');
		$(this).parent().parent().next('.supervisor').toggleClass('on');
		$(this).parent().parents().siblings('.supervisor_mod').removeClass('on');
	});

	$('.area_txt .btn_garw2').click(function(){
		$(this).toggleClass('on');
		$(this).next('.supervisor').toggleClass('on');
	});

	$('.supervisor .btn_mod').click(function(){
		$(this).parent().parent('.supervisor').removeClass('on');
		$(this).parent().parents().siblings('.supervisor_mod').addClass('on');
	});

	$('.supervisor_mod .btn_cancel').click(function(){
		$(this).parent().parents().siblings('.supervisor').addClass('on');
		$(this).parent().parent('.supervisor_mod').removeClass('on');
	});

	$('.supervisor_mod .btn_del').click(function(){
		$(this).parent('span').css("display", "none");
	});

	/* 개발 코드로 이동
	$('.list_tit .btn_tit button').click(function(){
		$(this).toggleClass('on');
		$(".box_listtop .area_pop").toggleClass('on');
	});*/

	/* 개발 코드로 이동
	$('.area_pop .tit ul li > button').click(function(){
		$(this).parent('li').addClass('on');
		$(this).parent().siblings('li').removeClass('on');
	});*/

	/* 개발코드로 이동
	$('.area_inp .btn_conf_aw').click(function(){
		$(this).toggleClass('on');
		$(".box_newap").toggleClass('on');
		$(".box_inphid").toggleClass('on');
	});*/

	$('.area_inp .file_inp .btn_del').click(function(){
		$(this).parent('span').remove();
	});

	$('.area_inp .sp_del .btn_del').click(function(){
		$(this).siblings('input').val("");
		$(this).siblings('input').attr("placeholder","");
		$(this).remove();
	});

	//pop
	$('.wrap_layerpop .btn_close button').click(function(){
		$('.wrap_layerpop').hide();
	});

	$('.wrap_layerpop .btn_close2 button').click(function(){
		$('.wrap_layerpop').hide();
	});

	$('.wrap_layerpop .search_choice button').click(function(){
		$(this).parent().parent('li').remove();
	});

	/*	개발 코드로 이동
	 $('.area_inp .btn_add').click(function(){
         $('.view_list').addClass('on');
         var numchk = $('.area_inp .view_list li').length;
         console.log(numchk);
	 });
	 */

	/*	개발 코드로 이동
	$('.area_inp .view_list li .btn_del').click(function(){
		$(this).parent().parent('li').remove();

		var numchk = $('.area_inp .view_list li').length;
		console.log(numchk);
		if(numchk == 0){
			$('.area_inp .view_list').removeClass('on');
		}
	});
	 */

}

function tabview(){
	/* 개발 코드로 이동
	$('.box_ullist > ul > li').click(function(){
		if($(this).children('.box_chart').hasClass('on')) {
			$(this).children('.box_chart').removeClass('on');
		} else {
			$(this).children('.box_chart').addClass('on');
			$(this).siblings('li').children('.box_chart').removeClass('on');
		}
	});
	*/


	//서비스소개 tab
	$('.tab_info > ul > li').click(function(){
		$(this).addClass('on');
		$(this).siblings('li').removeClass('on');

		var numchk1 = $(this).attr('class');
		var itemchk1 = numchk1.substr(3,1);
		console.log(itemchk1);

		$('.tf_cont').removeClass('on');
		$('.tf_cont'+itemchk1).addClass('on');

	});
}

//input file
function inputFile(){
	var fileTarget = $('.file_inp .hidden');

    fileTarget.on('change', function(){
        if(window.FileReader){
            var filename = $(this)[0].files[0].name;
        } else {
            var filename = $(this).val().split('/').pop().split('\\').pop();
        }

        $(this).siblings('.uploadfile').val(filename);
    });
}
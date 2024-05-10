$(function(){
	
	//건너뛰기 링크
	$("#skip_navi a").on('click', function() {
		var activeTab = $(this).attr("href");
		$(activeTab).attr('tabindex','-1').focus();
	});

	$("#header").on("mouseenter focusin",function(){
		$('#header').addClass("over");
	}).on("mouseleave focusout",function(){
		$('#header').removeClass("over");
	});

	//상단 메뉴
	$("#gnb").each(function(){
		$(".gnb li li.on",this).parents("li").addClass("on");
		$(".gnb > li", this).on("mouseenter focusin",function(){
			$(this).addClass("hv");
		}).on("mouseleave focusout",function(){
			$(this).removeClass("hv");
		});
		$('.gnb',this).on("mouseenter focusin",function(){
			$(this).addClass("on").stop(true,true).animate({"height":"320px"},300, 'easeOutExpo');//높이 지정(메뉴 추가시 조절)
			$('#header').addClass("online");
		}).on("mouseleave",function(){
			$(this).removeClass("on").stop(true,true).animate({"height":"100px"},300, 'easeOutExpo');
			$('#header').removeClass("online");
		});
	});
	$('*:not("#gnb a")').focus(function(){
		$(".gnb").removeClass("hv")
		$(".gnb").removeClass("on").stop(true,true).animate({"height":"100px"},300, 'easeOutExpo');
	});
	
	//메뉴 좌측에서 우측으로 이동
	$(".btnMenu").click(function(){
		$(".fix_bg").show();
		$("#mGnb").stop().animate( { right: "0" }, 200, 'easeOutExpo' );
		$("#wrapper").addClass("wra_box");
		$("#mGnb .btnClose, .fix_bg").click(function(){
			$("#mGnb").stop().animate( { right: "-100%" }, 200, 'easeOutExpo' );
			$("#wrapper").removeClass("wra_box");
			$(".fix_bg").hide();
			return false;
		});
		return false;
	});

	//모바일메뉴
	$('#mGnb li.has-sub > a').on('click', function(){
		//$(this).removeAttr('href');
		var element = $(this).parent('li');
		if (element.hasClass('open')) {
			element.removeClass('open');
			element.find('li').removeClass('open');
			element.find('ul').slideUp('fast');
		}
		else {
			element.addClass('open');
			element.children('ul').slideDown('fast');
			element.siblings('li').children('ul').slideUp('fast');
			element.siblings('li').removeClass('open');
			element.siblings('li').find('li').removeClass('open');
			element.siblings('li').find('ul').slideUp('fast');
		}
		return false;
	});

	//왼쪽메뉴
	$('#lnb li button').click(function(){
		$('#lnb li button').removeClass('on');
		$('#lnb li ul').slideUp('fast');
		if($('+ul',this).is(':hidden')){
			$(this).addClass('on').next().slideDown('fast');
		}else{
			$(this).removeClass('on').next().slideUp('fast');
		}
		return false;
	});
	$('*:not("#lnb li button")').click(function(){
		$('#lnb li button').removeClass('on');
		$('#lnb li ul').slideUp('fast');
	});
	
})







$(document).ready(function(){
	$('.area_subVisual .lnb.mobile .small').click(function(){
		$(this).children('ul').slideToggle(300,'linear');
		$(this).toggleClass('active');
	});
});


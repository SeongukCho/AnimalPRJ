$(function(){
	
	$(window).scroll( function() {
		var scrollTop = $(window).scrollTop();
		if (scrollTop){
			$("#header").addClass("fix");
		} else {
			$("#header").removeClass("fix");
		}
	});


	$('.fFamily button').click(function(){
		$('.fFamily ul').slideToggle('fast');
		$(this).toggleClass('on');
	})
	
	$(window).scroll(function () {
		if($(document).scrollTop()>100){ 
			$('.btnTop').fadeIn();
		}else {
			$('.btnTop').fadeOut();
		}
	})
	 //TOP 이동
	$('.btnTop').click(function(e){
        e.preventDefault();
         $('html,body').stop().animate({scrollTop:0},200);
    });

	//탭메뉴 - 모바일
	$(".mb_tab").each(function(){
		var mbstr = $("ul li.on a",this).html();
		$(".selet_txt",this).html(mbstr);
		$(".selet_txt",this).click(function(){
			$("+ul",this).slideToggle("fast");
			$(this).toggleClass("on");
			$("+ul li a",this).click(function(){
				var mbstr2 = $(this).html();
				//$(this).parent().parent().siblings('.selet_txt').html(mbstr2);
				$(this).parent().parent().hide();
				$(".selet_txt").removeClass('on');
			});
			return false;
		});
	});
})


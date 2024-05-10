jQuery(function(){
	$(window).load(function() {
		$('#conFullPage .section1 .introTxt').addClass('firstAct');
	});
	
	//메인 fullpage
	var myFullpage = new fullpage('#conFullPage', {
		licenseKey: 'CA67F728-FC51494F-8E8BA956-B6C48EE9',
		verticalCentered: true, //콘텐츠 중앙정렬
		//anchors: ['firstPage', 'secondPage', '3rdPage', 'footPage'],
		navigation: false,
		scrollOverflow: false,
		responsiveWidth: 1200,
		easing: 'easeInOutExpo',
		css3: true,
		scrollBar: false,
		onLeave: function(origin, destination, direction){
			var leavingSection = this;
			if(origin.index == 2 && direction =='down'){
				$('#footer').addClass('on');
			}else if(origin.index == 3 && direction == 'up'){
				$('#footer').removeClass('on');
			}else{
				$('#footer').removeClass('on');
			}
		},
		afterLoad: function(anchorLink, index){
			if(index){
				$('.section.active').addClass('motion');
			}
		}
	});

	$('.autoDown').click(function() {
		myFullpage.moveSectionDown();
	});
	$('.autoUp').click(function() {
		myFullpage.moveSectionUp();
	});
	
	// 접근성 보강 포커스이동
	$("#conFullPage #section3 a" ).focus(function() {
		$('#footer').addClass('on');
	});
	
	var $vSlide = $('.mVisual');
	var $slidContr1_1 = $vSlide.find('.control_box .control');
	var swiper1 = new Swiper('.mVisual', {
		effect: 'fade',
		speed: 500,
		autoplay: 2500,
        autoplayDisableOnInteraction: false,
		loop: true,
		pagination: '.mPage',
        paginationType: 'fraction',
		nextButton: '.nextSlide',
        prevButton: '.prevSlide',
		onSlideChangeStart: function(swiper){ //접근성 추가 - tab 포커스이동
			$('.mVisual li').find('a').attr('tabindex', -1);
			$('.mVisual li.swiper-slide-active').find('a').attr('tabindex', 0);
		}
	});
	$slidContr1_1.find( '.startSlide' ).on( 'click', function () {
		swiper1.startAutoplay();
		$(this).hide().prev().show().focus();
	});
	$slidContr1_1.find( '.stopSlide' ).on( 'click', function () {
		swiper1.stopAutoplay();
		$(this).hide().next().show().focus();
	});
	$('.mVisual li' ).focusin( function () {
		swiper1.stopAutoplay();
		$('.mVisual').find( '.stopSlide' ).hide().next().show();
	});
	/*$( ".mVisual li" ).hover(
      function() {
        swiper1.autoplay.stop();
      }, function() {
        swiper1.autoplay.start();
      }
    );*/

	$(".mNotice").each(function(){
		var $tabCon = $('> ul > li',this)
		$tabCon.find('> a').on("click", function () {
			$tabCon.removeClass("on");
			$tabCon.find('.mTabCon').hide();
			$(this).parent().addClass("on");
			$(this).next().show();
			return false;
		});
	});
	
	var $pSlide = $('.mCon1');
	var $slidContr2 = $pSlide.find('.control');
	var swiper2 = new Swiper('.mCon1', {
		autoplay: 4000,
        autoplayDisableOnInteraction: false,
		loop: true,
		pagination: '.mPage2',
		paginationClickable: true,
        paginationBulletRender: function (swiper, index, className) {
            return '<button type="button" class="' + className + '">' + (index + 1) + '</button>';
        },
		onSlideChangeStart: function(swiper){ //접근성 추가 - tab 포커스이동
			$('.mCon1 li').find('a').attr('tabindex', -1);
			$('.mCon1 li.swiper-slide-active').find('a').attr('tabindex', 0);
		}
	});
	$slidContr2.find( '.startSlide2' ).on( 'click', function () {
		swiper2.startAutoplay();
		$(this).hide().prev().show().focus();
	});
	$slidContr2.find( '.stopSlide2' ).on( 'click', function () {
		swiper2.stopAutoplay();
		$(this).hide().next().show().focus();
	});
	$('.mCon1 li' ).focusin( function () {
		swiper2.stopAutoplay();
		$('.mCon1').find( '.stopSlide2' ).hide().next().show();
	});

	var $bSlide = $('.mBanner');
	var $slidContr3 = $bSlide.find('.control');
	var swiper3 = new Swiper('.banSlide', {
		autoplay: 4000,
        autoplayDisableOnInteraction: false,
		loop: true,
		nextButton: '.nextSlide3',
        prevButton: '.prevSlide3',
		slidesPerView: 6,
		spaceBetween: 8,
		onSlideChangeStart: function(swiper){ //접근성 추가 - tab 포커스이동
			$('.banSlide li').find('a').attr('tabindex', -1);
			$('.banSlide li.swiper-slide-active, .banSlide li.swiper-slide-active + li, .banSlide li.swiper-slide-active + li + li, .banSlide li.swiper-slide-active + li + li + li, .banSlide li.swiper-slide-active + li + li + li + li, .banSlide li.swiper-slide-active + li + li + li + li + li').find('a').attr('tabindex', 0);
		},
		breakpoints: {
			1200: {
				slidesPerView: 5,
				spaceBetween: 8,
			},
			1000: {
				slidesPerView: 4,
				spaceBetween: 5,
			},
			640: {
				slidesPerView: 3,
				spaceBetween: 5,
			},
			400: {
				slidesPerView: 2,
				spaceBetween: 5,
			}
		},
	});
	$slidContr3.find( '.startSlide3' ).on( 'click', function () {
		swiper3.startAutoplay();
		$(this).hide().prev().show().focus();
	});
	$slidContr3.find( '.stopSlide3' ).on( 'click', function () {
		swiper3.stopAutoplay();
		$(this).hide().next().show().focus();
	});
	$('.banSlide li' ).focusin( function () {
		swiper3.stopAutoplay();
		$('.mBanner').find( '.stopSlide3' ).hide().next().show();
	});
});
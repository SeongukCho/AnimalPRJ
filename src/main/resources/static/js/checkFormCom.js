/***********************************
 jQuery로 form reset 
 : $(obj).reset()
***********************************/
jQuery.fn.reset = function () {
	$(this).each (function() { this.reset(); });
};

/******************************************
공백 제거
******************************************/
String.prototype.trim = function() {
	return this.replace(/^\s*|\s*$/g, '');
};

/******************************************
공백 체크
******************************************/
function fn_checkSpace( str ) {
	if(str.search(/\s/) != -1)
		return true;
	else 
		return false;
}

/******************************************
숫자만 입력 : style="ime-mode:disabled;" 함께 구현되어야 함
******************************************/
var varOnlyNumKDKeyCode = false;
function fn_onlyNumKD(e, theObj) {

	var varKeyCode = e.keyCode;
	if(varKeyCode == 17) {
		varOnlyNumKDKeyCode = varKeyCode;
		return true;
	}
	//alert(varOnlyNumKDKeyCode +":" + varKeyCode);
	if(varOnlyNumKDKeyCode == 17 && varKeyCode == 86) {
		// 붙여넣기
		varOnlyNumKDKeyCode = false;
		return true; 
	}
	if (varKeyCode != 0 && varKeyCode != 8 && varKeyCode != 9 && varKeyCode != 13 && varKeyCode != 46 && (varKeyCode < 35 || varKeyCode > 40 && varKeyCode < 48 || varKeyCode > 57 && varKeyCode < 96 || varKeyCode > 105)){
		return false;
	}
	varOnlyNumKDKeyCode = false;
	return true;
}

/******************************************
숫자(소수점 포함)만 입력 : style="ime-mode:disabled;" 함께 구현되어야 함
******************************************/
function fn_onlyNumDotKD(e, theObj) {
	var varKeyCode = e.keyCode;
	var varEventElement = theObj;
	var varVal = $(varEventElement).val();
	if(varKeyCode == 37 || varKeyCode == 39) return true;
	if (varVal.trim() == '' && (varKeyCode == 190 || varKeyCode == 110) || (varKeyCode != 190 && varKeyCode != 110 && varKeyCode != 0 && varKeyCode != 8 && varKeyCode != 9 && varKeyCode != 13 && 
			(varKeyCode != 45 && varKeyCode != 46 && (varKeyCode < 48 || varKeyCode > 57 && varKeyCode < 96 || varKeyCode > 105) || 
			((varVal == '' || varVal.indexOf('.') != -1) && varKeyCode == 46) || 
			(varVal != '' && varKeyCode == 45) /*||
			(varVal == '0' && varKeyCode != 46)*/))){
		return false;
	}
	
	var varDotChk = varVal.match(/[.]/g);
	if(varDotChk && varDotChk.length >= 1 && (varKeyCode == 190 || varKeyCode == 110)) return false;
	
	return true;
}

function fn_preventZeroHan(e, theObj) {
	var varRegTxt = /^0.*/g;
	var varHanRegTxt = /[ㄱ-ㅎㅏ-ㅣ가-힣]/g;
	var varChkVal = $(theObj).val();
	var varHasZero = varChkVal.match(varRegTxt);
	var varHasHan = varChkVal.match(varHanRegTxt);
	var varVal = $(theObj).val();
	if(varHasZero && varChkVal != '0' && varChkVal.indexOf(".") == -1) {
		varVal = varVal.replace('0', '');
		var varObjLen = $(theObj).attr("maxlength");
		var varValLen = varVal.length;
		if(varValLen > varObjLen) varVal = varVal.substring(0, varObjLen);
		$(theObj).val(varVal);
	}
	if(varHasHan) {
		varVal = varVal.replace(varRegTxt, '');
		var varObjLen = $(theObj).attr("maxlength");
		var varValLen = varVal.length;
		if(varValLen > varObjLen) varVal = varVal.substring(0, varObjLen);
		$(theObj).val(varVal);
	}
}

/******************************************
 * 한글입력있는 경우 한글만 지우기, 공백만 남은 경우 공백 지우기
 * @param e
 * @param theObj
 * @returns {Boolean}
 ******************************************/
function fn_preventHan(e, theObj, thePasteVal) {
	var varRegTxt = /[ㄱ-ㅎㅏ-ㅣ가-힣]/g;
	var varChkVal = (typeof(thePasteVal) == "undefined")?$(theObj).val():thePasteVal;
	var varHasHan = varChkVal.match(varRegTxt);
	var varPasteLen = (typeof(thePasteVal) == "undefined")?0:thePasteVal.length;
	var varVal = $(theObj).val();
	if(varHasHan && varPasteLen == 0) {
		varVal = varVal.replace(varRegTxt, '');
		var varObjLen = $(theObj).attr("maxlength");
		var varValLen = varVal.length;
		if(varValLen > varObjLen) varVal = varVal.substring(0, varObjLen);
		$(theObj).val(varVal);
	} else if(!varHasHan && varPasteLen > 0) {
		varVal = varVal + thePasteVal;
		var varObjLen = $(theObj).attr("maxlength");
		var varValLen = varVal.length;
		if(varValLen > varObjLen) varVal = varVal.substring(0, varObjLen);
		$(theObj).val(varVal);
	}
}


/*******************************************
 window resizeTo
 *******************************************/
var fn_window = {
		width:950
		, height:850
		, resizeTo:function(theWidth, theHeight){
			if(typeof(theWidth) == 'undefined' || theWidth == 0) theWidth = fn_window.width;
			if(typeof(theHeight) == 'undefined' || theHeight == 0) theHeight = fn_window.height;
			window.resizeTo(theWidth, theHeight);
			window.focus();
		}
}

/*
// 페이지이동시 window.open 창 닫기 : 이미 닫힌 경우 메시지 안 띄움
$(function(){
	$(window).on("beforeunload", function(){
		if(fn_dialog.openWin){
			var varCWinLen = fn_dialog.openWin.length;
			if(varCWinLen > 0) {
				var varOW = false;
				for(var i = 0 ; i < varCWinLen; i ++) {
					try {
						if(fn_dialog.openWin[i].document) {
							varOW = true;
							break;
						}
					} catch(e) {}
				}
				if(varOW) return "이 페이지에서 나가시면 새창이 닫힙니다.";
			}
		}
	});
	$(window).on("unload", function(){
		fn_checkWinOpen(window);
	});
	
	function fn_checkWinOpen(theWin) {
		try {
			if(theWin.fn_dialog.openWin){
				var varCWinLen = theWin.fn_dialog.openWin.length;
				if(varCWinLen > 0) {
					for(var i = 0 ; i < varCWinLen; i ++) {
						fn_checkWinOpen(theWin.fn_dialog.openWin[i]);
						theWin.fn_dialog.openWin[i].close();
					}
				}
			}
		}catch(e){}
	}
});
*/

/***********************************
새창 dialog 띄우기
fn_dialog.init	: dialog setting
fn_dialog.open  : dislog open
***********************************/
var fn_dialog = {
		dId:false
		, fId:false
		, width:950
		, height:800
		, openWin:[]
		, init:function(theDId, theZIndex, theWidth, theHeight){
			if(typeof(theDId) == 'undefined') theDId = "fn_default";
			if(typeof(theWidth) == 'undefined') theWidth = fn_dialog.width;
			if(typeof(theHeight) == 'undefined') theHeight = fn_dialog.height;
			
			var varDId = theDId + "_dialog";
			var varFId = theDId + "_iframe";

			fn_dialog.dId = varDId;
			fn_dialog.fId = varFId;
		}, open:function(theTitle, theAction, theWidth, theHeight, theDid){
			
			var varConfirm = confirm(fn_Message.popupConfirmText);
			if(!varConfirm) return false;
			
			if(typeof(theTitle) != 'undefined' && theTitle != "") theAction += "&tit=" + encodeURIComponent(theTitle);
			var varWidth, varHeight;
			if(typeof(theWidth) != 'undefined' && theWidth > 0)	varWidth = theWidth;
			else varWidth = fn_dialog.width;
			if(typeof(theHeight) != 'undefined' && theHeight > 0) varHeight = theHeight;
			else varHeight = fn_dialog.height;
			var varDid = fn_dialog.dId;
			if(typeof(theDid) != 'undefined') varDid = theDid;
			
			fn_dialog.openWin.push(window.open(theAction, varDid, "width=" + varWidth + ", height=" + varHeight/* + ", innerWidth=" + (varWidth - 150) + ", innerHeight=" + (varHeight - 150)*/ + ", scrollbars=yes"));
		}, openP:function(theTitle, theAction, theDid, theProperties){
			
			var varConfirm = confirm(fn_Message.popupConfirmText);
			if(!varConfirm) return false;
			
			if(typeof(theTitle) != 'undefined') theAction += "&tit=" + encodeURIComponent(theTitle);
			var varDid = fn_dialog.dId;
			if(typeof(theDid) != 'undefined') varDid = theDid;
			fn_dialog.openWin.push(window.open(theAction, varDid, theProperties));
		}, openF:function(theAction, theDid){
			// iframe으로 이동
			window.open(theAction, theDid);
		}, closeAll:function(){
			for(var wi = 0, varWin ; varWin = fn_dialog.openWin[wi]; wi ++) {
				try{if(varWin) varWin.close();}catch(e){}
			}
		}, closeReload:function(){
			/*$("#" + fn_dialog.dId).dialog("option", "close", function(){
				location.reload();
			});*/
		}
	};


var fn_dialog_div = {
		dId:false
		, init:function(theDId, theZIndex, theWidth, theHeight){
			if(typeof(theDId) == 'undefined') theDId = "fn_default";
			if(typeof(theZIndex) == 'undefined') theZIndex = 10006;
			if(typeof(theWidth) == 'undefined') theWidth = 950;
			if(typeof(theHeight) == 'undefined') theHeight = 800;
			
			var varDId = theDId + "_dialog";
			var varFId = theDId + "_iframe";
			$("#" + varDId).dialog({
				bgifram: true,
		   		autoOpen: false,
				resizable:false,
		   		modal: true
		   	});

			$("#" + varDId).dialog("option", "width", theWidth);
			$("#" + varDId).dialog("option", "height", theHeight);
			$("#" + varDId).css("z-index",theZIndex + 1);
			$("#" + varDId).css("overflow",'hidden');
			$("#" + varDId).css("background",'#FFF');
			$("#" + varDId).css("padding",'0px');
			$("#" + varDId).parent().css({"z-index":theZIndex, "padding":"0px","border":"0px","background":"transparent"});

			fn_dialog_div.dId = varDId;
			fn_dialog_div.fId = varFId;
			
			/*if(!$('#' + varFId).is('div')) {
				$("body").append("<iframe id=\"" + varFId + "\" frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"yes\" style=\"width:100%; height:100%\"></iframe>");
			}*/
				
		}, open:function(theTitle, theAction, theWidth, theHeight){
			if(typeof(theTitle) != 'undefined') $("#" + fn_dialog_div.dId).dialog("option", "title", theTitle);
			
			var varIsIfm = $("#" + fn_dialog_div.fId).size() > 0;
			if(typeof(theWidth) != 'undefined' && theWidth > 0)	{
				if(varIsIfm) $("#" + fn_dialog_div.fId).css("width", theWidth);
				$("#" + fn_dialog_div.dId).dialog("option", "width", theWidth);
			}
			if(typeof(theHeight) != 'undefined' && theHeight > 0) {
				if(varIsIfm) $("#" + fn_dialog_div.fId).css("height", theHeight);
				$("#" + fn_dialog_div.dId).dialog("option", "height", theHeight);
			}
			if(varIsIfm) {
				if(typeof(theAction) != 'undefined') eval(fn_dialog_div.fId + ".location.replace('" + theAction + "');");
			   	$("#" + fn_dialog_div.fId).load(function(){
			   		$("#" + fn_dialog_div.dId).dialog("option", "position", {
						my: "center",
						at: "center"});
					$("#" + fn_dialog_div.dId).dialog("open");
				});
			} else {
		   		$("#" + fn_dialog_div.dId).dialog("option", "position", {
					my: "center",
					at: "center"});
				$("#" + fn_dialog_div.dId).dialog("open");
			}
			
		}, closeReload:function(){
			$("#" + fn_dialog_div.dId).dialog("option", "close", function(){
				location.reload();
			});
		}
	};

/**
 * dialog background div 생성
 * @param thePObj
 * @param theId
 * @param theStyle
 */
var fn_dialog_layer = {
	dId:false
	, open:function(thePObj, theId, theStyle){
		if(typeof(theId) == 'undefined' || !fn_isValFill(theId)) return;
		fn_dialog_layer.dId = theId;
		if(!$('#' + fn_dialog_layer.dId).is('div')) {
			thePObj.append("<div id=\"" + fn_dialog_layer.dId + "\" style=\"position:absolute;background:#eee;opacity:0.7;" + theStyle + "\"></div>");
		} else $("#" + fn_dialog_layer.dId).show();
	}, close:function(){
		$("#" + fn_dialog_layer.dId).hide();
	}
};

/**
 * 처리 후 location.reload();
 */
function fn_procReload() {
	try{
		//alert((window != top) + ":" + (parent != top));
		if(opener) {
			opener.location.reload();
			location.reload();
		} else if(window != top) {
			try{
				parent.fn_dialog.closeReload();
			}catch(e){}
			location.reload();
		} else {
			try{
				top.fn_dialog.closeReload();
			}catch(e){}
			top.location.reload();
		}
	}catch(e){
		try{
			top.fn_dialog.closeReload();
		}catch(e){}
		top.location.reload();
	}
}

/**
 * 처리 후 top.location.reload();
 */
function fn_procTopReload() {
	try{
		//alert((window != top) + ":" + (parent != top));
		var varOpener = top.opener;
		if(varOpener) {
			varOpener.location.reload();
			top.location.reload();
		} else if(window != top) {
			try{
				parent.fn_dialog.closeReload();
			}catch(e){}
			location.reload();
		} else {
			try{
				top.fn_dialog.closeReload();
			}catch(e){}
			top.location.reload();
		}
	}catch(e){
		try{
			top.fn_dialog.closeReload();
		}catch(e){}
		top.location.reload();
	}
}

/**
 * 처리 후 location.reload();
 * opener가 있는 경우 모두 reload
 */
function fn_procOpenerLevelReload(theLevel) {
	try{
		//alert((window != top) + ":" + (parent != top));
		if(opener) {
			var varOpener = opener;
			var varOpenerLevel = 1;
			while(varOpener) {
				varOpener.location.reload();
				varOpener = varOpener.opener;
				if(theLevel && varOpenerLevel >= theLevel) break;
				varOpenerLevel ++;
			}
			location.reload();
		} else if(window != top) {
			try{
				parent.fn_dialog.closeReload();
			}catch(e){}
			location.reload();
		} else {
			try{
				top.fn_dialog.closeReload();
			}catch(e){}
			top.location.reload();
		}
	}catch(e){
		try{
			top.fn_dialog.closeReload();
		}catch(e){}
		top.location.reload();
	}
}

/**
 * 처리 후 페이지 이동
 * @param theAction
 * @param theMdl
 */
function fn_procAction(theAction, theMdl) {
	try{
		if(theMdl != 1 && opener) {
			if(typeof(theAction) == 'undefined') opener.location.reload();
			else opener.location.href = theAction;
		} else if(window != top && parent) {
			if(typeof(theAction) == 'undefined') parent.location.reload();
			else parent.location.href = theAction;
		} else {
			if(typeof(theAction) == 'undefined') top.location.reload();
			else top.location.href = theAction;
		}
	}catch(e){
		if(typeof(theAction) == 'undefined') top.location.reload();
		else top.location.href = theAction;
	}
}

/**
 * 처리 후 페이지 이동 && opener reload
 * @param theAction
 * @param theMdl
 */
function fn_procActionOpReload(theAction, theMdl) {
	try{
		if(theMdl != 1 && opener) {
			if(typeof(theAction) == 'undefined') opener.location.reload();
			else opener.location.href = theAction;
			self.close();
		} else if(window != top) {
			if(opener) opener.location.reload();
			if(typeof(theAction) == 'undefined') parent.location.reload();
			else parent.location.href = theAction;
		} else {
			if(opener) opener.location.reload();
			if(typeof(theAction) == 'undefined') top.location.reload();
			else top.location.href = theAction;
		}
	}catch(e){
		if(opener) opener.location.reload();
		if(typeof(theAction) == 'undefined') top.location.reload();
		else top.location.href = theAction;
	}
}

/**
 * 계속 등록
 * @param theAction
 * @param theMdl
 * @param theMessage
 * @returns {Boolean}
 */
function fn_procContinueAction(theAction, theMdl, theMessage) {
	try{
		var varMessage = (typeof(theMessage) != 'undefined')?theMessage + "\n":"";
		varMessage += fn_Message.continueConfirmText;
		var varConfirm = confirm(varMessage);
		if(varConfirm) {
			// 계속등록하는 경우
			if(theMdl != 1 && opener) {
				if(typeof(theAction) == 'undefined') opener.location.reload();
				else opener.location.href = theAction;
				self.location.reload();
			} else if(window != top) {
				parent.location.reload();
			} else {
				top.location.reload();
			}
			return false;
		}
		if(theMdl != 1 && opener) {
			if(typeof(theAction) == 'undefined') opener.location.reload();
			else opener.location.href = theAction;
			self.close();
		} else if(window != top) {
			if(typeof(theAction) == 'undefined') parent.location.reload();
			else parent.location.href = theAction;
		} else {
			if(typeof(theAction) == 'undefined') top.location.reload();
			else top.location.href = theAction;
		}
	}catch(e){}
}
 
/*************************************
 숨김 iframe생성
 * @param theFId			: iframe id/name
*************************************/
function fn_createHiddenIframe(theFId){
	if(typeof(theFId) == 'undefined' || !fn_isValFill(theFId)) return false;
	if(!$('#' + theFId).is('iframe')) {
		$('body').append("<iframe id=\"" + theFId + "\" name=\"" + theFId + "\" title=\"내용없음\" style=\"width:0px;height:0px;\" frameborder=\"0\"></iframe>");
	}
	return true;
}



function fn_getLPAD(theStr, theFillChar, theTotalLen) {

	if(typeof(theStr) == 'undefined' || typeof(theFillChar) == 'undefined' || typeof(theTotalLen) == 'undefined') return theStr;
	
	var varStrLen = new String(theStr).length;
	var varFillLen = theTotalLen - varStrLen;
	var varRtStr = '';
	for(var i = 0 ; i < varFillLen ; i ++){
		varRtStr += theFillChar;
	}
	varRtStr += theStr;
	return varRtStr;
}

/*****************************************************
  값이 있는지의 여부 체크
 * @param theVal			: 체크 대상 값
******************************************************/
function fn_isValFill(theVal) {
	try{
		if(typeof(theVal) == 'undefined') return false;
		var varValue = theVal.trim();
		if(varValue == '') {
			return false;
		} else {
			return true;
		}
	} catch(e) {
		return false;
	}
	return false;
}

/*****************************************************
  값이 있는지의 여부 체크
 * @param theInput			: object name
******************************************************/
function fn_isFill(theInput) {
	try{
		var varObj = $(theInput);
		if(varObj.size() == 0) return true;
		var varValue = varObj.val().trim();
		if(varValue != '') return true;
	}	catch(e)	{
		return true;
	}
	return false;
}

/*****************************************************
  값이 있는지의 여부 체크
 * @param theInput			: object name
 * @param theText			: 경고문구에 들어갈 object명
******************************************************/
function fn_checkFill(theInput, theText) {
	try{
		var varObj = $(theInput);
		if(varObj.size() == 0) return true;
		var varDisabledChk = varObj.prop("disabled"); 
		if(varDisabledChk) return true; 
		if(!fn_isFill(theInput)) {
			alert(fn_Message.fillText(theText));
			try {varObj.focus();} catch(e){}
			return false;
		}
	} catch(e) {
		//alert(e);
		return true;
	}
	return true;
}


function fn_isSelected(theInput) {
	try{
		var varObj = $(theInput);
		if(varObj.size() == 0) return true;
		var varDisabledChk = varObj.prop("disabled"); 
		if(varDisabledChk) return true; 
		try {
			var varHiddenType = varObj.attr("type").toLowerCase();
			if(varHiddenType == "hidden") return true;
		} catch(e) {
		}

		var varSelObj = varObj.find("option:selected");
		if(!varSelObj.is("option") || !fn_isFill(varSelObj)) {
			return false;
		}
	} catch(e) {
		return true;
	}
	return true;
	
}

/*****************************************************
  select option 선택값이 있는지의 여부 체크
 * @param theInput			: object name
 * @param theText			: 경고문구에 들어갈 object명
******************************************************/
function fn_checkSelected(theInput, theText) {
	try{
		var varObj = $(theInput);
		if(varObj.size() == 0) return true;
		var varDisabledChk = varObj.prop("disabled"); 
		if(varDisabledChk) return true; 
		try {
			var varHiddenType = varObj.attr("type").toLowerCase();
			if(varHiddenType == "hidden") return true;
		} catch(e) {
		}

		var varSelObj = varObj.find("option:selected");
		if(!varSelObj.is("option") || !fn_isFill(varSelObj)) {
			alert(fn_Message.checkText(theText));
			try {theInput.focus();} catch(e){}
			return false;
		}
	} catch(e) {
		alert(e);
		return true;
	}
	return true;
	
}

/***********************************************************************
* multi select option값 여부 체크
* @param theObj 		: object
* @param theNum			: 비교할 수
* @param theText		: 경고문구에 들어갈 object명
* @return boolean
***********************************************************************/
function fn_checkSelectOption(theInput, theText, theGtIdx){
	try{
		var varObj = $(theInput);
		if(varObj.size() == 0) return true;
		var varDisabledChk = varObj.prop("disabled"); 
		if(varDisabledChk) return true; 
		try {
			var varHiddenType = varObj.attr("type").toLowerCase();
			if(varHiddenType == "hidden") return true;
		} catch(e) {
		}
		var varOpt = (theGtIdx > -1)? $(varObj).find("option:gt(" + theGtIdx + ")"):$(varObj).find('option');
		
		var varOptSize = varOpt.size();
		if(typeof(theMinSize) != 'undefined') {
			if(varOptSize < theMinSize) {
				alert(fn_Message.checkText(theText));
				try {varObj.focus();} catch(e){}
				return false;
			}
		} else {
			if(varOptSize == 0) {
				alert(fn_Message.checkText(theText));
				try {varObj.focus();} catch(e){}
				return false;
			}
		}
		return true;
	}catch(e){
		//alert(e);
		return false;
	}
	return false;
}

/***********************************************************************
* multi file select option값 여부 체크
* @param theObj 		: object
* @param theNum			: 비교할 수
* @param theText		: 경고문구에 들어갈 object명
* @return boolean
***********************************************************************/
function fn_checkFileSelectOption(theInputLayer, theText, theGtIdx, theMinSize, theMaxSize){
	try{
		var varObjLayer = $(theInputLayer);
		if(varObjLayer.size() == 0) return true;
		
		try {
			var varHiddenType = varObjLayer.hasClass("hide");
			if(varHiddenType) return true;
		} catch(e) {}
		var varOpt = (theGtIdx > -1)? varObjLayer.find("input[type='checkbox']:gt(" + theGtIdx + ")"):varObjLayer.find("input[type='checkbox']");
		
		try{
		var varOptSize = varOpt.size();
		if(typeof(theMinSize) == 'undefined' && typeof(theMaxSize) == 'undefined') {
			if(varOptSize == 0) {
				alert(fn_Message.checkText(theText));
				try {varObjLayer.find("input[type='checkbox']:gt(0)").focus();} catch(e){}
				return false;
			}
		} else {
			if(varOptSize == 0 && theMinSize == 0 && theMaxSize > 0) {
				alert(fn_Message.checkFileMaxLimitCountText(theText, theMaxSize));
				try {varObjLayer.find("input[type='checkbox']:gt(0)").focus();} catch(e){}
				return false;
			} else if(varOptSize < theMinSize || varOptSize > theMaxSize) {
				alert(fn_Message.checkFileMinMaxLimitCountText(theText, theMinSize, theMaxSize));
				try {varObjLayer.find("input[type='checkbox']:gt(0)").focus();} catch(e){}
				return false;
			}
		}
		} catch(e){alert(e);}
		return true;
	}catch(e){
		//alert(e);
		return false;
	}
	return false;
}

/*****************************************************
 * checkbox, radio checked 여부
 * @param theName			: check할 object name
 * @param theText			: 경고문구에 들어갈 object명
******************************************************/
function fn_checkElementChecked(theName, theText){
	try{
		var varObjs = $("input[name='" + theName + "']");
		var varDisabledChk = (varObjs.length == varObjs.filter(":disabled").length); 
		if(varDisabledChk) return true; 
		try {
			var varHiddenType = varObjs.attr("type").toLowerCase();
			if(varHiddenType == "hidden") return true;
		} catch(e) {
		}
		var varChkObjs = varObjs.filter(":checked");
		var varChkObjSize = varChkObjs.size();
		if(varChkObjSize == 0) {
			alert(fn_Message.checkText(theText));
			varObjs.filter(":eq(0)").focus();
			return false;
		}
		return true;
 	} catch(e) {}
 	return false;
}

/******************************************
//이메일 형식 체크
******************************************/
function fn_validEmail(theObj) {
	if (typeof theObj == "undefined" || $(theObj).size() == 0) {
		return true;
	}
	var varDisabledChk = $(theObj).prop("disabled"); 
	if(varDisabledChk) return true; 

	if(!fn_isFill(theObj)) {
		return true;
	}

	var varEmail = $(theObj).val();
    var arrMatch = varEmail.match(/^(\".*\"|[A-Za-z0-9_-]([A-Za-z0-9_-]|[\+\.])*)@(\[\d{1,3}(\.\d{1,3}){3}]|[A-Za-z0-9][A-Za-z0-9_-]*(\.[A-Za-z0-9][A-Za-z0-9_-]*)+)$/); //"정규식
    if (arrMatch == null) {
		alert(fn_Message.validEmailText);
		try {theObj.focus();}catch(e){}
        return false;
    }

	return true;
}

/******************************************
아이디 체크 (이메일 형식)
******************************************/
function fn_validEmailID(theObj, theText) {
	if (typeof theObj == "undefined" || $(theObj).size() == 0) {
		return true;
	}
	var varDisabledChk = $(theObj).prop("disabled"); 
	if(varDisabledChk) return true; 

	if(!fn_isFill(theObj)) {
		return true;
	}

	var varEmail = $(theObj).val();
    var arrMatch = varEmail.match(/^(\".*\"|[A-Za-z0-9_-]([A-Za-z0-9_-]|[\+\.])*)@(\[\d{1,3}(\.\d{1,3}){3}]|[A-Za-z0-9][A-Za-z0-9_-]*(\.[A-Za-z0-9][A-Za-z0-9_-]*)+)$/); //"정규식
    if (arrMatch == null) {
		alert(fn_Message.validEmailIdText(theText));
		try {theObj.focus();}catch(e){}
        return false;
    }

	return true;
}

/******************************************
아이디 체크
******************************************/
function fn_validID(theObj, theTitle, theMinLen, theMaxLen){
	if (typeof theObj == "undefined" || $(theObj).size() == 0) {
		return true;
	}
	var varObj = $(theObj);
	var varDisabledChk = varObj.prop("disabled"); 
	if(varDisabledChk) return true; 
	//if(!fn_checkFill(varObj, theTitle)) return false;
	if(!fn_isFill(varObj)) return true;
	
	var varStr = varObj.val();
	var retVal = fn_checkSpace(varStr);
	if(retVal) {
		alert(fn_Message.validNoSpaceText(theTitle));
		return false; 
	} 
	
	if(varStr.charAt(0) == '_') {
		alert(fn_Message.validNoFirstCharText(theTitle));
	 	return false;
	}

	if(typeof (theMinLen) == "undefined") theMinLen = 6;
	if(typeof (theMaxLen) == "undefined") theMaxLen = 12;
	eval("var isID = /^[a-z0-9_]{" + theMinLen + "," + theMaxLen + "}$/;");
	if(!isID.test(varStr))	{
		alert(fn_Message.validNoIDCharText(theTitle, theMinLen, theMaxLen)); 
		return false;
	}
	return true;
}

/******************************************
비밀번호 체크
******************************************/
function fn_validPW(theObj, theTitle, theMinLen, theMaxLen) {
	if (typeof theObj == "undefined" || $(theObj).size() == 0) {
		return true;
	}
	var varObj = $(theObj);
	var varDisabledChk = varObj.prop("disabled"); 
	if(varDisabledChk) return true; 
	//if(!fn_checkFill(varObj, theTitle)) return false;
	if(!fn_isFill(varObj)) return true;
	
	var varStr = $(theObj).val();
	var retVal = fn_checkSpace(varStr);
	if(retVal){
		alert(fn_Message.validNoSpaceText(theTitle));
		return false; 
	} 

	var varSChar = /[~!@\#$%^&*\()\-=+_'\"]/gi;
	var varFChar = varStr.charAt(0); 
	if(varSChar.test(varFChar)){
		alert(fn_Message.validNoFirstCharText(theTitle));
	 	return false;
	}
	
	if(typeof (theMinLen) == "undefined") theMinLen = 8;
	if(typeof (theMaxLen) == "undefined") theMaxLen = 15;
	eval("var isID = /^[a-zA-Z0-9~!@\#$%^&*\()\-=+_'\"]{" + theMinLen + "," + theMaxLen + "}$/;");
	if(!isID.test(varStr)){
		alert(fn_Message.validNoPWCharText(theTitle, theMinLen, theMaxLen)); 
		return false;
	}
	var chk_num = varStr.search(/[0-9]/g);
	var chk_eng = varStr.search(/[a-zA-Z]/ig);
	var chk_sp = varStr.search(varSChar);
	 
	if(chk_num <0 || chk_eng <0 || chk_sp < 0){
		alert(fn_Message.validNoPWCharText2(theTitle)); 
		return false;
	}
	
	if(/(\w)\1\1\1/.test(varStr)){
		alert(fn_Message.validNoPWCharText3(theTitle)); 
		return false;
	}

	return true;
}

/******************************************
비밀번호 아이디 포함여부 체크 : 비밀번호는 아이디를 포함하여 사용하실 수 없습니다.
******************************************/
function fn_validPWID(thePwdObj, thePwdTitle, theIdObj, theIdTitle) {
	return fn_validPWIDVal($(thePwdObj).val(), thePwdTitle, $(theIdObj).val(), theIdTitle);
}

/******************************************
비밀번호 아이디 포함여부 체크 : 비밀번호는 아이디를 포함하여 사용하실 수 없습니다.
******************************************/
function fn_validPWIDVal(thePwdStr, thePwdTitle, theIdStr, theIdTitle) {
	if(!thePwdStr) return true;
	if(!theIdStr) return false;
	
	if(thePwdStr.search(theIdStr) > -1 || theIdStr.search(thePwdStr) > -1){
		alert(fn_Message.validNoPWCharText4(thePwdTitle, theIdTitle)); 
		return false;
	}
	return true;
}

//이메일 형식 체크
function fn_checkEmail(theInput) {

	if (typeof theInput == "undefined") {
		return true;
	}
	var varDisabledChk = $(theInput).prop("disabled"); 
	if(varDisabledChk) return true; 

	if(!fn_isFill(theInput)) {
		return true;
	}

	var varEmail = $(theInput).val();
    var arrMatch = varEmail.match(/^(\".*\"|[A-Za-z0-9_-]([A-Za-z0-9_-]|[\+\.])*)@(\[\d{1,3}(\.\d{1,3}){3}]|[A-Za-z0-9][A-Za-z0-9_-]*(\.[A-Za-z0-9][A-Za-z0-9_-]*)+)$/); //"정규식
    if (arrMatch == null) {
		alert(fn_Message.validEmailText);
		try {theInput.focus();}catch(e){}
        return false;
    }

	return true;
}

function fn_isHomePhone(theFlag, theInput) {

	try {
		if(theInput){

			var varDisabledChk = $(theInput).prop("disabled"); 
			if(varDisabledChk) return true; 
			
			var varVal = $(theInput).val();
			if(!fn_isValFill(varVal)) return true;
			
			if(theFlag == 1 && !varVal.match(/^((02)|(031)|(032)|(033)|(041)|(042)|(043)|(051)|(052)|(053)|(054)|(055)|(061)|(062)|(063)|(064)|(070)|(0502)|(0504)|(0505)|(0506))$/)) return false;
			else if(theFlag == 2 && !varVal.match(/^\d{3,4}$/)) return false;
			else if(theFlag == 3 && !varVal.match(/^\d{4}$/)) return false;

		}
	} catch(e) {}
	return true;
}

function fn_isMobilePhone(theFlag, theInput) {

	try {
		if(theInput){

			var varDisabledChk = $(theInput).prop("disabled"); 
			if(varDisabledChk) return true; 
			
			var varVal = $(theInput).val();
			if(!fn_isValFill(varVal)) return true;
			
			if(theFlag == 1 && !varVal.match(/^((010)|(011)|(016)|(017)|(018)|(019))$/)) return false;
			else if(theFlag == 2 && !varVal.match(/^\d{3,4}$/)) return false;
			else if(theFlag == 3 && !varVal.match(/^\d{4}$/)) return false;
		}
	} catch(e) {}
	return true;
}

/*****************************************************************************
전화번호 형식 체크
*****************************************************************************/
function fn_checkHomePhone(theInput1, theInput2, theInput3, theText)
{
	try {
		var varDisabledChk1 = $(theInput1).prop("disabled"); 
		var varDisabledChk2 = $(theInput2).prop("disabled"); 
		var varDisabledChk3 = $(theInput3).prop("disabled"); 
		if(varDisabledChk1 && varDisabledChk2 && varDisabledChk3) return true; 
		
		var varFillCnt = 0;
		if(!fn_isHomePhone(1, theInput1)) {
			alert(fn_Message.validText(theText));
			$(theInput1).focus();
			return false;
		} else if(fn_isFill(theInput1)) {
			varFillCnt ++;
		}
		if(!fn_isHomePhone(2, theInput2)) {
			alert(fn_Message.validText(theText));
			$(theInput2).focus();
			return false;
		} else if(fn_isFill(theInput2)) {
			varFillCnt ++;
		} else if(varFillCnt > 0) {
			alert(fn_Message.fillAllText(theText));
			$(theInput2).focus();
			return false;
		}
		if(!fn_isHomePhone(3, theInput3)) {
			alert(fn_Message.validText(theText));
			$(theInput3).focus();
			return false;
		} else if(fn_isFill(theInput3)) {
			varFillCnt ++;
		} else if(varFillCnt > 0) {
			alert(fn_Message.fillAllText(theText));
			$(theInput3).focus();
			return false;
		}
		
	} catch(e) {}
	return true;
}

/*****************************************************************************
휴대번호 형식 체크
*****************************************************************************/
function fn_checkMobilePhone(theInput1, theInput2, theInput3, theText)
{
	try {
		var varDisabledChk1 = $(theInput1).prop("disabled"); 
		var varDisabledChk2 = $(theInput2).prop("disabled"); 
		var varDisabledChk3 = $(theInput3).prop("disabled"); 
		if(varDisabledChk1 && varDisabledChk2 && varDisabledChk3) return true; 
		
		var varFillCnt = 0;
		if(!fn_isMobilePhone(1, theInput1)) {
			alert(fn_Message.validText(theText));
			$(theInput1).focus();
			return false;
		} else if(fn_isFill(theInput1)) {
			varFillCnt ++;
		}
		if(!fn_isMobilePhone(2, theInput2)) {
			alert(fn_Message.validText(theText));
			$(theInput2).focus();
			return false;
		} else if(fn_isFill(theInput2)) {
			varFillCnt ++;
		} else if(varFillCnt > 0) {
			alert(fn_Message.fillAllText(theText));
			$(theInput2).focus();
			return false;
		}
		if(!fn_isMobilePhone(3, theInput3)) {
			alert(fn_Message.validText(theText));
			$(theInput3).focus();
			return false;
		} else if(fn_isFill(theInput3)) {
			varFillCnt ++;
		} else if(varFillCnt > 0) {
			alert(fn_Message.fillAllText(theText));
			$(theInput3).focus();
			return false;
		}
	} catch(e) {}
	return true;
}

/*****************************************************
  입력값유형  체크
******************************************************/
var fn_itemTypeReg = {
		  11: '(0|([1-9]+[0-9]*))*',
		  12: '(^[0]{1}(\\.\\d+)?$)|(^[1-9]+\\d*(\\.\\d+)?$)', 
		  //11: '(0|([1-9]+[0-9]*))*',
		  1: '0-9', 
		  2: 'a-z', 
		  3: 'A-Z', 
		  4: '0-9', 
		  5: '^ㄱ-ㅎㅏ-ㅣ가-힣'
};

function fn_checkItemTypeForm(theItemTypes, theInput, theText, theDsetText) {
	var varItemTypes, varItemTypeName = '', varItemTypeLen, varItemType, varRegs = '', varBool = false, varVal;
	//alert(fn_isValFill(theItemTypes));
	
	//alert(theItemTypes + ':' + fn_isValFill(theItemTypes));
	if(!fn_isValFill(theItemTypes) || theItemTypes == '0') return true;

	try{
		if(!theInput) return true;
		
		var varDisabledChk = $(theInput).prop("disabled"); 
		if(varDisabledChk) return true; 
		
		varVal = $(theInput).val();
		if(!fn_isValFill(varVal)) return true;

		if(theItemTypes == '1')	{
			varItemTypeName += fn_Message.checkItemType[1];
			if(!fn_isValFill(theDsetText)) {
				varRegs += '/';
				varRegs += fn_itemTypeReg[12];
				varRegs += '/g';
				
				varBool = fn_isNumber(theInput);
			}else{
				varRegs += '/^(';
				varRegs += fn_itemTypeReg[11];
				//varRegs += '|';
				varItemTypeName += ', ';
				varRegs += '[' + fn_getRegText(theDsetText) + ']*';
				varItemTypeName += '\'' + theDsetText.replace(/(,\s)+/g, '\', \'') + '\'';
				varRegs += ')+$/g';
				eval('varBool = ' + 'varVal.match(' + varRegs + ');');
			}
		}else{
			varItemTypes = theItemTypes.split(',');
			varItemTypeLen = varItemTypes.length;
			//varRegs = new Array();
			if(varItemTypeLen > 0) varRegs += '/^([';
			for(var i = 0 ; i < varItemTypeLen ; i ++) {
				varItemType = varItemTypes[i];
				//alert('varItemType:' + varItemType);
				if(i > 0){
					varItemTypeName += ', ';
					//varRegs += '|';
				}
				varRegs += fn_itemTypeReg[varItemType];
				//alert(varRegs[i]);
				varItemTypeName += fn_Message.checkItemType[varItemType];
			}
			if(fn_isValFill(theDsetText)){
				if(varItemTypeLen > 0){
					//varRegs += '|';
					varItemTypeName += ', ';
				}
				varRegs += fn_getRegText(theDsetText);
				varItemTypeName += '\'' + theDsetText.replace(/(,\s)+/g, '\', \'') + '\'';
			}
			//if(varItemTypeLen > 0) varRegs += ')';
			if(varItemTypeLen > 0) varRegs += ']*)$/g';
			eval('varBool = ' + 'varVal.match(' + varRegs + ');');
			//return;
		}
		
		if(!varBool){
			alert(fn_Message.itemTypeText(varItemTypeName, theText));
			try {
				theInput.focus();
			} catch(e){}
			return false;
		}
	}catch(e){
		return true;
	}
	return true;
}

function fn_onlyItemTypeForm(theItemTypes, theInput, theText, theDsetText) {
	var varResult = fn_checkItemTypeForm(theItemTypes, theInput, theText, theDsetText);
	if(!varResult){
		var varVal = $(theInput).val();
		if(varVal != ''){
			var varLen = varVal.length;
			$(theInput).val(varVal.substring(0, varLen - 1));
		}
	}
	return varResult;
}

/*****************************************************
입력가능값  체크
******************************************************/
function fn_getRegText(theDsetText) {
	var varTexts, varTextLen, varRegText = '';
	varTexts = theDsetText.split(',');
	varTextLen = varTexts.length;
	
	for(var i = 0 ; i < varTextLen ; i ++){
		varRegText += varTexts[i];
	}
	return varRegText;
}

/******************************************
입력값 체크
******************************************/
function fn_checkTextForm(theUseText, theDsetText, theInput, theText) {
	if(typeof(theInput) == 'undefined' || !fn_isValFill(theDsetText)) return true;
	var varDisabledChk = theInput.prop("disabled"); 
	if(varDisabledChk) return true; 
	var varVal, varTexts, varTextLen, varRegText = '', varBool = false;
	varVal = $(theInput).val();
	varTexts = theDsetText.split(',');
	varTextLen = varTexts.length;
	if(theUseText == 2)	{
		// 불가능

		for(var i = 0 ; i < varTextLen ; i ++) {
			if(varVal.indexOf(varTexts[i]) == -1) varBool = true;
			else {
				varBool = false;
				break;
			}
		}
	} else {
		// 가능
		varRegText = 'varBool = (varVal.match(/[';
		for(var i = 0 ; i < varTextLen ; i ++) {
			if(i > 0) varRegText += '|';
			varRegText += varTexts[i];
		}
		varRegText += ']/g));';
		eval(varRegText);
	}
	if(!varBool) {
		alert(fn_Message.impossibleText(theDsetText, theText, true));
		try {
		theInput.focus();
		} catch(e){}
		return false;
	}
	return true;
}

/******************************************
입력값 체크 : 금지단어
******************************************/
function fn_checkPrvTextForm(theDsetText, theInput, theText) {
	if(typeof(theInput) == 'undefined' || !fn_isValFill(theDsetText)) return true;
	var varDisabledChk = theInput.prop("disabled"); 
	if(varDisabledChk) return true; 
	var varVal, varTexts, varTextLen, varRegText = '', varMatch = false;
	varVal = $(theInput).val();
	varTexts = theDsetText.split(',');
	varTextLen = varTexts.length;
	// 불가능

	var varTextType = 0;
	var varCommentText;
	for(var i = 0 ; i < varTextLen ; i ++) {
		varRegText = 'varMatch = varVal.match(/^' + varTexts[i] + '$/g);';
		eval(varRegText);
		//alert('>' + varVal + ':' + varRegText + ':' + varMatch);
		if(varMatch) {
			varCommentText = varTexts[i];
			var varRegIdx = varCommentText.indexOf('[');
			if(varRegIdx == 0) {
				// 끝나는 문자
				varRegIdx = varCommentText.indexOf(']+');
				if(varRegIdx != -1)	varCommentText = varCommentText.substring(varRegIdx + 2);
				//varComment = "'" + varCommentText + "'로 끝나는";
				varTextType = 2;
			} else if(varRegIdx > 0) {
				// 시작하는 문자
				varCommentText = varCommentText.substring(0, varRegIdx);
				//varComment = "'" + varCommentText + "'로 시작하는";
				varTextType = 1;
			}// else varComment = varCommentText;
			break;
		}
	}
	if(varMatch) {
		var varMessage;
		if(varTextType == 1) varMessage = fn_Message.impossibleBeginText(varCommentText, theText, true);
		else if(varTextType == 2) varMessage = fn_Message.impossibleEndText(varCommentText, theText, true);
		else varMessage = fn_Message.impossibleText(varCommentText, theText, true);
		alert(varMessage);
		try {
			theInput.focus();
		} catch(e){alert(e);}
		return false;
	}
	
	return true;
}

/**
 * 글자수 제한
 * @param theObj 
 * @param theMaxLen : character기준
 * @param theText
 * @return
 */
function fn_checkStrMaxLenForm(theObj, theText, theMaxLen){
	var varDisabledChk = $(theObj).prop("disabled"); 
	if(varDisabledChk) return true; 
	if(!fn_isFill(theObj)) return true;
	var valStr = $(theObj).val();
	var varDataType = $(theObj).attr("data-type");
	if(varDataType == "current") valStr = valStr.replace(/,/g, "");
	var valLen = valStr.length;
	if(valLen > theMaxLen) {
		alert(fn_Message.limitTLenText(theText, theMaxLen));
		$(theObj).val(valStr.substring(0, theMaxLen));
		return false;
	}
	return true;
}

/**
 * 글자수 제한
 * @param theObj
 * @param theText
 * @param theMinLen
 * @param theMaxLen
 * @returns {Boolean}
 */
function fn_checkStrLenForm(theObj, theText, theMinLen, theMaxLen){
	var varDisabledChk = $(theObj).prop("disabled"); 
	if(varDisabledChk) return true; 
	if(!fn_isFill(theObj)) return true;
	var valStr = $(theObj).val();
	var varDataType = $(theObj).attr("data-type");
	if(varDataType == "current") valStr = valStr.replace(/,/g, "");
	var valLen = valStr.length;

	if(valLen < theMinLen) {
		alert(fn_Message.limitBTLenText(theText, theMinLen, theMaxLen));
		return false;
	}
	if(valLen > theMaxLen) {
		alert(fn_Message.limitTLenText(theText, theMaxLen));
		$(theObj).val(valStr.substring(0, theMaxLen));
		return false;
	}
	return true;
}

/*****************************************************************************
이메일 setting
*****************************************************************************/
function fn_initEmailValue(theObj, theTargetId, theTargetVal){
	var varVal = $(theObj).find("option:selected").val();
	if(varVal != '0'){
		$('#' + theTargetId).val(varVal);
		$('#' + theTargetId).prop('readOnly', true);
	}else{
		$('#' + theTargetId).prop('readOnly', false);
	}
}

function fn_setEmailValue(theObj, theTargetId, theTargetVal){
	var varVal = $(theObj).find("option:selected").val();
	if(varVal != '0'){
		$('#' + theTargetId).val(varVal);
		$('#' + theTargetId).prop('readOnly', true);
	}else{
		var varTargetVal = (typeof(theTargetVal) == 'undefined')?"":theTargetVal;
		$('#' + theTargetId).val(varTargetVal);
		$('#' + theTargetId).prop('readOnly', false);
		$('#' + theTargetId).focus();
	}
}


/***********************************
 select option 순서 위아래 수정
 * @param theObj 			: source select
 * @param theFlag			: 위/아래(1/2)
 * @param theSIdx			: option start index
 * @return
***********************************/
function fn_moveUDOptions(theObj, theFlag, theSIdx) {
	var varSelectedObj = $(theObj).find('option:selected');
	var varSelectedCnt = $(varSelectedObj).size();
	
	if(varSelectedCnt <= 0)	{
		alert('이동할 ' + $(theObj).attr('title') + '를 선택하세요.');
		return;
	}
	
	var varIsChecked = false;
	var varSIdx;
	var varTIdx;
	var varTObj;
	if(theFlag == '1') {
		// 위
		$(varSelectedObj).each(function(index, item){
			if(varIsChecked) return;
			var varOptions = $(theObj).find('option');
			varSIdx = $(varOptions).index(item);
			if(varSIdx <= theSIdx){
				alert('위로 이동하실 수 없습니다.');
				varIsChecked = true;
				return;
			}
			varTIdx = varSIdx - 1;
			varTObj = $(varOptions).eq(varTIdx);
			$(varTObj).before(item);
		});
	} else {
		// 아래
		varSelectedObj = $(varSelectedObj).get().reverse();
		var varOptionMaxIdx = $(theObj).find('option').size() - 1;
		$(varSelectedObj).each(function(index, item){
			if(varIsChecked) return;
			var varOptions = $(theObj).find('option');
			//alert($(theObj).find('option').index(item));
			varSIdx = $(varOptions).index(item);
			if(varSIdx < theSIdx || varSIdx >= varOptionMaxIdx){
				alert('아래로 이동하실 수 없습니다.');
				varIsChecked = true;
				return;
			}
			varTIdx = varSIdx + 1;
			varTObj = $(varOptions).eq(varTIdx);
			$(varTObj).after(item);
		});
	}
}


/***********************************
 ul, checkbox 순서 위아래 수정
 * @param theObj 			: source ul
 * @param theFlag			: 위/아래(1/2)
 * @param theSIdx			: option start index
 * @return
***********************************/
function fn_moveUDLiChecks(theTxt, theObj, theFlag, theSIdx) {
	var varSelectedObj = $(theObj).find('input[type="checkbox"]:checked').parent("li");
	var varSelectedCnt = $(varSelectedObj).size();
	
	if(varSelectedCnt <= 0)	{
		alert('이동할 ' + theTxt + '를 선택하세요.');
		return;
	}
	
	var varIsChecked = false;
	var varSIdx;
	var varTIdx;
	var varTObj;
	if(theFlag == '1') {
		// 위
		$(varSelectedObj).each(function(index, item){
			if(varIsChecked) return;
			var varOptions = $(theObj).find('>li');
			varSIdx = $(varOptions).index(item);
			if(varSIdx <= theSIdx){
				alert('위로 이동하실 수 없습니다.');
				varIsChecked = true;
				return;
			}
			varTIdx = varSIdx - 1;
			varTObj = $(varOptions).eq(varTIdx);
			$(varTObj).before(item);
		});
	} else {
		// 아래
		varSelectedObj = $(varSelectedObj).get().reverse();
		var varOptionMaxIdx = $(theObj).find('>li').size() - 1;
		$(varSelectedObj).each(function(index, item){
			if(varIsChecked) return;
			var varOptions = $(theObj).find('>li');
			//alert($(theObj).find('option').index(item));
			varSIdx = $(varOptions).index(item);
			if(varSIdx < theSIdx || varSIdx >= varOptionMaxIdx){
				alert('아래로 이동하실 수 없습니다.');
				varIsChecked = true;
				return;
			}
			varTIdx = varSIdx + 1;
			varTObj = $(varOptions).eq(varTIdx);
			$(varTObj).after(item);
		});
	}
}

// 통화 format
function fn_getValFormatCurrent(theVal) {
	var varVal = (typeof(theVal) == "number")? theVal.toString():theVal;
	if(!fn_isValFill(varVal)) return;
	varVal = varVal.replace(/,/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	return varVal;
}

function fn_setFormatCurrent(theObj) {
	if(!fn_isFill(theObj)) return;
	var varVal = $(theObj).val();
	varVal = varVal.toString().replace(/,/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	var varCM = varVal.match(/,/g);
	var varCMLen = (varCM)?varCM.length:0;
	var varAttrML = $(theObj).attr("maxlength");
	$(theObj).attr("maxlength", new Number(varAttrML) + varCMLen);
	$(theObj).val(varVal);
}

function fn_setNFormatCurrent(theObj) {
	if(!fn_isFill(theObj)) return;
	var varVal = $(theObj).val();
	var varCM = varVal.match(/,/g);
	var varCMLen = (varCM)?varCM.length:0;
	
	varVal = varVal.replace(/,/g, "");
	var varAttrML = $(theObj).attr("maxlength");
	$(theObj).attr("maxlength", new Number(varAttrML) - varCMLen);
	$(theObj).val(varVal);
}

// 통화 체크
function fn_checkCurrent(theInput, theText){
	try{
		var varObj = $(theInput);
		if(varObj.size() == 0) return true;
		var varDisabledChk = varObj.prop("disabled"); 
		if(varDisabledChk) return true; 
		if(!fn_isFill(theInput)) return true;

		varVal = varObj.val().replace(/,/g, "");
		if(!fn_isNumberVal(varVal, 0)) {
			alert(fn_Message.numberText(theText));
			try {varObj.focus();} catch(e){}
			return false;
		}
	} catch(e) {
		//alert(e);
		return true;
	}
	return true;
}

/*****************************************************
숫자 체크
******************************************************/
function fn_isNumberVal(theVal, theFirstNumber) {
	var varValue, varLen;
	try{
		varValue = theVal.replace(/^\s*|\s*$/g, ''); // 좌우 공백 제거
		if (varValue == '' || isNaN(varValue)) return false;
		
		if(theFirstNumber != undefined)	{
			varLen = varValue.length;
			if(varLen > 1 && varValue.charAt(0) == theFirstNumber) return false;
		}
		return true;
	}catch(e){
		return false;
	}
	return false;
}

/*****************************************************
숫자 체크 /숫자문자체크
******************************************************/
function fn_isNumber(theInput, theFirstNumber) {
	var varValue, varLen;
	try{
		if(theInput) {
			varValue = $(theInput).val().replace(/^\s*|\s*$/g, ''); // 좌우 공백 제거
			//alert('2:' + isNaN('terr'));
			if (varValue == '' || isNaN(varValue)) return false;
			
			if(theFirstNumber != undefined)	{
				varLen = varValue.length;
				if(varLen > 1 && varValue.charAt(0) == theFirstNumber) return false;
			}
			return true;
		}
	}catch(e){
		return false;
	}
	return false;
}

/**
 * 3차 select option 초기 setting
 * @param theId
 */
function fn_setInitItemOrderOption(theId){
	try{
		var varMaxLevel = new Number($("#" + theId).attr("data-max"));
		for(var varIdx = 1 ; varIdx <= varMaxLevel ; varIdx ++) {
			(function(varIdx){
				$("#" + theId + varIdx).change(function(){
					// 저장값 setting
					var varClassIdx;
					for(var varSIdx = varIdx ; varSIdx >= 1 ; varSIdx --) {
						varClassIdx = $("#" + theId + varSIdx).find("option:selected").val();
						if(varClassIdx != '') break;
					}
					$("#" + theId + " option[value='" + varClassIdx + "']").prop("selected", true);
					
					// 다음 level setting
					if(varIdx < varMaxLevel) {
						for(var varSIdx = varIdx + 1 ; varSIdx <= varMaxLevel ; varSIdx ++) {
							$("#" + theId + varSIdx + " option:gt(0)").remove();
						}
						
						var varPClassIdx = $(this).find("option:selected").val();
						var varClassOpts = $("#" + theId + " option[data-pcode='" + varPClassIdx + "']");
						$("#" + theId + (varIdx + 1)).append(varClassOpts.clone());
					}
				});
			})(varIdx);
		}
	}catch(e){}
}

/*****************************************************
3차 select option 채우기
******************************************************/
function fn_setItemOrderOption(theId){
	try {
		var varSelOptObj = $("#" + theId).find("option:selected");
		var varSelClassIdx = varSelOptObj.val();
		var varClassIdx = varSelClassIdx;
		if(varClassIdx == '') {
			var varClassOpts = $("#" + theId + " option[data-pcode='0']");
			$("#" + theId + "1").append(varClassOpts.clone());
		} else {
			var varDataLevel = new Number(varSelOptObj.attr("data-level"));
			var varPCode;
			for(var varIdx = varDataLevel ; varIdx >= 1 ; varIdx --) {
				varSelOptObj = $("#" + theId + " option[value='" + varClassIdx + "']");
				varPCode = varSelOptObj.attr("data-pcode");
				var varClassOpts = $("#" + theId + " option[data-pcode='" + varPCode + "']");
				$("#" + theId + varIdx).append(varClassOpts.clone());
				$("#" + theId + varIdx).find("option[value='" + varClassIdx + "']").prop("selected", true);
				varClassIdx = varPCode;
			}

			var varMaxLevel = new Number($("#" + theId).attr("data-max"));
			if(varDataLevel < varMaxLevel) {
				varClassIdx = varSelClassIdx;
				var varIdx = varDataLevel + 1
				var varObj = $("#" + theId + varIdx);
				var varPClassIdx = varClassIdx;
				var varClassOpts = $("#" + theId + " option[data-pcode='" + varPClassIdx + "']");
				$("#" + theId + varIdx).append(varClassOpts.clone());
			}
		}
	} catch(e){}
}


/*****************************************************
tree형태 select 전체 선택
******************************************************/
function fn_setAllTreeSelectObjs(theObj) {
	var varChked = $(theObj).prop("checked");
	
	var varObjName = $(theObj).attr("name");
	var varCObjs = $("input[name='" + varObjName + "']").filter("[data-l!='1']");
	if(varChked) varCObjs.prop("checked", varChked);
	varCObjs.prop("disabled", varChked);
}

/*****************************************************
tree형태 select 개별 선택
******************************************************/
function fn_setTreeSelectObjs(theObj) {
	var varChked = $(theObj).prop("checked");
	var varObjName = $(theObj).attr("name");
	var varCObjs = $("input[name='" + varObjName + "']").filter("[data-p='" + $(theObj).val() + "']");
	if(varCObjs.size() > 0) {
		// child가 있는 경우 
		
		var varPCd = $(theObj).attr("data-p");
		//var varLevel = $(this).attr("data-l");
		
		var varSLObjs = $("input[name='" + varObjName + "']").filter("[data-p='" + varPCd + "']");
		var varIdx = varSLObjs.index($(theObj));
		var varNextObj = varSLObjs.filter(":eq(" + (varIdx + 1) + ")");

		var varTObjs = $("input[name='" + varObjName + "']");
		var varSIdx = varTObjs.index($(theObj));
		var varSObjs = varTObjs.filter(":gt(" + varSIdx + ")");
		if(varNextObj.is('input')) {
			// 같은 level이 있는 경우
			var varEIdx = varTObjs.index(varNextObj);
			var varCnt = varEIdx - varSIdx;
			if(varCnt > 1) {
				var varChkObjs = varSObjs.filter(":lt(" + (varCnt - 1) + ")");
				/*if(varChked) */varChkObjs.prop("checked", varChked);
				varChkObjs.prop("disabled", varChked);
			}
		} else {
			// 같은 level이 없는 경우 
			// 1. 다음 상위있는 경우
			var varLevel = new Number($(theObj).attr("data-l"));
			var varEIdx = -1;
			$.each(varSObjs, function(i){
				var varSLevel = new Number($(this).attr("data-l"));
				if(varLevel > varSLevel) {
					varEIdx = i;
					return false;
				}
			});
			
			if(varEIdx > -1) {
				var varChkObjs = varSObjs.filter(":lt(" + varEIdx + ")");
				/*if(varChked) */varChkObjs.prop("checked", varChked);
				varChkObjs.prop("disabled", varChked);
			} else {
				// 2. 마지막인 경우
				/*if(varChked) */varSObjs.prop("checked", varChked);
				varSObjs.prop("disabled", varChked);
			}
		}
	}
}

/***********************************
 * select option 이동
 * @param theSObj			: source select
 * @param theTObj			: target select
 * @param theSelected		: 이전 선택option selected true/false여부
 * @return
 ***********************************/
function fn_moveLROptions(theSObj, theTObj, theSelected) {

	$(theTObj).find('option:selected').prop('selected', theSelected);
	$(theSObj).find('option:selected').each(function(index, item){
		$(theTObj).append(item);
	});
}
/***********************************
 select option 순서 위아래 수정
 * @param theObj 			: source select
 * @param theFlag			: 위/아래(1/2)
 * @param theSIdx			: option start index
 * @return
***********************************/
function fn_moveUDOptions(theObj, theTitle, theFlag, theSIdx) {
	var varSelectedObj = $(theObj).find('option:selected');
	var varSelectedCnt = $(varSelectedObj).size();
	if(varSelectedCnt <= 0){
		alert('이동할 ' + theTitle + '를 선택하세요.');
		return;
	}
	
	var varTmpOption;
	var varIsChecked = false;
	var varSIdx;
	var varTIdx;
	var varTObj;
	if(theFlag == '1'){
		// 위
		$(varSelectedObj).each(function(index, item){
			if(varIsChecked) return;
			var varOptions = $(theObj).find('option');
			varSIdx = $(varOptions).index(item);
			if(varSIdx <= theSIdx){
				alert('위로 이동하실 수 없습니다.');
				varIsChecked = true;
				return;
			}
			varTIdx = varSIdx - 1;
			varTObj = $(varOptions).eq(varTIdx);
			$(varTObj).before(item);
		});
	} else {
		// 아래
		varSelectedObj = $(varSelectedObj).get().reverse();
		var varOptionMaxIdx = $(theObj).find('option').size() - 1;
		$(varSelectedObj).each(function(index, item){
			if(varIsChecked) return;
			var varOptions = $(theObj).find('option');
			//alert($(theObj).find('option').index(item));
			varSIdx = $(varOptions).index(item);
			if(varSIdx < theSIdx || varSIdx >= varOptionMaxIdx){
				alert('아래로 이동하실 수 없습니다.');
				varIsChecked = true;
				return;
			}
			varTIdx = varSIdx + 1;
			varTObj = $(varOptions).eq(varTIdx);
			$(varTObj).after(item);
			//alert(varTIdx);
		});
	}
}

/*****************************************************
object 전체 scroll
******************************************************/
function fn_addScrollObject(theTObj, theHeight, theWidth){

	var varDHeight = theHeight.replace(/px/gi, "");
	var varTHeight = theTObj.height();

	if(varTHeight <= varDHeight) return false;
	
	theTObj.css("border", "0px");
	var varDLStr = "<div style='";
	if(typeof(theWidth) != 'undefined') varDLStr += "width:" + theWidth + "px;";
	varDLStr += "height:" + theHeight + "; overflow-y:auto;'></div>";
	var varDLObj = $(varDLStr);

	$(theTObj).before(varDLObj);
	varDLObj.append(theTObj);
	
	var varTBorderColor = theTObj.css("borderTopColor");
	if(varTBorderColor == '') varTBorderColor = "#6b6b6b";
	varDLObj.css("border-top", "2px solid " + varTBorderColor);
	varDLObj.css("border-bottom", "1px solid " + varTBorderColor);
}


/*****************************************************
테이블 head 고정
******************************************************/
function fn_fixedTableHead(theTObj, theWidth, theHeight){
	
	if(!theTObj.is("table")) return false;
	var varTHeight = theTObj.find("tbody").height();

	if(varTHeight == 0) {
		if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ) {
			setTimeout(function(){ fn_setFixedTableHead(theTObj, theWidth, theHeight); }, 100);
		}
	} else {
		fn_setFixedTableHead(theTObj, theWidth, theHeight);
	}
}

function fn_setFixedTableHead(theTObj, theWidth, theHeight){
	
	if(!theTObj.is("table")) return false;

	var varDHeight = theHeight.replace(/px/gi, "");
	var varTHeight = theTObj.find("tbody").height();
	
	if(varTHeight <= varDHeight) return false;
	
	theTObj.css("border", "0px");
	var varDivObj = $("<div style='clear:both;position:relative;'><div style='width:" + theWidth + "; height:" + theHeight + "; overflow-x:hidden; overflow-y:auto;'></div></div>");

	
	$(theTObj).before(varDivObj);
	var varTHObj = theTObj.clone();
	varTHObj.find(">tbody").remove();
	varDivObj.prepend(varTHObj);
	varDivObj.find(">div").append(theTObj);

	theTObj.find(">thead").hide();
	
	var varTBorderColor = theTObj.css("borderTopColor");
	if(varTBorderColor == '') varTBorderColor = "#6b6b6b";
	varDivObj.css("border-top", "2px solid " + varTBorderColor);
	varDivObj.find(">div").css("border-bottom", "1px solid " + varTBorderColor);
	
	var varTWidth = varDivObj.find(">div>table").width();
	if(varTWidth > 0) {
		fn_setFixedTableHeadAddWidth(theTObj, "tbody");
		varDivObj.find(">div>table").css("width", (varTWidth + 20) + "px");
		fn_setFixedTableHeadAddWidth(varDivObj.find(">table"), "thead");
	}
}

function fn_setFixedTableHeadAddWidth(theTObj, theType) {
	var varIsCol = theTObj.find(">colgroup").is("colgroup");
	if(varIsCol) {
		var varLastCol = theTObj.find(">colgroup>col:last-child");
		var varLastColWidth = varLastCol.width();
		if(varLastColWidth > 0) varLastCol.attr("width", varLastColWidth + 20 + "px");
	} else {
		theTObj.find(theType + ">tr>td:last-child").css("width", theTObj.find("tbody>tr>td:last-child").width() + 20 + "px");
	}
}

var fn_checkObj = {
	checkedId:false
	, checkedBtn:false
	, checkeds:false
	, checkForm:false
	, reset:function(){
		// 중복확인 setting
		$(fn_checkObj.checkedBtn).text(fn_Message.duplicateCheck);
		var varObj = $("*[name^=" + fn_checkObj.checkedId + "]");
		varObj.val("");
		varObj.not("[data-readonly]").prop("readonly", false);
		varObj.filter("select").not(":selected").prop("disabled", false);
		varObj.css("backgroundColor", "#fff");
		varObj.focus();
		fn_checkObj.checkForm.obj.val("");
		fn_checkObj.checkeds[fn_checkObj.checkedId] = false;
	}, success:function() {
		// 중복확인 성공
		var varConfirm = confirm(fn_Message.useConfirm($('label[for^=' + fn_checkObj.checkedId + ']').text()));
		if(!varConfirm) {
			// 미사용
			fn_checkObj.reset();
			return false;
		}

		// 사용		
		var varObj = $("*[name^=" + fn_checkObj.checkedId + "]");
		var varBtnObj = fn_checkObj.checkedBtn;
		
		fn_useCheckObj(varObj, varBtnObj, fn_checkObj.checkedId);
		
	}, fail:function(theCode){
		// 중복확인 실패
		var varMsg;
		if(theCode == 1) {
			varMsg = fn_Message.existDuplicate($('label[for^=' + fn_checkObj.checkedId + ']').text());
		} else {
			varMsg = fn_Message.errorsRSAEncrypt;
		}
		alert(varMsg);
		if(theCode == 1) fn_checkObj.reset();

		return false;
	}, confirm:function(theFormatType){
		// 아이디 중복확인
		var varObj = $("#" + fn_checkObj.checkedId);
		var varObjName = $('label[for^=' + fn_checkObj.checkedId + ']').text();
		if(theFormatType == 3)
		{
			if(fn_isFill($("#" + fn_checkObj.checkedId + "1")) || fn_isFill($("#" + fn_checkObj.checkedId + "2"))) {
				$("#" + fn_checkObj.checkedId).val($("#" + fn_checkObj.checkedId + "1").val() + '@' + $("#" + fn_checkObj.checkedId + "2").val());
			}
		}
		if(!fn_checkFill(varObj, varObjName)) return false;
		if(theFormatType == '3' && !fn_validEmail()) {
			return false;
		}
		if(theFormatType == '10' && !fn_validID(varObj, varObjName)) {
			return false;
		}

		fn_checkObj.checkForm.obj.val(varObj.val());
		fn_checkObj.checkForm.form.submit();
	}, check:function(theBtnObj, theObjId, theCheckForm, theFormatType){
		// 버튼클릭
		//alert($(fn_checkObj.checkForm["form"]).attr('id'));
		fn_checkObj.checkForm = theCheckForm;
		try {
			var varChecked = false;
			if(!fn_checkObj.checkeds) {
				fn_checkObj.checkeds = {};
				fn_checkObj.checkeds[theObjId] = false;
			} else {
				varChecked = fn_checkObj.checkeds[theObjId];
			}
			fn_checkObj.checkedId = theObjId;
			fn_checkObj.checkedBtn = theBtnObj;
			if(varChecked) {
				// 중복확인한 경우
				var varConfirm = confirm(fn_Message.reconfirm($('label[for^=' + fn_checkObj.checkedId + ']').text()));
				if(varConfirm) {
					fn_checkObj.reset();
				}
			} else {
				// 중복확인 안한 경우
				fn_checkObj.confirm(theFormatType);
			}
		} catch(e) {}
	}
};

function fn_useCheckObj(theObj, theBtnObj, theId){
	$(theBtnObj).text(fn_Message.authenticationComplete);
	theObj.prop("readonly", true);
	theObj.filter("select").not(":selected").prop("disabled", true);
	theObj.css("backgroundColor", "#e5e5e5");

	fn_checkObj.checkeds[theId] = true;
}

// 배열 매칭 값 찾기
function getMatchObject(arr, matchVal, keyName) {
	if(typeof(arr) == "undefined" || typeof(matchVal) == "undefined") return false;
	
	var varObj = false;
	var arrVal;
	$.each(arr, function(i, item){
		arrVal = item[keyName];
		//alert(arrVal + ":" + matchVal + ":" + (arrVal == matchVal));
		if(arrVal == matchVal) {
			varObj = item;
			return true;
		}
	});
	return varObj;
}

function fn_getIndexOf(theFlag, theStr1, theStr2) {
	if(theStr1 == '' || theStr2 == '') return -1;
	
	var varStr1 = theFlag + theStr1 + theFlag;
	var varStr2 = theFlag + theStr2 + theFlag;
	
	return varStr1.indexOf(varStr2);
	
}
/*
 * strTemp  : [필수] 크로스사이트 스크립팅을 검사할 문자열
 * level    : [옵션] 검사레벨
 *            0 (기본) -> XSS취약한 문자 제거
 *            1 (선택) -> 단순한 <, > 치환
 */
function replaceXSS(strTemp, level) {
  if ( level == undefined || level == 0 ) {
    strTemp = strTemp.replace(/\<|\>|\"|\'|\%|\;|\(|\)|\&|\+|\-/g,"");
  }
  else if (level != undefined && level == 1 ) {
    strTemp = strTemp.replace(/\</g, "&lt;");
    strTemp = strTemp.replace(/\>/g, "&gt;");
  }
  return strTemp;
}

/**
 * SNS공유
 */
function fn_shareSns(theType, theImgSrc) {
	    var varUrl = document.URL;
	    var varTitle = document.title;
	    var varHref, wp;

	    if (theType == 'twitter') {
	    	varHref = 'https://twitter.com/intent/tweet?text=' + encodeURIComponent(varTitle) + ' ' + encodeURIComponent(varUrl);
	        wp = window.open(varHref, theType, 'width=600, height=400');
	    } else if (theType == 'facebook') {
	    	if(theImgSrc)
	    	{
	    		varHref = 'http://facebook.com/sharer.php?s=100&u=' + encodeURIComponent(varUrl) + '&p[url]=' + encodeURIComponent(varUrl); + '&p[title]=' + encodeURIComponent(varTitle);
	    		varHref += 'p[images][0]=' + theImgSrc;
	    	}
	    	else varHref = 'http://facebook.com/sharer.php?u=' + encodeURIComponent(varUrl) + '&t=' + encodeURIComponent(varTitle);
	    	wp = window.open(varHref, theType, 'width=800, height=400');
	    } else if (theType == 'me2day') {
	    	varHref = 'http://me2day.net/posts/new?new_post[body]=' + encodeURIComponent('"' + varTitle + '" : ' + varUrl);
	        wp = window.open(varHref, theType, 'width=600, height=560');
	    } else if (theType == 'yozm') {
	    	varHref = 'http://yozm.daum.net/api/popup/prePost?link=' + encodeURIComponent(varUrl) + '&prefix=' + encodeURIComponent(varTitle);
	        wp = window.open(varHref, theType, 'width=400, height=364');
	    } else if(theType == 'band') {
    		varHref = 'https://band.us/plugin/share?route=' + encodeURIComponent(varUrl) + '&body=' + encodeURIComponent(varTitle) + ' ' + encodeURIComponent(varUrl);
    		window.open(varHref, theType, 'width=410, height=540');
	    } else if(theType == 'blog') {
    		varHref = 'http://blog.naver.com/openapi/share?url=' + encodeURIComponent(varUrl) + '&title=' + encodeURIComponent(varTitle) + ' ' + encodeURIComponent(varUrl);
    		window.open(varHref, theType, 'width=410, height=540');
	    }
	}

function fn_contactSubmitForm(theAction) {

	var varAction = theAction;
	$.ajax({
		type:'POST', 
		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');}, 
		url:varAction, 
		async:true, 
		success:function(result){
		}, 
		error:function(request,error){
		}
	});	
}

/**
 * 날짜 format
 */
Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";
 
    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var d = this;
     
    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
};

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};



if (!String.prototype.endsWith) {
  String.prototype.endsWith = function(searchString, position) {
      var subjectString = this.toString();
      if (typeof position !== 'number' || !isFinite(position) || Math.floor(position) !== position || position > subjectString.length) {
        position = subjectString.length;
      }
      position -= searchString.length;
      var lastIndex = subjectString.indexOf(searchString, position);
      return lastIndex !== -1 && lastIndex === position;
  };
}
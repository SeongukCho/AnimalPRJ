var fn_ajax = {
	checkError:function(request) {
		if(request.status == "403") {
			// 권한 없는 경우 로그인 페이지로 이동
			var varRespText = request.responseText;
			var varUrlInfo = JSON.parse(varRespText);
			fn_checkLogin(varUrlInfo.loginUrl, varUrlInfo.contUrl);
		} else {
			alert(request.responseText);
		}
	}
};

function fn_checkLogin(theLoginUrl, theMenuContentsUrl) {
	var varConfirm = confirm(fn_Message.loginConfirmText);
	
	var varWin = top;
	var varWinO = [];
	var varWinCnt = 0;
	var varIsOpener = false;
	if(varConfirm) {
		var varLoginUrl = theLoginUrl;	// 로그인 경로
		var varToUrl;						// 되돌아갈 경로
		var varTmpWin = varWin;				// 현재창
		var varTmpOpener;					// opener
		for(var winI = 0 ; winI < 3 ; winI ++) {
			try {
				varTmpOpener = varTmpWin.opener;
				if(!varTmpWin) break;
				try{
					varIsOpener = varTmpOpener.top.gVarM;
				}catch(e){}

				varWinO[varWinCnt ++] = varTmpWin;
				if(varIsOpener) {
					varWin = varTmpOpener.top;
					break;
				}
				varTmpWin = varTmpOpener;
			} catch(e) {break;}
		}
		if(!varIsOpener) {
			varWin = top;
			varWinO = [];
			varWinCnt = 0;
		}
		varToUrl = varWin.location.href;
		if(typeof(theMenuContentsUrl) != "undefined" && varToUrl.indexOf("/menuContents/") != -1/* && varToUrl.indexOf(varMenuContentsUrl) == -1*/) {
			varToUrl = theMenuContentsUrl;
		}
		
		varLoginUrl += (varLoginUrl.indexOf("?") != -1)?"&":"?";
		varLoginUrl += "url=" + encodeURIComponent(varToUrl);

		varWin.location.href = varLoginUrl;
		if(varWinCnt > 0) {
			for(var winI = varWinCnt ; winI > 0 ; winI --) {
				varWinO[winI - 1].close();
			}
		}
	} else {
		history.go(-1);
	}
}

function fn_getToUrl(theUrl, theMenuContentsUrl) {
	
	var varToUrl = theUrl;
	if(typeof(theMenuContentsUrl) != "undefined" && varToUrl.indexOf("/menuContents/") != -1) {
		varToUrl = theMenuContentsUrl;
	}
	
	return varToUrl;
}
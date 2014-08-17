//prompt(123);
~function(){
	var screenX,screenY;
	try{
	document.addEventListener("touchstart",function (event){
		event = event.touches[0];
		screenX = event.screenX;
		screenY = event.screenY;
	})
	document.addEventListener("touchend",function (event){
		event = event.changedTouches[0];
		if(screenX || screenY){
			var x = event.screenX;
			var y = event.screenY;
			x-= screenX;
			y-= screenY;
			prompt('js-invoke://','event:fling@'+encodeURIComponent('['+x+','+y+']'));
		}
	})
	}catch(e){
		prompt(e);
	}
}()
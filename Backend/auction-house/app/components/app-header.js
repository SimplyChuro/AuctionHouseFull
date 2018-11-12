import Component from '@ember/component';
import ENV from 'auction-house/config/environment';

export default Component.extend({
	actions: {
		logout: function(){
			Ember.$.ajax({
	            url: 'http://localhost:9000/api/v1/logout',
	            type: 'POST',
	        	headers: { 
		        	'X-AUTH-TOKEN': ENV.USER_TOKEN
	        	}
	        });
	        ENV.USER_TOKEN = '';
	        window.location.reload(true);
	        this.transitionToRoute('home');
    	}
	}
});

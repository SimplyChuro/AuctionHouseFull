import Component from '@ember/component';
import ENV from 'auction-house/config/environment';
import { later } from '@ember/runloop';

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
	        Ember.getOwner(this).lookup('router:main').transitionTo('home');
			later(this, function() {
			 	window.location.reload(true);
            }, 150);
    	}
	}
});

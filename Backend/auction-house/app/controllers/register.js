import Controller from '@ember/controller';
import { later } from '@ember/runloop';
import { bind } from '@ember/runloop';

export default Controller.extend({
	actions: {
		createUser: function() {		
			let record = this.store.createRecord('user');

			record.set('name', this.get('name'));
			record.set('surname', this.get('surname'));
			record.set('email', this.get('emailAddress'));
			record.set('password', this.get('password'));
			record.save().then(function() {
			});
			this.transitionToRoute('register-success');
			// var responseCode;
			// Ember.$.ajax({
   //              url: 'http://localhost:9000/api/v1/users',
   //              type: 'POST',
   //              data: JSON.stringify({
   //              	name: this.get('name'),
			// 		surname: this.get('surname'),
   //                  email: this.get('emailAddress'),
   //                  password: this.get('password')
   //              }),
   //              contentType: 'application/json;charset=utf-8',
   //              dataType: 'json',
   //              statusCode: {
			// 	    200: function() {
			// 	    	console.log(200, response);
			// 	    },
			// 	    400: function() {
			// 	      console.log(400, response);
			// 	    }
			// 	}
   //          });
			// alert(this,resource)
   //          this.resource('article', { path: ':article_id' });
            // later(this, function() {
            // 	alert(responseCode);
            // }, 200);
			// this.store.createRecord('user', {
			// 	name: this.get('name'),
			//   	surname: this.get('surname'),
			//   	email: this.get('emailAddress'),
			//   	password: this.get('password')
			// }).save().then(null, function() {
  		// 		if(record.get('isError')){
				// 	alert('valid');
				// }else{
				// 	alert('something');
				// }
			// });
			//this.transitionToRoute('register-success');
	
		}
	}
});

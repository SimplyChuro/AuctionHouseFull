import Controller from '@ember/controller';
import ENV from 'auction-house/config/environment';
import { later } from '@ember/runloop';

export default Controller.extend({
	actions: {
        login: function() {
        	Ember.$.ajax({
                url: 'http://localhost:9000/api/v1/login',
                type: 'POST',
                data: JSON.stringify({
                    email: this.get('emailAddress'),
                    password: this.get('password')
                }),
                contentType: 'application/json;charset=utf-8',
                dataType: 'json'
            }).then(function(response) {
                Ember.run(function() {
                    ENV.USER_TOKEN = response.authToken;
                });
            });
            later(this, function() {
                if(ENV.USER_TOKEN !== ''){
                    this.transitionToRoute('home');
                }else{
                    alert("Incorrect Loging Details");
                }
            }, 100);
        }
    }
});
